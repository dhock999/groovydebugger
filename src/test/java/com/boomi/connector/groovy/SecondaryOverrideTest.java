// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.boomi.connector.api.ObjectType;

/**
 * 
 */
@Ignore
public class SecondaryOverrideTest extends BaseDelegateConnectorTest {

    public SecondaryOverrideTest() {
        super("secondary-override.groovy");
    }
    
    @Test
    public void overrideSecondaryBrowserMethod() {
        List<ObjectType> types = createBrowser().getObjectTypes().getTypes();
        System.out.println(types);
    }

}
