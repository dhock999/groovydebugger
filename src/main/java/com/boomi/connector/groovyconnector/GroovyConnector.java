
// Copyright (c) 2022 Boomi, Inc.

package com.boomi.connector.groovyconnector;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.util.BaseConnection;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.util.listen.UnmanagedListenConnector;
import com.boomi.connector.util.listen.UnmanagedListenOperation;

public class  GroovyConnector extends UnmanagedListenConnector {

    public Browser createBrowser(BrowseContext browseContext) {
        return new GroovyBrowser(new BaseConnection(browseContext));
    }
    
    @Override
    public Operation createExecuteOperation(OperationContext operationContext){
        return new GroovyExecuteOperation(new BaseConnection(operationContext));
    }
    
	@Override
	public UnmanagedListenOperation createListenOperation(OperationContext context) {
		return new GroovyListenOperation(context);
	}

}