package com.boomi.connector.groovyconnector;

import java.io.StringWriter;
import java.util.logging.Logger;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.openapi.OpenAPIConnection;
import com.boomi.util.StringUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyOpenAPIConnection extends OpenAPIConnection{

	Binding _binding;
	GroovyShell _shell;
	Logger logger = Logger.getLogger("GroovyOpenAPIConnection");
	StringWriter _debugLogWriter;

	public GroovyOpenAPIConnection(BrowseContext context) {
		super(context);
		 _shell = GroovyScriptHelpers.getShell();
	     _binding = new Binding();
	     _binding.setVariable("context", this.getContext());
	}
	
    @Override
    public String getSpec() {
    	String spec = super.getSpec();
		String scriptName = "getSpec.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("spec", spec);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        }
        return spec;
    }
    
	public void setRedirectDebugLogger(StringWriter debugLogWriter) {
		this._debugLogWriter = debugLogWriter;
		_binding.setVariable("out", debugLogWriter);
	}
}
