package com.manywho.services.atomsphere.actions.groovyprocessscriptrunner;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = "Process Script Property Item")
public class PropertyItem implements Type{
	@Type.Identifier
	private String guid;
	@Type.Property(name = "Property Name", contentType = ContentType.String)
	private String name;
	@Type.Property(name = "Property Value", contentType = ContentType.String)
	private String value;

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

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
