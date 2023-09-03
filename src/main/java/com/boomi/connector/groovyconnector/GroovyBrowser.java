package com.boomi.connector.groovyconnector;

import java.io.StringWriter;
import java.util.Collection;
import java.util.logging.Logger;
import com.boomi.connector.api.*;
import com.boomi.connector.util.BaseBrowser;
import com.boomi.connector.util.BaseConnection;
import com.boomi.execution.StdOutLoggerHandler;
import com.boomi.util.StringUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyBrowser extends BaseBrowser implements ConnectionTester {
	Binding _binding;
	GroovyShell _shell;
	Logger _logger = Logger.getLogger("GroovyBrowser");
	private StringWriter _debugLogWriter;
	private StdOutLoggerHandler _stdoutHandler;
	
	protected GroovyBrowser(BaseConnection<BrowseContext> connection) {
		super(connection);
		 _shell = GroovyScriptHelpers.getShell();
	     _binding = new Binding();
	     _binding.setVariable("context", this.getContext());
	     _binding.setVariable("logger", _logger);
	}

	@Override
	public ObjectTypes getObjectTypes() {
		_logger.info("getObjectTypes");
		ObjectTypes objectTypes = null;
		System.out.println("****getObjectTypes");
		String scriptName = "getObjectTypes.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName);
        if (StringUtil.isNotBlank(scriptText))
        {
            objectTypes = (ObjectTypes) GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for getObjectTypes");
        }
        System.out.println("****getObjectTypes" + objectTypes.toString());
		return objectTypes;
	}

	@Override
	public ObjectDefinitions getObjectDefinitions(String objectTypeId, Collection<ObjectDefinitionRole> roles) {
		ObjectDefinitions objectDefinitions = null;
		String scriptName = "getObjectDefinitions.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName);
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("roles", roles);
            _binding.setVariable("objectTypeId", objectTypeId);
            if (_stdoutHandler!=null)
            	_stdoutHandler.setScriptName(scriptName);
            objectDefinitions = (ObjectDefinitions) GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for getObjectDefinitions");
        }

		return objectDefinitions;
	}

	@Override
	public void testConnection() {
		String scriptName = "testConnection.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName);
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
