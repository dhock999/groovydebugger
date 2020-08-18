package com.manywho.services.atomsphere.actions.groovymapscriptrunner;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = "Map Script Input/Output Item")
public class MapScriptIOItem implements Type{
	@Type.Identifier
	private String guid;
	@Type.Property(name = "Field Name", contentType = ContentType.String)
	private String fieldName;
	@Type.Property(name = "Field Type", contentType = ContentType.String)
	private String fieldType;
	@Type.Property(name = "Field Value", contentType = ContentType.String)
	private String fieldValue;

	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
}
