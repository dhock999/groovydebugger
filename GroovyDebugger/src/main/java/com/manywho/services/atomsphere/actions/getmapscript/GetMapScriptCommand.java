package com.manywho.services.atomsphere.actions.getmapscript;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.atomsphere.AtomsphereAPI;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.getmapscript.GetMapScript.Inputs;
import com.manywho.services.atomsphere.actions.getmapscript.GetMapScript.Outputs;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.MapScriptIOItem;

public class GetMapScriptCommand implements ActionCommand<ServiceConfiguration, GetMapScript, Inputs, Outputs>{
    private Logger logger;

	@Override
	public ActionResponse<Outputs> execute(ServiceConfiguration configuration, ServiceRequest request, Inputs input) {
		String script="";
		input.getComponentId();
		List<MapScriptIOItem> inputFields = new ArrayList<MapScriptIOItem>();
		List<MapScriptIOItem> outputFields = new ArrayList<MapScriptIOItem>();
		
		logger = Logger.getLogger(this.getClass().getName());
		try {
			String boomiDateFormat = "yyyyMMDD HHmmss.SSS";
			SimpleDateFormat sdf = new SimpleDateFormat(boomiDateFormat);

			Document response = AtomsphereAPI.executeAPIXML(configuration, "Component", "GET", input.getComponentId(), null, false);
			NodeList elements = response.getElementsByTagName("script");
			Node node = elements.item(0);
			script = node.getTextContent();
			
			elements = response.getElementsByTagName("Input");
			for(int i=0; i<elements.getLength(); i++)
			{
				Element element = (Element)elements.item(i);
				MapScriptIOItem item = new MapScriptIOItem();
				inputFields.add(item);
				item.setGuid(UUID.randomUUID().toString());
				item.setFieldName(element.getAttribute("name"));
				String fieldType = element.getAttribute("dataType");
				if (fieldType==null || fieldType.length()==0)
					fieldType = "character";
				item.setFieldType(fieldType);	
				String fieldValue;
				switch (fieldType)
				{
				case "integer":
					fieldValue="0";
					break;
				case "float":
					fieldValue="0.0";
					break;
				case "datetime":
					fieldValue=sdf.format(new Date());
					break;
				default:
					fieldValue="a string";
				}
				item.setFieldValue(fieldValue);
			}

			elements = response.getElementsByTagName("Output");
			for(int i=0; i<elements.getLength(); i++)
			{
				Element element = (Element)elements.item(i);
				MapScriptIOItem item = new MapScriptIOItem();
				outputFields.add(item);
				item.setGuid(UUID.randomUUID().toString());
				item.setFieldName(element.getAttribute("name"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
 
		return new ActionResponse<>(new Outputs(script, inputFields, outputFields));
	}
}
