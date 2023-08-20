package com.manywho.services.atomsphere.actions.connectorTestMultiScript;

import com.boomi.execution.GroovyRunner;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.connectorTestMultiScript.RunGroovyConnectorTestMultiScript.Inputs;
import com.manywho.services.atomsphere.actions.connectorTestMultiScript.RunGroovyConnectorTestMultiScript.Outputs;


public class RunGroovyConnectorTestMultiScriptCommand implements ActionCommand<ServiceConfiguration, RunGroovyConnectorTestMultiScript, RunGroovyConnectorTestMultiScript.Inputs, RunGroovyConnectorTestMultiScript.Outputs>{

	@Override
	public ActionResponse<Outputs> execute(ServiceConfiguration configuration, ServiceRequest request, Inputs input) {
		String consoleOutput="";

		GroovyRunner gr = new GroovyRunner();
		gr.runConnectorTestMulti(input.getTestScript(), input.getConnectorScripts());

		consoleOutput = gr.getStdout();  
		return new ActionResponse<>(new Outputs(consoleOutput));
	}

}
