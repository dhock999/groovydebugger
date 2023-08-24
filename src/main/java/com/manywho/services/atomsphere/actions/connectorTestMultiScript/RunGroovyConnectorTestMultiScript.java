package com.manywho.services.atomsphere.actions.connectorTestMultiScript;

import java.util.List;

import org.jboss.logging.Logger;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;


//TODO currently this just sends one simple executeUpdateOperation script. We need to pass in a list for things like startlistener, stop, getobjecttypes, getobjectdefinitons, getheaders, getentity, getspec..... 
@Action.Metadata(name="Run Groovy Connector Test Multi Script", summary = "Execute a connector test and return console output", uri="/groovy/executeConnectorTestMulti")
public class RunGroovyConnectorTestMultiScript {
	static Logger logger = Logger.getLogger("RunGroovyConnectorTestMultiScript");
	public static class Inputs{
	    @Action.Input(name = "Test Script", contentType = ContentType.String)
	    private String testScript;

	    public String getTestScript() {
	        return testScript;
	    }

	    @Action.Input(name = "Connector Scripts", contentType = ContentType.List)
	    private List<ConnectorScriptItem> connectorScripts;

	    public List<ConnectorScriptItem> getConnectorScripts() {
	        return connectorScripts;
	    }

//	    @Action.Input(name = "getObjectTypes Script", contentType = ContentType.String)
//	    private String getObjectTypesScript;
//
//	    public String getGetObjectTypesScript() {
//	        return getObjectTypesScript;
//	    }
//
//	    @Action.Input(name = "getObjectDefinitions Script", contentType = ContentType.String)
//	    private String getObjectDefinitionsScript;
//
//	    public String getGetObjectDefinitionsScript() {
//	        return getObjectDefinitionsScript;
//	    }
//
//	    @Action.Input(name = "getHeaders Script", contentType = ContentType.String)
//	    private String getHeadersScript;
//
//	    public String getGetHeadersScript() {
//	        return getHeadersScript;
//	    }
//
//	    @Action.Input(name = "getEntity Script", contentType = ContentType.String)
//	    private String getEntityScript;
//
//	    public String getGetEntityScript() {
//	        return getEntityScript;
//	    }
//
//	    @Action.Input(name = "getSpec Script", contentType = ContentType.String)
//	    private String getSpecScript;
//
//	    public String getGetSpecScript() {
//	        return getSpecScript;
//	    }
//
//	    @Action.Input(name = "start Listener Script", contentType = ContentType.String)
//	    private String startListenerScript;
//
//	    public String getStartListenerScript() {
//	        return startListenerScript;
//	    }
//	    
//	    @Action.Input(name = "stop Listener Script", contentType = ContentType.String)
//	    private String stopListenerScript;
//
//	    public String getStopListenerScript() {
//	        return stopListenerScript;
//	    }
	}
	
	public static class Outputs {
		@Action.Output(name="Console Output", contentType=ContentType.String)
		private String consoleOutput;

		public Outputs(String consoleOutput)
		{
			logger.info(consoleOutput);
			this.consoleOutput=consoleOutput;
		}
	}
}
