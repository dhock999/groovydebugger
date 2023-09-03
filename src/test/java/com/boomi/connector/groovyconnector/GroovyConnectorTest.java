package com.boomi.connector.groovyconnector;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.boomi.execution.GroovyRunner;
import com.manywho.services.atomsphere.actions.connectorTestMultiScript.ConnectorScriptItem;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroovyConnectorTest {
	static String _executeOperationScript;
	private static String _startListenerScript;
	private static String _stopListenerScript;
    public static void main(String args[]) throws IOException {
    	List<ConnectorScriptItem> operationScripts = new ArrayList<ConnectorScriptItem>();
    	ConnectorScriptItem scriptItem = new ConnectorScriptItem();
    	scriptItem.setName("executeOperation.groovy");
    	scriptItem.setScriptText(new String(Files.readAllBytes(Paths.get("./src/test/executeOperation.groovy"))));
    	operationScripts.add(scriptItem);
    	
    	scriptItem = new ConnectorScriptItem();
    	scriptItem.setName("startListener.groovy");
    	scriptItem.setScriptText(new String(Files.readAllBytes(Paths.get("./src/test/startListener.groovy"))));
    	operationScripts.add(scriptItem);
    	
    	scriptItem = new ConnectorScriptItem();
    	scriptItem.setName("stopListener.groovy");
    	scriptItem.setScriptText(new String(Files.readAllBytes(Paths.get("./src/test/stopListener.groovy"))));
    	operationScripts.add(scriptItem);
    	
        String testScriptText = new String(Files.readAllBytes(Paths.get("./src/test/operationTest.groovy")));
//        testScriptText = new String(Files.readAllBytes(Paths.get("./src/test/browseOpenAPITest.groovy")));
        run(testScriptText, operationScripts);
        
//        testScriptText = new String(Files.readAllBytes(Paths.get("./src/test/BrowseTest.groovy")));
//        run(testScriptText, executeOperationScriptText);
    }

    public static String run(String testScriptText, List<ConnectorScriptItem> operationScripts)
    {
  	  GroovyRunner gr = new GroovyRunner();
  	  gr.runConnectorTestMulti(testScriptText, operationScripts);
  	  System.out.println(gr.getStdout());
        return gr.getStdout();
    }

}

