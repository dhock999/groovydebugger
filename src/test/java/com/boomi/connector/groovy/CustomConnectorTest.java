// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.boomi.connector.api.ContentType;
import com.boomi.connector.api.ObjectDefinition;
import com.boomi.connector.api.ObjectDefinitionRole;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.testutil.SimpleOperationResult;
import com.boomi.util.CollectionUtil;

/**
 * 
 */
public class CustomConnectorTest extends BaseDelegateConnectorTest {

    public CustomConnectorTest() {
        this("custom.groovy");
    }
    
    protected CustomConnectorTest(String resource) {
        super(resource);
    }
    
    
    @Test
    public void testGetObjectTypes() {
        int multiplier = 10; 
        
        List<ObjectType> types = createBrowser().getObjectTypes().getTypes();

        assertEquals(10, types.size());
        for (int i = 1; i < 10; i++) {
            String type = String.valueOf(i * multiplier);
            assertEquals(type, 1, count(type, types));            
        }
    }
    
    @Test
    public void testGetObjectDefinitions() {
        Set<ObjectDefinitionRole> roles = CollectionUtil.asSet(ObjectDefinitionRole.INPUT, ObjectDefinitionRole.OUTPUT);
        
        List<ObjectDefinition> definitions = createBrowser().getObjectDefinitions(null, roles).getDefinitions();
        
        assertEquals(roles.size(), definitions.size());
        ObjectDefinition input = definitions.get(0);
        assertEquals(ContentType.JSON, input.getInputType());
        assertEquals(ContentType.NONE, input.getOutputType());
        assertEquals("", input.getElementName());
        assertEquals("{}", input.getJsonSchema());
        assertNull(input.getSchema());
        
        ObjectDefinition output = definitions.get(1);
        assertEquals(ContentType.NONE, output.getInputType());
        assertEquals(ContentType.JSON, output.getOutputType());
        assertEquals("", output.getElementName());
        assertEquals("{}", output.getJsonSchema());
        assertNull(output.getSchema());
    }
    
    @Test
    public void testExecuteOperation() {
        List<SimpleOperationResult> results = executeOperation("OPOP");
        assertEquals(1, results.size());
        
        SimpleOperationResult result = results.get(0);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertEquals("200", result.getStatusCode());
        assertEquals("great success!", result.getMessage());
        assertTrue(result.getPayloads().isEmpty());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testListenOperation() throws IOException {
        SimpleListenerHandler handler = new SimpleListenerHandler(); 
        
        startListener(handler);
        
        assertEquals("don't do it", toString(handler.getPayloads().get(0)));
    }
}
