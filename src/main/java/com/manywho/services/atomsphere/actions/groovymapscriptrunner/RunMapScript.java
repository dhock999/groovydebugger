package com.manywho.services.atomsphere.actions.groovymapscriptrunner;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;

@Action.Metadata(name="Run Groovy Map Script", summary = "Execute with Map input and output parameters and the console output", uri="/groovy/executeMapScript")
public class RunMapScript {
	public static class Inputs{
	    @Action.Input(name = "Script", contentType = ContentType.String)
	    private String script;

	    public String getScript() {
	        return script;
	    }

	    @Action.Input(name = "Input Fields", contentType = ContentType.List)
	    private List<MapScriptIOItem> inputFields;

	    public List<MapScriptIOItem> getInputFields() {
	        return inputFields;
	    }
	    
	    @Action.Input(name = "Output Fields", contentType = ContentType.List)
	    private List<MapScriptIOItem> outputFields;

	    public List<MapScriptIOItem> getOutputFields() {
	        return outputFields;
	    }
	}
	
	public static class Outputs {
		@Action.Output(name="Console Output", contentType=ContentType.String)
		private String consoleOutput;
		@Action.Output(name="Output Fields", contentType=ContentType.List)
		private List<MapScriptIOItem> outputFields;

		public Outputs(String consoleOutput, List<MapScriptIOItem> newOutputFields)
		{
			this.consoleOutput=consoleOutput;
			outputFields=newOutputFields;
//			Logger logger = Logger.getLogger(this.getClass().getName());
//			logger.warning("RETURN SIZE: " + outputFields.size() + " OUTPUT" +consoleOutput + newOutputFields.toString());
			for (int i=0; i<newOutputFields.size(); i++)
			{
//				logger.warning("field: "+newOutputFields.get(i).getFieldName() + " " + newOutputFields.get(i).getGuid());
				newOutputFields.get(i).setGuid(UUID.randomUUID().toString());
			}
		}
	}
}
