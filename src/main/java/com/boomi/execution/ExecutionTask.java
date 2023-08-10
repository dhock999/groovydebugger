package com.boomi.execution;

import java.util.Properties;

public class ExecutionTask {
	  private ExecutionManager manager;
//	  private Set<String> dirtyProperties = []
	  public ExecutionTask(ExecutionManager manager) {
	    this.manager = manager;
	  }
	  public Properties getProperties() {
	    return this.manager._props;
	  }

	  public String getProperty(String name, String defaultValue) {
	    String value = this.getProperty(name);
	    return !(value==null || value.isEmpty()) ? value : defaultValue;
	  }

	  public String getProperty(String name) {
	    String value = this.manager._props.getProperty(name);

	    return value;
	  }
	  
	  public ExecutionTask getParent()
	  {
		  return null;
	  }
	  
	  public String getProcessId()
	  {
		 return "PROCESSID"; 
	  }

	  public String getExecutionId()
	  {
		 return "EXECUTIONID"; 
	  }

//	  public Property getDefinedProperty(String componentId, String propId) {
//	    return this.manager._props.get(buildPropertyName(componentId, propId));
//	  }
	  
	  private static String buildPropertyName(String componentId, String propertyKey) {
	    return componentId + propertyKey;
	  }

	  public void setProperty(String name, String value) {
	    this.setProperty(name, value, false);
	  }

	  public void setProperty(String name, String value, boolean persist) {
	    if (value != null) {
//	      dirtyProperties.add(name)
	      this.manager._props.setProperty(name, value);
	    }
	  }

//	  public void dumpDirtyProperties(PrintStream out) {
//	    def props = this.getProperties()
//	    def clone = new Properties()
//	    dirtyProperties.each {
//	      clone.setProperty(it, props.getProperty(it))
//	    }
//	    out.println('\nChanged Process Properties:')
//	    if (clone.size() == 0) return
//
//	    clone.each { Map.Entry<String, String> it ->
//	      def val = it.value
//	      if (val.length() > 500) {
//	        val = val.substring(0, 500) + "...";
//	      }
//	      out.println(it.key + "=" + val)
//	    }
//	  }
}
