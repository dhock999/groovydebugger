// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import java.util.Collection;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.ConnectionTester;
import com.boomi.connector.api.ObjectDefinitionRole;
import com.boomi.connector.api.ObjectDefinitions;
import com.boomi.connector.api.ObjectTypes;
import com.boomi.connector.util.BaseBrowser;
import com.boomi.util.Args;

import groovy.lang.Closure;

public class DelegateBrowser extends BaseBrowser implements ConnectionTester{
    
    private final Browser _delegate;
    
    private Closure<?> _getObjectTypes;
    private Closure<?> _getObjectDefinitions;
    private Closure<?> _testConnection;
    
    protected DelegateBrowser(BrowseContext context, Browser delegate) {
        super(context);
        _delegate = Args.notNull(delegate, "delegate is required");
    }

    @Override
    public ObjectTypes getObjectTypes() {
        if (_getObjectTypes != null) {
            return (ObjectTypes) _getObjectTypes.call();
        }
        return _delegate.getObjectTypes();
//        return _delegate.invokeMethod("getObjectTypes", null);
    }

    @Override
    public ObjectDefinitions getObjectDefinitions(String objectTypeId, Collection<ObjectDefinitionRole> roles) {
        if (_getObjectDefinitions != null) {
            return (ObjectDefinitions) _getObjectDefinitions.call(objectTypeId, roles);
        }
        return _delegate.getObjectDefinitions(objectTypeId, roles);
//        return _delegate.invokeMethod("getObjectDefinitions", [ objectTypeId, roles ])        
    }
    
	@Override
	public void testConnection() {
		if (_getObjectTypes != null) {
			_testConnection.call();
        }
	}

	public void getObjectTypes(Closure<?> closure) {
        _getObjectTypes = closure.rehydrate(_delegate, _delegate, closure);
    }
    
    public void getObjectDefinitions(Closure<?> closure) {
        _getObjectDefinitions = closure.rehydrate(_delegate, _delegate, closure); 
    }

	public void testConnection(Closure<?> closure) {
		_testConnection = closure.rehydrate(_delegate, _delegate, closure); 
	}
    
//    public Object propertyMissing(String name) {
//        System.out.println("found it: " + name);
//        return null; 
//    }
    
//    public Object methodMissing(String methodName, Object methodArgs) { 
//        println "missing " + methodName
//        Closure<?> closure = (Closure<?>) ((Object[]) methodArgs)[0];
//        _delegate.metaClass."$methodName" = closure;
//        return null;
//    }  
    
}