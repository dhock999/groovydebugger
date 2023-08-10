package com.manywho.services.atomsphere.actions.getmapscript;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.MapScriptIOItem;

@Action.Metadata(name="Get Map Script", summary = "Get Map Script and fields", uri="/groovy/getMapScript")
public class GetMapScript {
	public static class Inputs{
	    @Action.Input(name = "Component Id", contentType = ContentType.String)
	    private String componentId;

	    public String getComponentId() {
	        return componentId;
	    }
	}
	
	public static class Outputs {
		@Action.Output(name="Script", contentType=ContentType.String)
		private String script;
		@Action.Output(name="Input Fields", contentType=ContentType.List)
		private List<MapScriptIOItem> inputFields;
		@Action.Output(name="Output Fields", contentType=ContentType.List)
		private List<MapScriptIOItem> outputFields;

		public Outputs(String script, List<MapScriptIOItem> inputFields, List<MapScriptIOItem> outputFields)
		{
			this.script=script;
			this.inputFields = inputFields;
			this.outputFields = outputFields;
//			Logger logger = Logger.getLogger(this.getClass().getName());
//			logger.warning("RETURN SIZE: " + outputFields.size() + " OUTPUT" +consoleOutput + newOutputFields.toString());
			for (int i=0; i<inputFields.size(); i++)
			{
				inputFields.get(i).setGuid(UUID.randomUUID().toString());
			}
			for (int i=0; i<outputFields.size(); i++)
			{
//				logger.warning("field: "+newOutputFields.get(i).getFieldName() + " " + newOutputFields.get(i).getGuid());
				outputFields.get(i).setGuid(UUID.randomUUID().toString());
			}
		}
	}
}
