package com.boomi.execution;

import java.util.Properties;

public class ExecutionManager {
	static ExecutionManager singleton = new ExecutionManager();
	static ExecutionTask singletonTask = new ExecutionTask(singleton);
	Properties _props = new Properties();

	public Properties getProperties() {
		return _props;
	}

	public static ExecutionTask getCurrent() {
	    return singletonTask;
	}
}
