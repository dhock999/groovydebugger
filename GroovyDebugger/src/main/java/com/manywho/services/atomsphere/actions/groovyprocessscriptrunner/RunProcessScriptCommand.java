package com.manywho.services.atomsphere.actions.groovyprocessscriptrunner;

import java.util.Comparator;
import java.util.logging.Logger;

import com.boomi.execution.GroovyRunner;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.groovyprocessscriptrunner.RunProcessScript.Inputs;
import com.manywho.services.atomsphere.actions.groovyprocessscriptrunner.RunProcessScript.Outputs;

public class RunProcessScriptCommand implements ActionCommand<ServiceConfiguration, RunProcessScript, Inputs, Outputs>{
    private Logger logger;

	@Override
	public ActionResponse<Outputs> execute(ServiceConfiguration configuration, ServiceRequest request, Inputs input) {
		logger = Logger.getLogger(this.getClass().getName());
		
		logger.info("input.getDocumentProperties():  " + input.getDocumentProperties().size());
		if (input.getDocumentProperties().size()>0)
		{
			logger.info("Name: " + input.getDocumentProperties().get(0).getName());
			logger.info("Value: " + input.getDocumentProperties().get(0).getValue());
		}
		String consoleOutput="";
		String scriptOutput="";
		logger = Logger.getLogger(this.getClass().getName());
		GroovyRunner gr = new GroovyRunner();
		gr.runProcessScript(input.getScript(), input.getInputDocument(), input.getDocumentProperties(), input.getProcessProperties());
		consoleOutput = gr.getStdout(); 
		scriptOutput = gr.getReturnDocument(); 
 
		return new ActionResponse<>(new Outputs(consoleOutput, scriptOutput, gr.getReturnProcessProperties(), gr.getReturnDocumentProperties()));
	}
	
	class PropertyComparator implements Comparator<PropertyItem> {
		@Override
		public int compare(PropertyItem n1, PropertyItem n2) {
			return n1.getName().compareTo(n2.getName());
		}
	}
}
