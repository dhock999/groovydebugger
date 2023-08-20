package com.boomi.connector.groovyconnector;

import com.boomi.connector.api.OperationContext;
import com.boomi.connector.openapi.OpenAPIOperationConnection;

public class GroovyOpenAPIOperationConnection extends OpenAPIOperationConnection {

    private static final String CUSTOM_AUTH_CREDENTIALS_PROPERTY = "customAuthCredentials";

    public GroovyOpenAPIOperationConnection(OperationContext context) {
        super(context);
    }

    public boolean getPreemptive() {
        return true;
    }

    @Override
    public String getCustomAuthCredentials() {
        String apiKey = getContext().getConnectionProperties().getProperty(CUSTOM_AUTH_CREDENTIALS_PROPERTY);
        return "Bearer " + apiKey;
    }
}
