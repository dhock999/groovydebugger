// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import java.io.IOException;

import org.junit.Test;

/**
 * 
 */
public class CustomListenConnectorTest extends CustomConnectorTest {

    public CustomListenConnectorTest() {
        super("custom-listen.groovy");
    }

    @Test
    @Override
    public void testListenOperation() throws IOException {
        super.testListenOperation();
    }    

}
