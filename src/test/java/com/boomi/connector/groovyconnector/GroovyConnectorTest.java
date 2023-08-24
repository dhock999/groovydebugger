package com.boomi.connector.groovyconnector;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GroovyConnectorTest {
	static String _executeOperationScript;
	private static String _startListenerScript;
	private static String _stopListenerScript;
    public static void main(String args[]) throws IOException {

        _executeOperationScript = new String(Files.readAllBytes(Paths.get("./src/test/executeOperation.groovy")));
        _startListenerScript = new String(Files.readAllBytes(Paths.get("./src/test/startListener.groovy")));
        _stopListenerScript = new String(Files.readAllBytes(Paths.get("./src/test/stopListener.groovy")));
        String testScriptText = new String(Files.readAllBytes(Paths.get("./src/test/operationTest.groovy")));
//        run(testScriptText);
        testScriptText = new String(Files.readAllBytes(Paths.get("./src/test/browseOpenAPITest.groovy")));
        run(testScriptText);
        
//        testScriptText = new String(Files.readAllBytes(Paths.get("./src/test/BrowseTest.groovy")));
//        run(testScriptText, executeOperationScriptText);
    }
  public static String run(String testScriptText)
  {
      Binding binding = new Binding();
//        _binding.setVariable("context", this.getContext());
      StringWriter sw=new StringWriter();
      binding.setProperty("out", sw);
      CompilerConfiguration groovyCompilerConfiguration = new CompilerConfiguration();
      groovyCompilerConfiguration.setDebug(true); //true shows stack traces
      groovyCompilerConfiguration.setVerbose(true); //???What does this do?
      //    groovyCompilerConfiguration.setScriptBaseClass();
      GroovyShell shell = new GroovyShell(binding, groovyCompilerConfiguration);
      Map<String, Object> connectionProperties = new HashMap<String, Object>();
//      connectionProperties.put("executeOperation.groovy", _executeOperationScript);
//      connectionProperties.put("startListener.groovy", _startListenerScript);
//      connectionProperties.put("stopListener.groovy", _stopListenerScript);
    connectionProperties.put("spec.groovy", _executeOperationScript);
      binding.setProperty("connectionProperties", connectionProperties);
      Script script = shell.parse(testScriptText);
      script.setBinding(binding);
      script.run();
      System.out.println(sw.toString());
      return sw.toString();
  }

}

