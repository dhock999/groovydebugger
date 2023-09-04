// Copyright (c) 2023 Boomi, LP
package com.boomi.connector.groovy.test;

import java.util.Collection;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.ContentType;
import com.boomi.connector.api.ObjectDefinition;
import com.boomi.connector.api.ObjectDefinitionRole;
import com.boomi.connector.api.ObjectDefinitions;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.ObjectTypes;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseBrowser;
import com.boomi.connector.util.BaseConnector;
import com.boomi.connector.util.BaseUpdateOperation;

/**
 * Custom connector to test overriding an existing connector in the dsl spec
 */
public class CustomConnector extends BaseConnector {

    @Override
    public Browser createBrowser(BrowseContext context) {
        return new BaseBrowser(context) {
            @Override
            public ObjectTypes getObjectTypes() {
                ObjectTypes types = new ObjectTypes();
                for (int i = 1; i <= 10; i++) {
                    types.getTypes().add(new ObjectType().withId(String.valueOf(i * multiplier())));
                }
                return types;
            }

            @Override
            public ObjectDefinitions getObjectDefinitions(String objectTypeId, Collection<ObjectDefinitionRole> roles) {
                try {
                    ObjectDefinitions definitions = new ObjectDefinitions();
                    for (ObjectDefinitionRole role : roles) {
                        definitions.getDefinitions()
                                .add(new ObjectDefinition().withElementName("")
                                        .withInputType((role == ObjectDefinitionRole.INPUT) ? ContentType.JSON
                                                : ContentType.NONE)
                                        .withOutputType((role == ObjectDefinitionRole.OUTPUT) ? ContentType.JSON
                                                : ContentType.NONE)
                                        .withJsonSchema(getJsonSchema()));
                    }
                    return definitions;
                } catch (Exception e) {
                    throw new ConnectorException(e);
                }
            }

            protected String getJsonSchema() {
                return "{}";
            }

            public int multiplier() {
                return 10;
            }
        };
    }

     @Override
     protected Operation createExecuteOperation(OperationContext context) {
         return new BaseUpdateOperation(context) {
            @Override
            protected void executeUpdate(UpdateRequest request, OperationResponse response) {
                response.addCombinedResult(request, OperationStatus.SUCCESS, "200", "great success!", null);
            }
         };
     }


   
}
