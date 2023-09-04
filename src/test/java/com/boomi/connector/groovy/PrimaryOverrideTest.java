// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.boomi.connector.api.ContentType;
import com.boomi.connector.api.ObjectDefinition;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.api.OperationType;
import com.boomi.connector.api.Payload;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.testutil.SimpleOperationResult;

/**
 * 
 */
public class PrimaryOverrideTest extends BaseDelegateConnectorTest {


    public PrimaryOverrideTest() {
        super("primary-override.groovy");
    }

    @Test
    public void testListenOperation() throws IOException {
        SimpleListenerHandler handler = new SimpleListenerHandler();

        ListenOperation<?> listen = startListener(handler);
        listen.stop();

        List<Payload> payloads = handler.getPayloads();
        assertEquals(1, payloads.size());
        assertEquals("i did it", toString(payloads.get(0)));
    }
    
    @Test
    public void testExecuteOperation() {
        List<SimpleOperationResult> results = executeOperation("opop");
        assertFalse(results.isEmpty());
        for (SimpleOperationResult result : results) {
            assertEquals(OperationStatus.APPLICATION_ERROR, result.getStatus());
            assertEquals("400", result.getStatusCode());
            assertEquals("bad things!", result.getMessage());
            assertTrue(result.getPayloads().isEmpty());
        }
    }
    
    @Test
    public void testBrowserObjectTypes() {
        List<ObjectType> types = createBrowser().getObjectTypes().getTypes();
        assertEquals(3, types.size());
        assertEquals(1, count("a", types));
        assertEquals(1, count("b", types));
        assertEquals(1, count("c", types));
    }
    
    @Test
    public void testBrowserGetObjectDefinitions() {
        //TODO null? 
        List<ObjectDefinition> definitions = createBrowser().getObjectDefinitions("x", OperationType.EXECUTE.getSupportedDefinitionRoles()).getDefinitions();
        assertEquals(1, definitions.size());
        
        ObjectDefinition definition = definitions.get(0);
        assertNotNull(definition);
        assertEquals(ContentType.NONE, definition.getInputType());
        assertEquals(ContentType.NONE, definition.getOutputType());
    }
}
