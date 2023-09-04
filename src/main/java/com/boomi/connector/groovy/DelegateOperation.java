// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.OperationRequest;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.util.BaseOperation;
import com.boomi.util.Args;

import groovy.lang.Closure;

public class DelegateOperation extends BaseOperation {

    private final Operation _delegate; 
    
    private Closure<?> _execute; 
    
    protected DelegateOperation(OperationContext context, Operation delegate) {
        super(context); 
        _delegate = Args.notNull(delegate, "delegate is required");
    }

    @Override
    public void execute(OperationRequest request, OperationResponse response) {
        if (_execute != null) {
            _execute.call(request, response);    
        } else {
            _delegate.execute(request, response);
        }
    }
    
    public void execute(Closure<?> closure) {
        _execute = closure.rehydrate(this, this, closure); 
    }
    
}