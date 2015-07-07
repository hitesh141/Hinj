/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */

package com.hinj.app.utils;
/**
 * @author jitendrav
 *
 */
public class AField {
	private String fieldNameStr;
	private String fieldData;
	private FieldType fieldType;
	
	public enum FieldType {
    NULL,
    INTEGER,
    REAL,
    TEXT,
    BLOB,
    UNRESOLVED
  }
	
  public String toString() {
  	return "Type = " + fieldType + " Data = " + fieldData;
  }
  
	public String getFieldData() {
		return fieldData;
	}
	public void setFieldData(String fieldData) {
		this.fieldData = fieldData;
	}
	public FieldType getFieldType() {
		return fieldType;
	}
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getFieldNameStr() {
		return fieldNameStr;
	}
	public void setFieldNameStr(String fieldNameStr) {
		this.fieldNameStr = fieldNameStr;
	}
	
}
