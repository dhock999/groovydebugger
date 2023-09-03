
// Copyright (c) 2022 Boomi, Inc.

package com.boomi.connector.groovyconnector;

import com.boomi.common.rest.RestConnector;
import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;

import com.boomi.connector.util.BaseConnector;
import com.boomi.connector.util.BaseConnection;

public class  GroovyConnectorRest extends RestConnector {

    public Browser createBrowser(BrowseContext browseContext) {
        return new GroovyBrowser(new BaseConnection(browseContext));
    }
    
    @Override
    public Operation createExecuteOperation(final OperationContext operationContext){
        return new GroovyExecuteOperation(new BaseConnection(operationContext));
    }
}