package com.manywho.services.atomsphere.actions.connectorTestMultiScript;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = "Connector Script Item")
public class ConnectorScriptItem implements Type{
	@Type.Identifier
	private String guid;
	@Type.Property(name = "Name", contentType = ContentType.String)
	private String name;
	@Type.Property(name = "Script Text", contentType = ContentType.String)
	private String scriptText;

	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScriptText() {
		return scriptText;
	}

	public void setScriptText(String scriptText) {
		this.scriptText = scriptText;
	}
}
