package com.easaa.plugin.code;

import java.util.ArrayList;
import java.util.List;

public class TableModel {
	public static String Mapper="Mapper";
	public static String Controller="Controller";
	public static String Service="Service";
	
	
	
	public List<ColumnModel> cloumns=new ArrayList<ColumnModel>();
	private String pk="";
	private String tableName="";
	private String lowBaseName="";
	private String baseName="";
	private String moduleName="";
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public TableModel(String createSql){
		String tempStr=createSql;
		String[] array = tempStr.split("\n");
		for (int i = 1; i < array.length - 1; i++) {
			tempStr = array[i];
			tempStr = tempStr.replaceAll("'", "");
			tempStr = tempStr.replaceAll("`", "");
			tempStr = tempStr.replaceAll("\\,", "");
			tempStr = tempStr.trim();
			if(tempStr.indexOf("PRIMARY")>-1){
				String pk = null;
				int index = tempStr.indexOf("PRIMARY KEY (");
				if (index > -1) {
					pk = tempStr.substring(index + "PRIMARY KEY (".length());
					pk = pk.substring(0, pk.length()-1);
				}
				setPk(DataBaseType.getColumnName(pk));
				continue;
			}
			ColumnModel model = parse(tempStr);
			cloumns.add(model);
		}
	}
	
	public ColumnModel parse(String str) {
		ColumnModel model = new ColumnModel();
		String[] columns = str.split(" ");
		model.setColumnName(columns[0]);
		if("kEY".equalsIgnoreCase(columns[0])){
			return null;
		}
		System.out.println(columns[1]);
		if (columns[1].indexOf("datetime") > -1) {
			model.setFiledType("Date");
		} else if (columns[1].indexOf("int") > -1 ) {
			model.setFiledType("Integer");
		} else if(columns[1].indexOf("decimal")>-1){
			model.setFiledType("double");
		}else if(columns[1].indexOf("tinyint")>-1){
			model.setFiledType("Integer");
		}else if(columns[1].indexOf("smallint")>-1){
			model.setFiledType("Integer");
		}else if(columns[1].indexOf("mediumint")>-1){
			model.setFiledType("Integer");
		}else {
			model.setFiledType("String");
		}
		model.setNotNull(str.indexOf("NOT NULL") > -1);

		String comment = null;
		int index = str.indexOf("COMMENT");
		if (index > -1) {
			comment = str.substring(index + 8);
			comment = comment.substring(0, comment.length());
		}
		model.setComment(comment);
		return model;
	}
	
	public List<ColumnModel> getCloumns() {
		return cloumns;
	}
	public void setCloumns(List<ColumnModel> cloumns) {
		this.cloumns = cloumns;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.baseName=DataBaseType.getClassBaseName(tableName);
		this.lowBaseName=getFirstLowerCaseString(baseName);
	}
	public String getBaseName() {
		return baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public String getLowBaseName() {
		return lowBaseName;
	}
	public String getFirstLowerCaseString(String sourceString){
		return sourceString.substring(0, 1).toLowerCase() +sourceString.subSequence(1, sourceString.length());
	}
}
