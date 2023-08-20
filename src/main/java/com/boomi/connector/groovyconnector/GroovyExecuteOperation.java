
// Copyright (c) 2022 Boomi, Inc.

package com.boomi.connector.groovyconnector;


import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseConnection;
import com.boomi.connector.util.BaseUpdateOperation;

import com.boomi.util.StringUtil;
import groovy.lang.*;
import java.util.logging.Logger;

public class GroovyExecuteOperation extends BaseUpdateOperation {
    Logger logger = Logger.getLogger("GroovyExecuteOperation");

    //TODO we want to simplify overhead of having a script launcher in every class....but we also want to stay factored for things like compiled scripts in the connector cache....
    Binding _binding;
    GroovyShell _shell;
    protected GroovyExecuteOperation(BaseConnection connection) {
        super(connection);
        _shell = GroovyScriptHelpers.getShell();
        _binding = new Binding();
        _binding.setVariable("context", this.getContext());

    }
    @Override
    protected void executeUpdate(UpdateRequest request, OperationResponse response) {
        String scriptName = "executeOperation.groovy";
        logger = response.getLogger();
        //TODO how do we get this to stdio during test time?
        logger.info("executeOperation Launching Groovy Script");
        
        String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("request", request);
            _binding.setVariable("response", response);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for executeOperation");
        }
    }
 }