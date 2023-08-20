package com.boomi.connector.groovyconnector;

import com.boomi.connector.api.ObjectData;
import com.boomi.connector.openapi.OpenAPIOperation;
import com.boomi.connector.openapi.OpenAPIOperationConnection;
import com.boomi.execution.StdOutLoggerHandler;
import com.boomi.util.StringUtil;
import groovy.lang.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;

public class GroovyOpenAPIOperation extends OpenAPIOperation {
    Logger logger = Logger.getLogger("GroovyOpenAPIOperation");
    Binding _binding;
    GroovyShell _shell;
	private StringWriter _debugLogWriter;
    
    protected GroovyOpenAPIOperation(OpenAPIOperationConnection connection) {
        super(connection);
        _shell = GroovyScriptHelpers.getShell();
        _binding = new Binding();
        _binding.setVariable("context", this.getContext());
        _binding.setVariable("connection", this.getConnection());
	     if (_debugLogWriter!=null)
	    	 logger.addHandler(new StdOutLoggerHandler(_debugLogWriter));
        _binding.setVariable("logger", logger);
    }
    
    @Override
    protected Iterable<Map.Entry<String,String>> getHeaders(ObjectData data) {
        String scriptName = "getHeaders.groovy";
        List<Map.Entry<String,String>> headers = (List<Entry<String, String>>) super.getHeaders(data);
        String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("data", data);
            _binding.setVariable("headers", headers);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        }
        return headers;
    }
    
    @Override
    protected HttpEntity getEntity(ObjectData data) throws IOException {
        String scriptName = "getEntity.groovy";
        String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        HttpEntity entity = super.getEntity(data);
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("data", data);
            _binding.setVariable("entity", entity);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        }
      return entity;
    }
    
    @Override
    public OpenAPIOperationConnection getConnection() {
        return (OpenAPIOperationConnection) super.getConnection();
    }

	public void setRedirectDebugLogger(StringWriter debugLogWriter) {
		this._debugLogWriter = debugLogWriter;
		_binding.setVariable("out", debugLogWriter);
	}
 }
