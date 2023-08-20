package com.boomi.connector.groovyconnector;

import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.api.listen.SingletonListenOperation;
import com.boomi.connector.util.listen.UnmanagedListenOperation;
import com.boomi.util.StringUtil;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyListenOperation extends UnmanagedListenOperation implements SingletonListenOperation<ListenManager>{

	Binding _binding;
	GroovyShell _shell;
	Resources resources = new Resources();
    public GroovyListenOperation(OperationContext context) {
        super(context);
		 _shell = GroovyScriptHelpers.getShell();
	     _binding = new Binding();
	     _binding.setVariable("context", this.getContext());
    }

	@Override
	public void stop() {
		String scriptName = "stopListener.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("resources", resources);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for stopListener");
        }
	}

	@Override
	protected void start(Listener listener) {
		String scriptName = "startListener.groovy";
	    String scriptText = GroovyScriptHelpers.getScript(scriptName, this.getContext().getConnectionProperties(), this.getClass());
        if (StringUtil.isNotBlank(scriptText))
        {
            _binding.setVariable("resources", resources);
            _binding.setVariable("listener", listener);
            GroovyScriptHelpers.runScript(_shell, _binding, scriptName, scriptText);
        } else {
        	throw new ConnectorException("A script is required for startListener");
        }
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	class Resources {
		public Object resource1;
		public Object resource2;
	}
}
