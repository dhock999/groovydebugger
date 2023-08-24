package com.boomi.connector.groovyconnector;

import java.io.StringWriter;
import java.util.Collection;
import java.util.logging.Logger;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.ConnectionTester;
import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.ObjectTypes;
import com.boomi.connector.openapi.OpenAPIBrowser;
import com.boomi.connector.openapi.OpenAPIConnection;
import com.boomi.execution.StdOutLoggerHandler;
import com.boomi.util.StringUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyOpenAPIBrowser extends OpenAPIBrowser implements ConnectionTester {
	Binding _binding;
	GroovyShell _shell;
	Logger _logger = Logger.getLogger("GroovyOpenAPIBrowser");
	private StringWriter _debugLogWriter;
	private StdOutLoggerHandler _stdoutHandler;
	
	protected GroovyOpenAPIBrowser(OpenAPIConnection<? extends BrowseContext> connection) {
		super(connection);
		 _shell = GroovyScriptHelpers.getShell();
	     _binding = new Binding();
	     _binding.setVariable("context", this.getContext());
	     _binding.setVariable("logger", _logger);
	}

	@Override
	public ObjectTypes getObjectTypes() {
		ObjectTypes objectTypes = super.getObjectTypes();
		String scriptName = "filterObjectTypes.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
        	_binding.setVariable("objectTypes", this.getContext());
            if (_stdoutHandler!=null)
            	_stdoutHandler.setScriptName(scriptName);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        }
		return objectTypes;
	}

	@Override
	public void testConnection() {
		String scriptName = "testConnection.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            if (_stdoutHandler!=null)
            	_stdoutHandler.setScriptName(scriptName);
          GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for testConnection");
        }
	}

	public void setRedirectDebugLogger(StringWriter debugLogWriter) {
		if (debugLogWriter!=null)
		{
			this._debugLogWriter = debugLogWriter;
			_stdoutHandler = new StdOutLoggerHandler(_debugLogWriter);
			_logger.addHandler(_stdoutHandler);
			_binding.setVariable("out", debugLogWriter);
		}
	}
}
