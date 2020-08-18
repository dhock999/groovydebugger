package com.manywho.services.atomsphere.actions.groovyrunner;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;

@Action.Metadata(name="Run Groovy Process Script", summary = "Execute input docs and return output docs and the console output", uri="/groovy/executeGroovyScript")
public class RunGroovy {
	public static class Inputs{
	    @Action.Input(name = "Script", contentType = ContentType.String)
	    private String script;

	    public String getScript() {
	        return script;
	    }

	    @Action.Input(name = "Input Documents", contentType = ContentType.String)
	    private String inputDocs;

	    public String getInputDocs() {
	        return inputDocs;
	    }
	}
	
	public static class Outputs {
		@Action.Output(name="Console Output", contentType=ContentType.String)
		private String consoleOutput;
		@Action.Output(name="Output Documents", contentType=ContentType.String)
		private String outputDocs;

		public Outputs(String outputDocs, String consoleOutput)
		{
			this.outputDocs=outputDocs;
			this.consoleOutput=consoleOutput;
		}
	}
}
