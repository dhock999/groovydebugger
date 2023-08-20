package com.manywho.services.atomsphere;

import com.manywho.sdk.services.ServiceApplication;
import com.manywho.sdk.services.servers.EmbeddedServer;
import com.manywho.sdk.services.servers.undertow.UndertowServer;
//java -cp git\groovydebugger\target\boomi-groovy-runner-service.jar com.manywho.services.atomsphere.Application
public class Application extends ServiceApplication {
    public static void main(String[] args) throws Exception {
        EmbeddedServer server = new UndertowServer();
        server.setApplication(Application.class);
        server.addModule(new ApplicationModule());
        server.start();
    }
}
