package com.easaa.plugin.code;

public class CodeConfigModel {
	private String moduleTitle="";
	private String dbName="";
	private String tableName="";
	private boolean isController=true;
	private boolean isService=true;
	private boolean isModel=true;
	private boolean isDao=true;
	private boolean isSql=true;
	private boolean isListJsp=true;
	private boolean isEditJsp=true;
	private TableModel tableModel=null;
	private String baoQianZhui;
	private String projectRootPath;
	public String getPojoName(boolean isHasPackage){
		if(isHasPackage){
			return "com."+this.getBaoQianZhui()+".pojo."+this.getTableModel().getBaseName();
		}
		return this.getTableModel().getBaseName();
	}
	public String getDaoName(boolean isHasPackage){
		if(isHasPackage){
			return "com."+this.getBaoQianZhui()+".dao."+this.getTableModel().getBaseName()+"Mapper";
		}
		return this.getTableModel().getBaseName()+"Mapper";
	}
	
	public String getServiceName(boolean isHasPackage){
		if(isHasPackage){
			return "com."+this.getBaoQianZhui()+".service."+this.getTableModel().getBaseName()+"Service";
		}
		return this.getTableModel().getBaseName()+"Service";
	}
	public String getControllerName(boolean isHasPackage){
		if(isHasPackage){
			return "com."+this.getBaoQianZhui()+".controller."+this.getTableModel().getBaseName()+"Controller";
		}
		return this.getTableModel().getBaseName()+"Controller";
	}
	/**
	 * 获取包路径
	 * @param type 1:控制层;2:服务层;3:数据层;4bean;5:Sql
	 * @return
	 */
	public String getPackageName(int type){
		if(type==1){
			return "com."+this.getBaoQianZhui()+".sys.controller";
		}
		if(type==2){
			return "com."+this.getBaoQianZhui()+".service";
		}
		if(type==3){
			return "com."+this.getBaoQianZhui()+".dao";
		}
		if(type==4){
			return "com."+this.getBaoQianZhui()+".pojo";
		}
		if(type==5){
			return "com."+this.getBaoQianZhui()+".mapper";
		}
		return "";
	}
	/**
	 * 获取包路径
	 * @param type 1:控制层;2:服务层;3:数据层;4bean;5:Sql;6:编辑页面;7:列表界面
	 * @return
	 */
	public String getFilePath(int type){
		if(type==1){
			return getProjectRootPath()+"src/com/"+getBaoQianZhui()+"/controller/sys/"+getControllerName(false)+".java";
		}
		if(type==2){
			return getProjectRootPath()+"src/com/"+getBaoQianZhui()+"/service/"+getServiceName(false)+".java";
		}
		if(type==3){
			return getProjectRootPath()+"src/com/"+getBaoQianZhui()+"/dao/"+getDaoName(false)+".java";
		}
		if(type==4){
			return getProjectRootPath()+"src/com/"+getBaoQianZhui()+"/pojo/"+getPojoName(false)+".java";
		}
		if(type==5){
			return getProjectRootPath()+"src/com/"+getBaoQianZhui()+"/mapper/"+getTableModel().getBaseName()+"Mapper.xml";
		}
		if(type==6){
			return getProjectRootPath()+"WebRoot/views/sys/"+getTableModel().getLowBaseName()+"/edit.jsp";
		}
		if(type==7){
			return getProjectRootPath()+"WebRoot/views/sys/"+getTableModel().getLowBaseName()+"/list.jsp";
		}
		return "";
	}
	
	public boolean isController() {
		return isController;
	}
	public void setController(boolean isController) {
		this.isController = isController;
	}
	public boolean isService() {
		return isService;
	}
	public void setService(boolean isService) {
		this.isService = isService;
	}
	public boolean isModel() {
		return isModel;
	}
	public void setModel(boolean isModel) {
		this.isModel = isModel;
	}
	public boolean isDao() {
		return isDao;
	}
	public void setDao(boolean isDao) {
		this.isDao = isDao;
	}
	public boolean isSql() {
		return isSql;
	}
	public void setSql(boolean isSql) {
		this.isSql = isSql;
	}
	public boolean isListJsp() {
		return isListJsp;
	}
	public void setListJsp(boolean isListJsp) {
		this.isListJsp = isListJsp;
	}
	public boolean isEditJsp() {
		return isEditJsp;
	}
	public void setEditJsp(boolean isEditJsp) {
		this.isEditJsp = isEditJsp;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public TableModel getTableModel() {
		return tableModel;
	}
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	public String getProjectRootPath() {
		return projectRootPath;
	}
	public void setProjectRootPath(String projectRootPath) {
		this.projectRootPath = projectRootPath;
	}
	/**
	 * com.buhe.model 中的 buhe部分
	 * @return
	 */
	public String getBaoQianZhui() {
		return baoQianZhui;
	}
	public void setBaoQianZhui(String baoQianZhui) {
		this.baoQianZhui = baoQianZhui;
	}
	public String getModuleTitle() {
		return moduleTitle;
	}
	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}
}
