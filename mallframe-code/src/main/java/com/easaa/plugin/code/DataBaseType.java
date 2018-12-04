package com.easaa.plugin.code;

public class DataBaseType {
	public static String getColumnType(String column){
		//System.out.println(column);
		if("INTEGER".equalsIgnoreCase(column)){
			return "Integer";
		}
		if("VARCHAR".equalsIgnoreCase(column)){
			return "String";
		}
		if("DATETIME".equalsIgnoreCase(column)){
			return "Date";
		}
		return "未知";
	}
	
	public static String getColumnResult(String column){
		String result="<result property=\"";
		String columnName=getColumnName(column);
		result=result+columnName+"\" column=\""+column+"\"/>";
		return result;
	}
	
	public static String getColumnName(String column){
		String[] ns=column.split("_");
		String tempStr="";
		String columnName="";
		if(ns.length>0){
			for(int i=0;i<ns.length;i++){
				tempStr=ns[i];
				tempStr=tempStr.substring(0, 1).toUpperCase() +tempStr.subSequence(1, tempStr.length());
				columnName+=tempStr;
			}
		}
		return columnName.substring(0, 1).toLowerCase() +columnName.subSequence(1, columnName.length());
	}
	
	public static String getClassBaseName(String tableName){
		tableName=tableName.replaceFirst("t_","");
		//tableName.replaceFirst(regex, replacement)
		String[] ns=tableName.split("_");
		String tempStr="";
		String columnName="";
		if(ns.length>0){
			for(int i=0;i<ns.length;i++){
				tempStr=ns[i];
				tempStr=tempStr.substring(0, 1).toUpperCase() +tempStr.subSequence(1, tempStr.length());
				columnName+=tempStr;
			}
		}
		return columnName.substring(0, 1).toUpperCase() +columnName.subSequence(1, columnName.length());
	}
	
}
