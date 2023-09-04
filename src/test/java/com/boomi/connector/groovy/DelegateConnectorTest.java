// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.boomi.connector.api.ContentType;
import com.boomi.connector.api.ObjectDefinition;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.api.Payload;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.testutil.ConnectorTester;
import com.boomi.connector.testutil.SimpleOperationResult;

/**
 * Test the default spec (connector.groovy) and behaviors
 */
public class DelegateConnectorTest extends BaseDelegateConnectorTest {

    public DelegateConnectorTest() {
        super(new DelegateConnector());
    }
    
    @Test
    public void testListenOperation() throws IOException {
        SimpleListenerHandler handler = new SimpleListenerHandler();

        ListenOperation<?> listen = startListener(handler);
        listen.stop();

        List<Payload> payloads = handler.getPayloads();
        assertEquals(1, payloads.size());
        assertEquals("don't use strings as payloads", toString(payloads.get(0)));
    }

    @Test
    public void testGoodOperation() {
        List<SimpleOperationResult> results = executeOperation("good");
        assertFalse(results.isEmpty());
        for (SimpleOperationResult result : results) {
            assertEquals(OperationStatus.SUCCESS, result.getStatus());
            assertEquals("200", result.getStatusCode());
            assertEquals("great success!", result.getMessage());
            assertTrue(result.getPayloads().isEmpty());
        }
    }
    
    @Test
    public void testBadOperation() {
        List<SimpleOperationResult> results = executeOperation("bad");
        assertFalse(results.isEmpty());
        for (SimpleOperationResult result : results) {
            assertEquals(OperationStatus.APPLICATION_ERROR, result.getStatus());
            assertEquals("400", result.getStatusCode());
            assertEquals("bad things!", result.getMessage());
            assertTrue(result.getPayloads().isEmpty());
        }
    }
    
    @Test(expected = UnsupportedOperationException.class) 
    @Ignore("defect in test-util?")
    public void testUndefinedOperation() {
        ConnectorTester tester = tester("ugly");
        List<InputStream> inputs = inputs("x,", "y", "z");
    
        List<SimpleOperationResult> results = tester.executeExecuteOperation(inputs);
    
        assertEquals(inputs.size(), results.size());
    }
    
    @Test(expected = UnsupportedOperationException.class) 
    public void testUndefinedOperationNoInputs() {
        tester("ugly").executeExecuteOperation(Collections.emptyList());
    }
    
    @Test
    public void testBrowserObjectTypes() {
        List<ObjectType> types = createBrowser().getObjectTypes().getTypes();
        assertEquals(3, types.size());
        assertEquals(1, count("x", types));
        assertEquals(1, count("y", types));
        assertEquals(1, count("z", types));
    }
    
    @Test
    public void testBrowserGetObjectDefinitions() {
        List<ObjectDefinition> definitions = createBrowser().getObjectDefinitions(null, null).getDefinitions();
        assertEquals(1, definitions.size());
        
        ObjectDefinition definition = definitions.get(0);
        assertNotNull(definition);
        assertEquals(ContentType.BINARY, definition.getInputType());
        assertEquals(ContentType.BINARY, definition.getOutputType());
    }
}
