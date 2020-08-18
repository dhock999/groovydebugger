package com.manywho.services.atomsphere.actions.groovyrunner;

import com.boomi.execution.GroovyRunner;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.groovyrunner.RunGroovy.Inputs;
import com.manywho.services.atomsphere.actions.groovyrunner.RunGroovy.Outputs;

public class RunGroovyCommand implements ActionCommand<ServiceConfiguration, RunGroovy, RunGroovy.Inputs, RunGroovy.Outputs>{


	@Override
	public ActionResponse<Outputs> execute(ServiceConfiguration configuration, ServiceRequest request, Inputs input) {
		String consoleOutput="";
		String outputDocs="";
		try {
			GroovyRunner gr = new GroovyRunner();
			gr.run(input.getScript(), input.getInputDocs());

			consoleOutput = gr.getStdout(); 
			outputDocs = gr.getActualDocs();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
 
		return new ActionResponse<>(new Outputs(outputDocs, consoleOutput));
	}
}
