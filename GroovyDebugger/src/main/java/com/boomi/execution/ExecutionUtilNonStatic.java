package com.boomi.execution;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class ExecutionUtilNonStatic {

	DataContextImpl _dataContext;
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
//		CustomStreamHandler streamHandler = new CustomStreamHandler(System.out, new SimpleFormatter());
//		logger.addHandler(streamHandler);
//		StreamHandler handler = new StreamHandler();
		return logger;
	}
	
	void setDataContextImpl(DataContextImpl dataContext)
	{
		_dataContext = dataContext;
	}
}
