package com.boomi.connector.groovyconnector;

import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.api.listen.SingletonListenOperation;
import com.boomi.connector.util.listen.UnmanagedListenOperation;
import com.boomi.execution.StdOutLoggerHandler;
import com.boomi.util.StringUtil;
import java.io.StringWriter;
import java.util.logging.Logger;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyListenOperation extends UnmanagedListenOperation implements SingletonListenOperation<ListenManager>{

	Binding _binding;
	GroovyShell _shell;
	Resources resources = new Resources();
	Logger logger = Logger.getLogger("GroovyListenOperation");
	private StringWriter _debugLogWriter;
	private StdOutLoggerHandler _stdoutHandler;
	
    public GroovyListenOperation(OperationContext context) {
        super(context);
		 _shell = GroovyScriptHelpers.getShell();
	     _binding = new Binding();
	     _binding.setVariable("context", this.getContext());	    	 
	     _binding.setVariable("logger", logger);
    }

    public GroovyListenOperation(OperationContext context, StringWriter debugLogWriter) {
    	this(context);
    	this.setRedirectDebugLogger(debugLogWriter);
    }
   	@Override
	public void stop() {
		String scriptName = "stopListener.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName);
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("resources", resources);
            if (_stdoutHandler!=null)
            	_stdoutHandler.setScriptName(scriptName);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for stopListener");
        }
	}

	@Override
	protected void start(Listener listener) {
		String scriptName = "startListener.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName);
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("resources", resources);
            _binding.setVariable("listener", listener);
            if (_stdoutHandler!=null)
            	_stdoutHandler.setScriptName(scriptName);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for startListener");
        }
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	class Resources {
		public Object resource1;
		public Object resource2;
	}

	public void setRedirectDebugLogger(StringWriter debugLogWriter) {
		this._debugLogWriter = debugLogWriter;
		_stdoutHandler = new StdOutLoggerHandler(_debugLogWriter);
		logger.addHandler(_stdoutHandler);
		_binding.setVariable("out", debugLogWriter);
	}
}
