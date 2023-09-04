package com.boomi.connector.groovyconnector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;
import com.boomi.connector.api.ConnectorException;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import com.boomi.util.StreamUtil;


public class GroovyScriptHelpers {
    static Logger _logger = Logger.getLogger("GroovyScriptHelpers");
    public static String getGroovyErrorWithScriptCode(String scriptName, String scriptText, Exception e){
        String error = "";
        String message = e.getMessage();
        String targetTraceItem = "Script1.groovy:";
        //The groovy line number is in the stackTrace
        //This occurs for SyntaxException etc
        if (message==null) return message;
        int groovyErrorPosition = message.indexOf(targetTraceItem);
        if (error.length()==0 && groovyErrorPosition>-1)
        {
            //The groovy linenumber is in the error message
            //We get the line number from the message and strip the stacktrace
            int lineNumberStart = groovyErrorPosition+targetTraceItem.length();
            int lineNumberEnd = message.indexOf(":",lineNumberStart+1);
            String lineNumberString = message.substring(lineNumberStart, lineNumberEnd).trim();
            int lineNumber = Integer.parseInt(lineNumberString);
            message = message.substring(lineNumberStart).split("\n")[0]; //We just want the first line
            error = getErrorScriptLines(scriptName, scriptText, message, lineNumber);
        }
        
        if (error.length()==0)
        {
        	 for (StackTraceElement line:e.getStackTrace())
             {
                 if (line.toString().contains(targetTraceItem)) {
                     error = getErrorScriptLines(scriptName, scriptText, e.getClass().getCanonicalName() + " - " + message , line.getLineNumber());
                     break; //if groovy test calling groovy operation, we get 2
                 }
             }
        }
        error = "\n" + message +"\n"+ error;
        return error;
    }
    
    public static String getScript(String scriptName)
    {
        String scriptText = "";
        try {
            scriptText = GroovyScriptHelpers.readResource("resources/"+scriptName);
        } catch (Exception e) {
            _logger.info("No script found which is ok if this method wasn't overridden via groovy");
        }
        return scriptText;
    }
    
    public static String getErrorScriptLines(String scriptName, String scriptText, String message, int lineNumber){
        String error="\n"+scriptName+"\n\n";
//        message = message.split("\n")[0];
        String[] scriptLines = scriptText.split("\\n");
        for (int i=0; i<scriptLines.length; i++) {
            scriptLines[i] = String.format("%4s\t%s\n", i+1, scriptLines[i]);
        }
        int start=lineNumber-6;
        if (start<0)
            start=0;
        int end = lineNumber +4;
        if (end > scriptLines.length-1)
            end = scriptLines.length-1;
        for (int i=start; i< lineNumber-1;i++)
            error+= scriptLines[i];
        error+= ">>>" + " " + scriptLines[lineNumber-1] + " " + message + "\n";
        for (int i=lineNumber; i <= end ;i++)
            error+= scriptLines[i];
        return error;
    }

    public static String readResource(String resourcePath) throws Exception
    {
    	 InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
         if (resource == null) {
             return null; 
         }
         return StreamUtil.toString(resource, "utf-8"); 
    }
    
    public static GroovyShell getShell()
    {
        CompilerConfiguration groovyCompilerConfiguration = new CompilerConfiguration();
        groovyCompilerConfiguration.setDebug(true); //true shows stack traces
        groovyCompilerConfiguration.setVerbose(true); //???What does this do?
        //    groovyCompilerConfiguration.setScriptBaseClass();
        return new GroovyShell(groovyCompilerConfiguration);
    }
    
    public static Object runScript(GroovyShell shell, Binding binding, String scriptName, String scriptText)
    {
        try {
            Script script = shell.parse(scriptText); 
            script.setBinding(binding);
            return script.run();
        } catch (Exception e) {
        	e.printStackTrace();
            String error=GroovyScriptHelpers.getGroovyErrorWithScriptCode(scriptName, scriptText, e);
            throw new ConnectorException(error);
        }
    }
}
