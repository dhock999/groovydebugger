
// Copyright (c) 2022 Boomi, Inc.

package com.boomi.connector.groovyconnector;

import java.io.StringWriter;
import java.util.logging.Logger;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.util.BaseConnection;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.util.listen.UnmanagedListenConnector;
import com.boomi.connector.util.listen.UnmanagedListenOperation;
import com.boomi.execution.StdOutLoggerHandler;

public class  GroovyConnector extends UnmanagedListenConnector {
	StringWriter _debugLogWriter;
	
	public GroovyConnector ()
	{
		super();
	}
	
	public GroovyConnector (StringWriter debugLogWriter)
	{
		this();
		this._debugLogWriter=debugLogWriter;
	}
	
    public Browser createBrowser(BrowseContext browseContext) {
    	GroovyBrowser browser = new GroovyBrowser(new BaseConnection(browseContext));
    	browser.setRedirectDebugLogger(_debugLogWriter);
    	return browser;
    }
    
    @Override
    public Operation createExecuteOperation(OperationContext operationContext){
    	GroovyExecuteOperation operation = new GroovyExecuteOperation(new BaseConnection(operationContext));
    	operation.setRedirectDebugLogger(_debugLogWriter);
    	return operation;
    }
    
    //We never debug with the actual listener, we have to use a mock because not supported by Connector Tester
    //TODO could we extend connector tester someday?
	@Override
	public UnmanagedListenOperation createListenOperation(OperationContext context) {
		GroovyListenOperation operation = new GroovyListenOperation(context);
		return operation;
	}
}