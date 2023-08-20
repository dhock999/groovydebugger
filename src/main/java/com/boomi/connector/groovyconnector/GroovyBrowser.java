package com.boomi.connector.groovyconnector;

import java.util.Collection;
import java.util.logging.Logger;
import com.boomi.connector.api.*;
import com.boomi.connector.util.BaseBrowser;
import com.boomi.connector.util.BaseConnection;
import com.boomi.util.StringUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyBrowser extends BaseBrowser{
	Binding _binding;
	GroovyShell _shell;
	Logger _logger = Logger.getLogger("GroovyBrowser");
	protected GroovyBrowser(BaseConnection<BrowseContext> connection) {
		super(connection);
		 _shell = GroovyScriptHelpers.getShell();
	     _binding = new Binding();
	     _binding.setVariable("context", this.getContext());
	}

	@Override
	public ObjectTypes getObjectTypes() {
		_logger.info("getObjectTypes");
		ObjectTypes objectTypes = new ObjectTypes();
		String scriptName = "getObjectTypes.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("objectTypes", objectTypes);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for getObjectTypes");
        }
		return objectTypes;
	}

	@Override
	public ObjectDefinitions getObjectDefinitions(String objectTypeId, Collection<ObjectDefinitionRole> roles) {
		ObjectDefinitions objectDefinitions = new ObjectDefinitions();
		String scriptName = "getObjectDefinitions.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("objectDefinitions", objectDefinitions);
            _binding.setVariable("roles", roles);
            _binding.setVariable("objectTypeId", objectTypeId);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for getObjectDefinitions");
        }

		return objectDefinitions;
	}
}
