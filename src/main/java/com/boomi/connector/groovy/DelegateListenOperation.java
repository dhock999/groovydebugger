// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.util.listen.BaseListenOperation;
import com.boomi.util.Args;

import groovy.lang.Closure;

/**
 * 
 */
@SuppressWarnings("rawtypes")
public class DelegateListenOperation extends BaseListenOperation {

    private final ListenOperation<ListenManager> _delegate;
    
    private Closure<?> _start; 
    private Closure<?> _stop; 
    
    public DelegateListenOperation(OperationContext context, ListenOperation<ListenManager> delegate) {
        super(context);
        _delegate = Args.notNull(delegate, "delegate is required"); 
    }

    @Override
    public void stop() {
        if (_stop != null) {
            _stop.call();
        } else {
           _delegate.stop();
        }
    }

    @Override
    public void start(Listener listener, ListenManager manager) {
        if (_start != null) {
            _start.call(listener);
        } else {
            _delegate.start(listener, manager);
        }
        
    }
   
    public void listen(Closure<?> closure) {
        closure.setDelegate(this);
        closure.call();
    }

    public void start(Closure<?> closure) {
        _start = closure.rehydrate(this, this, closure);
    }
    
    public void stop(Closure<?> closure) {
        _stop = closure.rehydrate(this, this, closure);
    }

}
