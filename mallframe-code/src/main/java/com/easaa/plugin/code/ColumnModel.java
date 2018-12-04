package com.easaa.plugin.code;

public class ColumnModel {
	private String columnName="";
	private String filedName="";
	private String filedType="";
	private boolean notNull=false;
	private int type=0;
	private String comment="";
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
		this.filedName = columnName;//DataBaseType.getColumnName(columnName);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFiledName() {
		return filedName;
	}
	public String getFiledType() {
		return filedType;
	}
	public void setFiledType(String filedType) {
		this.filedType = filedType;
	}
	public boolean isNotNull() {
		return notNull;
	}
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	
}
