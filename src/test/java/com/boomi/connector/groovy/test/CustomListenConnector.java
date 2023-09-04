// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy.test;

import com.boomi.connector.api.ConnectorContext;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.listen.ListenConnector;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.api.listen.Listener;

/**
 * 
 */
@SuppressWarnings("rawtypes")
public class CustomListenConnector extends CustomConnector implements ListenConnector {

    @Override
    public ListenOperation<?> createListenOperation(OperationContext context) {
        return new ListenOperation() {

            @Override
            public void start(Listener listener, ListenManager manager) {
                listener.submit(PayloadUtil.toPayload("don't do it"));
            }

            @Override
            public void stop() {

            }

        };
    }

    @Override
    public ListenManager createListenManager(ConnectorContext context) {
        // TODO Auto-generated method stub
        return null;
    }

}
