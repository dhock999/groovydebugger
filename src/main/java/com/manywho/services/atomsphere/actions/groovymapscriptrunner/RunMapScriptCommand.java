package com.manywho.services.atomsphere.actions.groovymapscriptrunner;

import java.util.logging.Logger;

import com.boomi.execution.GroovyRunner;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.RunMapScript.Inputs;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.RunMapScript.Outputs;

public class RunMapScriptCommand implements ActionCommand<ServiceConfiguration, RunMapScript, RunMapScript.Inputs, RunMapScript.Outputs>{
    private Logger logger;

	@Override
	public ActionResponse<Outputs> execute(ServiceConfiguration configuration, ServiceRequest request, Inputs input) {
		String consoleOutput="";
		logger = Logger.getLogger(this.getClass().getName());
		GroovyRunner gr = new GroovyRunner();
		gr.runMapScript(input.getScript(), input.getInputFields(), input.getOutputFields());
		consoleOutput = gr.getStdout(); 
		logger.info(consoleOutput);
		logger.info(input.getOutputFields().toString());
 
		return new ActionResponse<>(new Outputs(consoleOutput, input.getOutputFields()));
	}
}
