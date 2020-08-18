package com.manywho.services.atomsphere.actions.getprocessscript;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;

@Action.Metadata(name="Get Process Script", summary = "Return the text of the process script", uri="/groovy/getProcessScript")
public class GetProcessScript {
	public static class Inputs{
	    @Action.Input(name = "Component Id", contentType = ContentType.String)
	    private String componentId;

	    public String getComponentId() {
	        return componentId;
	    }
	}
	
	public static class Outputs {
		@Action.Output(name="Process Script", contentType=ContentType.String)
		private String script;

		public Outputs(String script)
		{
			this.script=script;
		}
	}
}
