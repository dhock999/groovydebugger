package com.boomi.execution;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import com.boomi.connector.groovyconnector.GroovyScriptHelpers;
import com.manywho.services.atomsphere.actions.connectorTestMultiScript.ConnectorScriptItem;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.MapScriptIOItem;
import com.manywho.services.atomsphere.actions.groovyprocessscriptrunner.PropertyItem;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GroovyRunner {
    private Logger logger;

	String stdout;
	String actualDocs;
	DataContextImpl dataContext;
	String returnDocument="";
	List<PropertyItem> returnDocumentProperties = new ArrayList<PropertyItem>();
	List<PropertyItem> returnProcessProperties  = new ArrayList<PropertyItem>();
	
	public GroovyRunner()
	{
		logger = Logger.getLogger(this.getClass().getName());
	}
	public void runProcessScript(String scriptText, String document, List<PropertyItem> documentProperties, List<PropertyItem> processProperties)
	{
		JSONObject inputDocs = new JSONObject();
		JSONArray documentsArray = new JSONArray();
		inputDocs.put("documents", documentsArray);
		JSONObject documentObject = new JSONObject();
		documentsArray.put(documentObject);
		documentObject.put("docContents", document);
		documentObject.put("docIndex", 0);
		JSONArray processPropertiesArray = new JSONArray();
		inputDocs.put("processProperties", processPropertiesArray);
		JSONArray documentPropertiesArray = new JSONArray();
		documentObject.put("documentProperties", documentPropertiesArray);

		for(PropertyItem item:documentProperties)
		{
			JSONObject property = new JSONObject();
			documentPropertiesArray.put(property);
			property.put("propName", item.getName());
			property.put("propValue", item.getValue());
		}
		
		for(PropertyItem item:processProperties)
		{
			JSONObject property = new JSONObject();
			processPropertiesArray.put(property);
			property.put("propName", item.getName());
			property.put("propValue", item.getValue());
		}

		logger.info("inputDocs: " + inputDocs.toString(2));
		dataContext = new DataContextImpl(inputDocs);

		Binding binding = new Binding();
		if (runGroovy(binding, scriptText))
		{	
			JSONObject actualData = dataContext.getActualData();
			logger.info("actualData: " + actualData.toString(2));
			processPropertiesArray = actualData.getJSONArray("processProperties");
			for(Object itemObj:processPropertiesArray)	
			{
				JSONObject item=(JSONObject)itemObj;
				PropertyItem propertyItem = new PropertyItem();
				returnProcessProperties.add(propertyItem);
				propertyItem.setGuid(UUID.randomUUID().toString());
				if (item.has("propName"))
					propertyItem.setName(item.getString("propName"));
				if (item.has("propValue"))
					propertyItem.setValue(item.getString("propValue"));
			}
			documentsArray=actualData.getJSONArray("documents");
			returnDocument="";
			if (documentsArray.length()>0)
			{
				documentObject=documentsArray.getJSONObject(0);
				if (documentObject.has("docContents"))
					returnDocument=documentObject.getString("docContents");
				for(Object itemObj:documentObject.getJSONArray("documentProperties"))	
				{
					JSONObject item=(JSONObject)itemObj;
					PropertyItem propertyItem = new PropertyItem();
					returnDocumentProperties.add(propertyItem);
					propertyItem.setGuid(UUID.randomUUID().toString());
					if (item.has("propName"))
						propertyItem.setName(item.getString("propName"));
					if (item.has("propValue"))
						propertyItem.setValue(item.getString("propValue"));
				}
			}
		}
	}

	public String getReturnDocument() {
		return returnDocument;
	}

	public List<PropertyItem> getReturnDocumentProperties() {
		return returnDocumentProperties;
	}

	public List<PropertyItem> getReturnProcessProperties() {
		return returnProcessProperties;
	}

	public void runMapScript(String scriptText, List<MapScriptIOItem> inputFields, List<MapScriptIOItem> outputFields)
	{
		Binding binding = new Binding();
		dataContext=null;
		if (this.setMapInputOutputVars(binding, inputFields, outputFields))
		{			
			if (runGroovy(binding, scriptText))
			{
				//for loop to getVariable output field values
				for (MapScriptIOItem outputField:outputFields)
				{
					outputField.setFieldValue(binding.getVariable(outputField.getFieldName()).toString());
				}
			}	
		}		
	}
	
	public void runConnectorTestMulti(String testScript, List<ConnectorScriptItem> operationScripts)
	{
		logger.info("******runConnectorTestMulti " + operationScripts.size());
		Binding binding = new Binding();
		Map<String, Object> connectionProperties = new HashMap<String,Object>();
		for (ConnectorScriptItem item: operationScripts)
		{
			connectionProperties.put(item.getName(), item.getScriptText());
			logger.info("***DEBUGSCRIPT*** " + item.getName() + " " + item.getScriptText());
		}
		binding.setProperty("connectionProperties", connectionProperties);
		runGroovy(binding, testScript);		
	}
	
	public void run(String scriptText, String mockDocs)
	{
		Binding binding = new Binding();
		actualDocs="";

		dataContext = new DataContextImpl(new JSONObject(mockDocs));		
		if(runGroovy(binding, scriptText))		
			actualDocs=dataContext.getActualData().toString(2);
	}
	
	public String getStdout() {
		return stdout;
	}

	public String getActualDocs() {
		return actualDocs;
	}

	private boolean setMapInputOutputVars(Binding binding, List<MapScriptIOItem> inputFields, List<MapScriptIOItem> outputFields)
	{
		//for loop to setVariable input field names and set their values 
		String boomiDateFormat = "yyyyMMDD HHmmss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(boomiDateFormat);
		StringBuilder sbParseErrors=new StringBuilder();
		
		for (MapScriptIOItem inputField:inputFields)
		{
			String inputValue = inputField.getFieldValue();
			Object value=null;
			boolean fieldError=false;
			if (inputField.getFieldType()==null)
				throw new RuntimeException("Field type is null for input field " + inputField.getFieldName());
			switch (inputField.getFieldType())
			{
			case "float":
				value=(float)0;
				try {
					if (inputValue!=null && inputValue.length()>0)
						value = Double.parseDouble(inputValue);
				} catch (NumberFormatException e)
				{
					sbParseErrors.append(String.format("%s: '%s' is not a valid %s format for input field %s\r\n", e.getClass().getName(), inputValue, inputField.getFieldType(), inputField.getFieldName()));
					fieldError=true;
				}
				break;
			case "integer":
				value=0;
				try {
					if (inputValue!=null && inputValue.length()>0)
						value = Long.parseLong(inputValue);
				} catch (NumberFormatException e)
				{
					sbParseErrors.append(String.format("%s: '%s' isn not a valid %s format for input field %s\r\n", e.getClass().getName(), inputValue, inputField.getFieldType(), inputField.getFieldName()));
					fieldError=true;
				}
				break;
			case "datetime":
				try {
					if (inputValue!=null && inputValue.length()>0)
						value = sdf.parse(inputValue);
				} catch (ParseException e)
				{					
					sbParseErrors.append(String.format("%s: '%s' in not a valid %s format for input field %s. Format %s is required.\r\n", e.getClass().getName(), inputValue, inputField.getFieldType(), inputField.getFieldName(), boomiDateFormat));
					fieldError=true;
				}
				break;
			default:
				value = inputValue;
			}
			if (!fieldError)
				binding.setVariable(inputField.getFieldName(), value);
		}
		
		for (MapScriptIOItem outputField:outputFields)
		{
			binding.setVariable(outputField.getFieldName(), "");
		}
		
		if (sbParseErrors.length()>0)
		{
			stdout=sbParseErrors.toString();	
			return false;
		}
		
		return true;
	}
	
	private boolean runGroovy(Binding binding, String scriptText)
	{
		boolean success=true;
		
		//This is a trick to allow a non-static implementation of ExecutionUtil
		scriptText = scriptText.replace("import com.boomi.execution.ExecutionUtil", "");
		StringWriter sw=new StringWriter();
		binding.setProperty("out", sw);
		ExecutionUtilNonStatic ExecutionUtil = new ExecutionUtilNonStatic(sw);
		ExecutionUtil.setDataContextImpl(dataContext);
		binding.setVariable("dataContext", dataContext);
		binding.setVariable("ExecutionUtil", ExecutionUtil);
			
		CompilerConfiguration groovyCompilerConfiguration = new CompilerConfiguration();
		groovyCompilerConfiguration.setDebug(true); //true shows stack traces with line number etc. We want for printing full info
		groovyCompilerConfiguration.setVerbose(true); //???What does this do?
		GroovyShell shell = new GroovyShell(binding, groovyCompilerConfiguration);
		String exception="";
		try {
			Script script = shell.parse(scriptText);
			script.setBinding(binding);
			script.run();			
//		} catch (CompilationFailedException e)
//		{
//			exception=GroovyScriptHelpers.getGroovyErrorWithScriptCode("Test Script", scriptText, e);
//			success=false;
		} catch (Exception e)
		{
			exception=GroovyScriptHelpers.getGroovyErrorWithScriptCode("Test Script", scriptText, e);
			success=false;
		}
//		catch (Throwable e)
//		{
//			exception=GroovyScriptHelpers.getGroovyErrorWithScriptCode("Test Script", scriptText, e);
//			success=false;
//		}
		stdout = sw.toString() + "\n" + exception;
		return success;
	}
}
