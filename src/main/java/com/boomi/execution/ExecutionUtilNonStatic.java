package com.boomi.execution;

import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class ExecutionUtilNonStatic {
	StringWriter stdOut;
	DataContextImpl _dataContext;
	ExecutionUtilNonStatic (StringWriter stdOut)
	{
		this.stdOut=stdOut;
	}
	String getDynamicProcessProperty(String propName)
	{
		return _dataContext.getDynamicProcessProperty(propName);
	}

	void setDynamicProcessProperty(String propName, String propValue, boolean persist)
	{
		_dataContext.setDynamicProcessProperty(propName, propValue);
	}
	
	Logger getBaseLogger()
	{
		Logger logger = Logger.getLogger("Groovy");
		logger.addHandler(new StdOutLoggerHandler(stdOut));
		return logger;
	}
	
	void setDataContextImpl(DataContextImpl dataContext)
	{
		_dataContext = dataContext;
	}
	
	public String getAccountId()
	{
		return "ACCOUNTID";
	}
	
	public String getContainerId()
	{
		return "ATOMID";
	}
	
	//com.boomi.util.Counter
	public Object getCounter(String counterName, int batchSize)
	{
		return null;
	}
	
	public String getDirectory() 
	{
		return "work";
	}
	
	public String getExecutionProperty(String key)
	{
		return "";
	}
	
	public static String getProcessProperty(String componentId, String propertyKey)
	{
		return "";
	}
	
	public void setExecutionProperty(String key, String value)
	{
		
	}
	
	public  void setProcessProperty(String componentId, String propertyKey, String value)
	{
		
	}
}
