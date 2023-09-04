// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.boomi.connector.api.AtomContext;
import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Connector;
import com.boomi.connector.api.ConnectorContext;
import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.ObjectDefinitionRole;
import com.boomi.connector.api.ObjectDefinitions;
import com.boomi.connector.api.ObjectTypes;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.OperationRequest;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.listen.ListenConnector;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.groovyconnector.GroovyScriptHelpers;
import com.boomi.connector.util.listen.BaseListenConnector;
import com.boomi.connector.util.listen.UnmanagedListenConnector;
import com.boomi.connector.util.listen.UnmanagedListenOperation;
import com.boomi.execution.StdOutLoggerHandler;
import com.boomi.util.Args;
import com.boomi.util.ClassUtil;
import com.boomi.util.StringUtil;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;

@SuppressWarnings("rawtypes")
public class DelegateConnector extends BaseListenConnector {

    private static final String DEFAULT_CONNECTOR_SPEC = "connector.groovy";
    private static final ListenConnector<?> NO_DELEGATE = new NoDelegateConnector();

    private final OperationMap _operations = new OperationMap();

    private Closure<?> _browserConfig;
    private ListenConnector<?> _delegate = NO_DELEGATE;
	private Logger _logger = Logger.getLogger("DelegateConnector");

    public DelegateConnector() {
        this(DEFAULT_CONNECTOR_SPEC, null);
    }

    public DelegateConnector(StringWriter debugLogWriter) {
        this(DEFAULT_CONNECTOR_SPEC, debugLogWriter);
    }

    public DelegateConnector(String resourceName) {
        this(DEFAULT_CONNECTOR_SPEC, null);
    }

    public DelegateConnector(String resourceName, StringWriter debugLogWriter) {
        try {
        	String script = GroovyScriptHelpers.getScript(resourceName);
        	if (StringUtil.isBlank(script))
        		throw new Exception(resourceName + " not found");
        	_logger.info("DelegateConnector");
            CompilerConfiguration compilerConfig = new CompilerConfiguration();
            compilerConfig.setScriptBaseClass(DelegatingScript.class.getName());
            GroovyShell shell = new GroovyShell(compilerConfig);
            DelegatingScript ds = (DelegatingScript) shell.parse(script);
            if (debugLogWriter!=null)
            {
            	ds.getBinding().setVariable("out", debugLogWriter);
            	StdOutLoggerHandler stdoutHandler = new StdOutLoggerHandler(debugLogWriter);
            	stdoutHandler.setScriptName(resourceName);
    			_logger.addHandler(stdoutHandler);
            }
            ds.setDelegate(this);
        	shell.setVariable("logger", _logger);
            ds.run();
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ConnectorException(e);
        }
    }

    private InputStreamReader getReader(String resourceName) {
        InputStream resourceAsStream =
                Args.notNull(ClassUtil.getResourceAsStream(resourceName), resourceName + " not found");
        return new InputStreamReader(resourceAsStream);
    }

    public void connector(Closure<?> connector) {
        connector.call();
    }
 
    public <C extends Connector> void connector(Class<C> clazz, Closure<?> connector) {
        try {
            _delegate = new SafeDelegateConnector(clazz.getConstructor().newInstance());
        } catch (Exception e) {
            throw new ConnectorException("failed to instantiate " + clazz);
        }
        connector(connector);
    }

    public void browser(Closure<?> browser) {
        _browserConfig = browser;
    }

    public void operations(Closure<?> operations) {
        operations.setDelegate(_operations);
        operations.call();
    }

    @Override
    public Browser createBrowser(BrowseContext context) {
        Browser browser = _delegate.createBrowser(context);
        if (_browserConfig == null) {
            return browser; 
        }
        DelegateBrowser delegateBrowser = new DelegateBrowser(context, browser);
        _browserConfig.rehydrate(delegateBrowser, browser, this).call();
        return delegateBrowser;
    }

    @Override
    public Operation createOperation(OperationContext context) {
        Operation operation = _delegate.createOperation(context);
        Closure<?> operationConfig = _operations.getOperation(context.getCustomOperationType());
        if (operationConfig == null) {
            return operation;
        }
        DelegateOperation delegateOperation = new DelegateOperation(context, operation);
        operationConfig.rehydrate(delegateOperation, this, this).call();
        return delegateOperation;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListenOperation<?> createListenOperation(OperationContext context) {
        ListenOperation<ListenManager> operation = (ListenOperation<ListenManager>) _delegate.createListenOperation(context);
        Closure<?> operationConfig = _operations.getOperation(context.getCustomOperationType());
        if (operationConfig == null) {
            return operation; 
        }
        DelegateListenOperation delegateOperation = new DelegateListenOperation(context, operation);
        operationConfig.rehydrate(delegateOperation, this, this).call();
        return delegateOperation;
    }
    
    @Override
    public ListenManager createListenManager(ConnectorContext context) {
        // TODO overrides? 
        return _delegate.createListenManager(context);
    }

    private static class OperationMap {

        private final Map<String, Closure<?>> _operations = new HashMap<>();

        @SuppressWarnings("unused")
        public Object methodMissing(String methodName, Object methodArgs) {
            _operations.put(methodName, (Closure<?>) ((Object[]) methodArgs)[0]);
            return null;
        }

        public Closure<?> getOperation(String name) {
            return _operations.get(name);
        }

    }

    private static class SafeDelegateConnector extends BaseListenConnector {

        private final Connector _delegate;

        public SafeDelegateConnector(Connector delegate) {
            _delegate = delegate;
        }

        @Override
        public ListenManager createListenManager(ConnectorContext context) {
            if (_delegate instanceof ListenConnector) {
                try {
                    return ((ListenConnector) _delegate).createListenManager(context);
                } catch (UnsupportedOperationException e) {
                    // do nothing
                }
            }
            return NO_DELEGATE.createListenManager(context);
        }

        @Override
        public ListenOperation<?> createListenOperation(OperationContext context) {
            if (_delegate instanceof ListenConnector) {
                try {
                    return ((ListenConnector) _delegate).createListenOperation(context);
                } catch (UnsupportedOperationException e) {
                    // do nothing
                }
            }
            return NO_DELEGATE.createListenOperation(context);
        }

        @Override
        public Browser createBrowser(BrowseContext context) {
            try {
                return _delegate.createBrowser(context);
            } catch (UnsupportedOperationException e) {
                // do nothing
            }
            return NO_DELEGATE.createBrowser(context);
        }

        @Override
        public Operation createOperation(OperationContext context) {
            try {
                return _delegate.createOperation(context);
            } catch (UnsupportedOperationException e) {
                // do nothing
            }
            return NO_DELEGATE.createOperation(context);
        }
        

        @Override
        public void initialize(AtomContext context) {
            // TODO Auto-generated method stub
        }

    }

    private static class NoDelegateConnector extends UnmanagedListenConnector {

        @Override
        public Browser createBrowser(BrowseContext context) {
            return new Browser() {

                @Override
                public ObjectTypes getObjectTypes() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public ObjectDefinitions getObjectDefinitions(String objectTypeId,
                                                              Collection<ObjectDefinitionRole> roles) {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public Operation createOperation(OperationContext context) {
            return new Operation() {
                @Override
                public void execute(OperationRequest request, OperationResponse response) {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public UnmanagedListenOperation createListenOperation(OperationContext context) {
            return new UnmanagedListenOperation(context) {
                @Override
                public void stop() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void start(Listener listener) {
                    throw new UnsupportedOperationException();
                }
            };
        }

    }

}