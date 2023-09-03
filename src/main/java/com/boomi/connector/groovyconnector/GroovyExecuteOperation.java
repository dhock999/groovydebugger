package com.boomi.connector.groovyconnector;

import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseConnection;
import com.boomi.connector.util.BaseUpdateOperation;
import com.boomi.execution.StdOutLoggerHandler;
import com.boomi.util.StringUtil;
import groovy.lang.*;

import java.io.StringWriter;
import java.util.logging.Logger;

public class GroovyExecuteOperation extends BaseUpdateOperation {
    Logger _logger = Logger.getLogger("GroovyExecuteOperation");

    //TODO we want to simplify overhead of having a script launcher in every class....but we also want to stay factored for things like compiled scripts in the connector cache....
    Binding _binding;
    GroovyShell _shell;

	private StringWriter _debugLogWriter;

	private StdOutLoggerHandler _stdoutHandler;
    protected GroovyExecuteOperation(BaseConnection connection) {
        super(connection);
        _shell = GroovyScriptHelpers.getShell();
        _binding = new Binding();
        _binding.setVariable("context", this.getContext());
        _binding.setVariable("logger", _logger);
    }
    @Override
    protected void executeUpdate(UpdateRequest request, OperationResponse response) {
        String scriptName = "executeOperation.groovy";
        _logger = response.getLogger();
        //TODO how do we get this to stdio during test time?
        _logger.info("executeOperation Launching Groovy Script");
        
        String scriptText = GroovyScriptHelpers.getScript(scriptName);
        if (StringUtil.isNotBlank(scriptText))
        {
        	setRedirectDebugLogger(_debugLogWriter);
            if (_stdoutHandler!=null)
            	_stdoutHandler.setScriptName(scriptName);
            if (_debugLogWriter!=null)
            	_logger.addHandler(_stdoutHandler);
            _binding.setVariable("logger", _logger);
            _binding.setVariable("request", request);
            _binding.setVariable("response", response);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for executeOperation");
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
