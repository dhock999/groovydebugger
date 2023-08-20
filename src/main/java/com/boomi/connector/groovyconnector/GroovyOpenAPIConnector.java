
// Copyright (c) 2022 Boomi, Inc.

package com.boomi.connector.groovyconnector;

import java.io.StringWriter;
import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.openapi.OpenAPIConnector;
import com.boomi.connector.openapi.OpenAPIOperationConnection;

public class GroovyOpenAPIConnector extends OpenAPIConnector {
	StringWriter _debugLogWriter;
	
	public GroovyOpenAPIConnector ()
	{
		super();
	}

	public GroovyOpenAPIConnector (StringWriter debugLogWriter)
	{
		super();
		this._debugLogWriter=debugLogWriter;
	}
	
    public Browser createBrowser(BrowseContext browseContext) {
        return new GroovyOpenAPIBrowser(new GroovyOpenAPIConnection(browseContext));
    }

    @Override
    public Operation createExecuteOperation(final OperationContext operationContext){
    	GroovyOpenAPIOperation operation = new GroovyOpenAPIOperation(new GroovyOpenAPIOperationConnection(operationContext));
    	operation.setRedirectDebugLogger(_debugLogWriter);
    	return operation;
    }
}