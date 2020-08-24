package com.manywho.services.atomsphere.actions.groovyprocessscriptrunner;

import java.util.List;
import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;


@Action.Metadata(name="Run Groovy Process Script With Params", summary = "Execute with Map input and output parameters and the console output", uri="/groovy/executeProcessScript")
public class RunProcessScript {
	public static class Inputs{
	    @Action.Input(name = "Script", contentType = ContentType.String)
	    private String script;

	    public String getScript() {
	        return script;
	    }

	    @Action.Input(name = "Input Document", contentType = ContentType.String)
	    private String document;

	    public String getInputDocument() {
	        return document;
	    }
	    
	    @Action.Input(name = "Input Document Properties", contentType = ContentType.List)
	    private List<PropertyItem> documentProperties;

	    public List<PropertyItem> getDocumentProperties() {
	        return documentProperties;
	    }

	    @Action.Input(name = "Input Process Properties", contentType = ContentType.List)
	    private List<PropertyItem> processProperties;

	    public List<PropertyItem> getProcessProperties() {
	        return processProperties;
	    }
	}
	
	public static class Outputs {
		@Action.Output(name="Console Output", contentType=ContentType.String)
		private String consoleOutput;

		@Action.Output(name="Output Document", contentType=ContentType.String)
		private String documentOutput;

		@Action.Output(name="Output Document Properties", contentType=ContentType.List)
		private List<PropertyItem> documentProperties;

		@Action.Output(name="Output Process Properties", contentType=ContentType.List)
		private List<PropertyItem> processProperties;

		public Outputs(String consoleOutput, String documentOutput, List<PropertyItem> processProperties, List<PropertyItem> documentProperties)
		{
			this.consoleOutput=consoleOutput;
			this.documentOutput=documentOutput;
			this.processProperties=processProperties;
			this.documentProperties=documentProperties;
		}
	}
}
