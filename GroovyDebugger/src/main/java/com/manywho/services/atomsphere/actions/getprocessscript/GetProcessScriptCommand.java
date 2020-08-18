package com.manywho.services.atomsphere.actions.getprocessscript;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.atomsphere.AtomsphereAPI;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.getprocessscript.GetProcessScript.Inputs;
import com.manywho.services.atomsphere.actions.getprocessscript.GetProcessScript.Outputs;

public class GetProcessScriptCommand implements ActionCommand<ServiceConfiguration, GetProcessScript, Inputs, Outputs>{

	@Override
	public ActionResponse<Outputs> execute(ServiceConfiguration configuration, ServiceRequest request, Inputs input) {
		String script="";
		try {
			Document response = AtomsphereAPI.executeAPIXML(configuration, "Component", "GET", input.getComponentId(), null, false);
			NodeList elements = response.getElementsByTagName("script");
			Node node = elements.item(0);
			script = node.getTextContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return new ActionResponse<>(new Outputs(script));
	}
}
