package com.boomi.execution;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.boomi.execution.DataContextImpl;
import com.boomi.execution.ExecutionUtilNonStatic;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.MapScriptIOItem;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GroovyRunner {

	String stdout;
	String actualDocs;
	
	public void run(String scriptText, String mockDocs)
	{
		Binding binding = new Binding();
		stdout="";
		actualDocs="";
		CompilerConfiguration groovyCompilerConfiguration = new CompilerConfiguration();
		groovyCompilerConfiguration.setDebug(false); //true shows stack traces
		groovyCompilerConfiguration.setVerbose(false); //???What does this do?
		StringWriter sw=new StringWriter();
		binding.setProperty("out", sw);

		DataContextImpl dataContext = new DataContextImpl(mockDocs);
		ExecutionUtilNonStatic ExecutionUtil = new ExecutionUtilNonStatic();
		ExecutionUtil.setDataContextImpl(dataContext);
		binding.setVariable("dataContext", dataContext);
		binding.setVariable("ExecutionUtil", ExecutionUtil);
			
		GroovyShell shell = new GroovyShell(binding, groovyCompilerConfiguration);
		stdout=null;
		try {
			Script script = shell.parse(scriptText);
			script.setBinding(binding);
			script.run();			
		} catch (CompilationFailedException e)
		{
			stdout=e.getMessage();
		} catch (Exception e)
		{
			stdout=e.getMessage();
		}
		if (stdout==null) stdout = sw.toString();
		actualDocs=dataContext.getActualData().toString(2);
	}

	//TODO IO Process Properties
	public void runMapScript(String scriptText, List<MapScriptIOItem> inputFields, List<MapScriptIOItem> outputFields)
	{
		Binding binding = new Binding();
		stdout="";
		CompilerConfiguration groovyCompilerConfiguration = new CompilerConfiguration();
		groovyCompilerConfiguration.setDebug(false); //true shows stack traces
		groovyCompilerConfiguration.setVerbose(false); //???What does this do?
		StringWriter sw=new StringWriter();
		binding.setProperty("out", sw);

		DataContextImpl dataContext=null ;//= new DataContextImpl(mockDocs);
		ExecutionUtilNonStatic ExecutionUtil = new ExecutionUtilNonStatic();
		ExecutionUtil.setDataContextImpl(dataContext);
		binding.setVariable("dataContext", dataContext);
		binding.setVariable("ExecutionUtil", ExecutionUtil);
		binding.setVariable("output", "");
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
		
		if (sbParseErrors.length() == 0)
		{
			for (MapScriptIOItem outputField:outputFields)
			{
				binding.setVariable(outputField.getFieldName(), "");
			}
			
			//for loop to setVariable output fields names
			GroovyShell shell = new GroovyShell(binding, groovyCompilerConfiguration);
			stdout=null;
			try {
				Script script = shell.parse(scriptText);
				script.setBinding(binding);
				script.run();			

				//for loop to getVariable output field values
				for (MapScriptIOItem outputField:outputFields)
				{
					outputField.setFieldValue(binding.getVariable(outputField.getFieldName()).toString());
				}
				System.out.println("output: " + binding.getVariable("output"));
			} catch (CompilationFailedException e)
			{
				stdout=e.getMessage();
			} catch (Exception e)
			{
				stdout=e.getMessage();
			}

			if (stdout==null) stdout = sw.toString();
		} else {
			stdout=sbParseErrors.toString();		
		}
		
	}

	public String getStdout() {
		return stdout;
	}

	public String getActualDocs() {
		return actualDocs;
	}
}
