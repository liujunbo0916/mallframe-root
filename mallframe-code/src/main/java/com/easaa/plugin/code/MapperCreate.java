package com.easaa.plugin.code;

import com.easaa.core.util.EAUtil;

public class MapperCreate {

	private static final String LINE = "\r\n";
	private static final String TAB = "\t";
	StringBuffer sb = new StringBuffer();
	private TableModel table;

	public MapperCreate(TableModel tableModel){
		this.table = tableModel;
		tableName=tableModel.getTableName();
	}
	private String tableName = "";
	public String doCreate() throws Exception {
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sb.append(LINE);
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		sb.append(LINE);
		// 添加命名空间部分
		appendNamespace();
		sb.append(LINE);
		appendCondition();
		appendSelectByPage();
		appendSelectByMap();
		appendSelectById();
		appendInsert();
		appendUpdate();
		appendDelete();
		sb.append(LINE);
		sb.append("</mapper>");
		System.out.println(sb.toString());
		return sb.toString();
	}

	private void appendSelectById() throws Exception {
		sb.append(LINE);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<select id=\"selectById\" resultType=\"pd\"  parameterType=\"Integer\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("SELECT * from " + this.tableName + " where "
				+ this.table.getCloumns().get(0).getColumnName() + " = #{id}");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("</select>");
	}

	private void appendDelete() throws Exception {
		sb.append(LINE);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<delete id=\"delete\"   parameterType=\"pd\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("delete from " + this.tableName + " where 1=1").append(LINE).append(TAB).append(TAB).append(TAB);
		sb.append("<include refid=\"condition\"></include>").append(LINE);
		sb.append(TAB);
		sb.append("</delete>");
	}

	private void appendUpdate() throws Exception {
		sb.append(LINE);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<update id=\"update\" parameterType=\"pd\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("update " + this.tableName + " <set>");
		String cName = "";
		for (int i = 1; i < table.getCloumns().size(); i++) {
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			cName = table.getCloumns().get(i).getColumnName();
			sb.append("<if test=\"" + cName + " != null\">");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append(TAB);
			sb.append(cName + " = #{" + cName + "},");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("</if>");
		}
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("</set>");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("where " + table.getCloumns().get(0).getColumnName() + " = #{"
				+ table.getCloumns().get(0).getFiledName() + "}");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("</update>");
	}

	private void appendInsert() throws Exception {
		sb.append(LINE);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<insert id=\"insert\" parameterType=\"pd\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("insert into " + this.tableName + " (");
		String columns = "";
		String attribues = "";
		String cName = "";
		for (int i = 1; i < table.getCloumns().size(); i++) {
			cName = table.getCloumns().get(i).getColumnName();
			if (!"".equals(columns)) {
				columns += ",";
				attribues += ",";
			}
			columns += cName;
			if ("create_date".equals(cName)) {
				attribues += "NOW()";
			} else {
				attribues = attribues + "#{" + cName + "}";
			}
		}
		sb.append(columns + ")");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("values (" + attribues + ")");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("</insert>");
	}

	
	private void appendSelectByPage() throws Exception {
		sb.append(LINE);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<select id=\"selectByPage\" resultType=\"pd\"  parameterType=\"page\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("select * from " + this.tableName + " where 1=1").append(LINE).append(TAB).append(TAB).append(TAB);
		sb.append("<include refid=\"condition\"></include>").append(LINE);
		sb.append(TAB);
		sb.append("</select>");
	}
	
	private void appendSelectByMap() throws Exception {
		sb.append(LINE);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<select id=\"selectByMap\" resultType=\"pd\"  parameterType=\"pd\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("select * from " + this.tableName + " where 1=1").append(LINE).append(TAB).append(TAB).append(TAB);
		sb.append("<include refid=\"condition\"></include>").append(LINE);
		sb.append(TAB);
		sb.append("</select>");
	}

	private void appendCondition() throws Exception {
		sb.append(TAB);
		sb.append("<sql id=\"condition\">");
		String cName = "";
		for (int i = 0; i < table.getCloumns().size(); i++) {
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			if(EAUtil.isEmpty(this.table.getCloumns().get(i))){
				break;
			}
			cName = table.getCloumns().get(i).getColumnName();
			sb.append("<if test=\"pd." + cName + " != null\">");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("and " + cName + " = #{pd." + cName + "}");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("</if>");
		}
		sb.append(LINE);
		sb.append(TAB);
		sb.append("</sql>");
	}
	
	

	private void appendNamespace() {
		sb.append("<mapper namespace=\"com.easaa."+table.getModuleName()+".dao."+table.getBaseName()+"Mapper\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<cache type=\"org.mybatis.caches.ehcache.EhcacheCache\"/>");
		sb.append(LINE);
		sb.append(TAB);
	}
}
