// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Connector;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.OperationType;
import com.boomi.connector.api.Payload;
import com.boomi.connector.api.PayloadMetadata;
import com.boomi.connector.api.listen.IndexedPayloadBatch;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.api.listen.ListenerExecutionResult;
import com.boomi.connector.api.listen.PayloadBatch;
import com.boomi.connector.api.listen.SubmitOptions;
import com.boomi.connector.testutil.ConnectorTestContext;
import com.boomi.connector.testutil.ConnectorTester;
import com.boomi.connector.testutil.SimpleOperationResult;
import com.boomi.util.Args;

/**
 * 
 */
public abstract class BaseDelegateConnectorTest  {

    protected final DelegateConnector _connector; 
    
    protected BaseDelegateConnectorTest(DelegateConnector connector) {
        _connector = connector; 
    }
    
    protected BaseDelegateConnectorTest(String resource) {
        this(new DelegateConnector(resource));
    }
     
    protected static List<InputStream> inputs(String... strings) {
        return Stream.of(strings).map(s -> new ByteArrayInputStream(s.getBytes())).collect(Collectors.toList());
    }

    protected Browser createBrowser() {
        return _connector.createBrowser(createContext(""));
    }
    
    protected ListenOperation<?> createListenOperation() {
        return _connector.createListenOperation(createContext("listen"));
    }
    
    protected ListenOperation<?> startListener(TestListenerHandler handler) {
        //TODO manager? 
        ListenOperation<?> operation = createListenOperation();
        operation.start(new TestListener(handler), null);
        return operation; 
    }

    protected List<SimpleOperationResult> executeOperation(String customOperationType) {
        ConnectorTester tester = tester(customOperationType);
        List<InputStream> inputs = inputs("x,", "y", "z");
        return tester.executeExecuteOperation(inputs);
    }

    protected ConnectorTester tester(String customOperationType) {
        ConnectorTester tester = new ConnectorTester(_connector);
        tester.setOperationContext(createContext(customOperationType));
        return tester;
    }

    protected static String toString(Payload payload) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        payload.writeTo(out);
        return new String(out.toByteArray());
    }

    protected static ConnectorTestContext createContext(String customOperationType) {
        ConnectorTestContext context = new ConnectorTestContext() {
            @Override
            protected Class<? extends Connector> getConnectorClass() {
                return DelegateConnector.class;
            }
        };
        context.setOperationType((OperationType.LISTEN.name().equalsIgnoreCase(customOperationType)
                ? OperationType.LISTEN
                : OperationType.EXECUTE));
        context.setOperationCustomType(customOperationType);
        return context;
    }

    protected static long count(String id, List<ObjectType> types) {
        return types.stream().filter(t -> id.equalsIgnoreCase(t.getId())).count();
    }
    
    //TODO test-util
    protected static class TestListener implements Listener {
    
        private final TestListenerHandler _handler;
    
        protected TestListener(TestListenerHandler handler) {
            _handler = Args.notNull(handler, "handler is required");
        }
    
        @Override
        public PayloadMetadata createMetadata() {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public void submit(Payload payload) {
            _handler.onSubmit(payload);
        }
    
        @Override
        public void submit(Throwable error) {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public Future<ListenerExecutionResult> submit(Payload payload, SubmitOptions options) {
            // TODO options
            _handler.onSubmit(payload);
            // TODO let handler do this?
            return CompletableFuture.completedFuture(null);
        }
    
        @Override
        public PayloadBatch getBatch() {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public <T> IndexedPayloadBatch<T> getBatch(T index) {
            throw new UnsupportedOperationException();
        }
    
    }

    protected static interface TestListenerHandler {
    
        void onSubmit(Payload payload);
    
    }

    protected static class SimpleListenerHandler implements TestListenerHandler {
    
        private final List<Payload> _payloads = new ArrayList<>();
    
        @Override
        public void onSubmit(Payload payload) {
            _payloads.add(payload);
        }
    
        public List<Payload> getPayloads() {
            return _payloads;
        }
    
    }

}