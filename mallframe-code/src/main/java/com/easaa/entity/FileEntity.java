package com.easaa.entity;

import java.util.List;

/**
 * ftp文件对象
 * 
 * 
 * @author liujunbo
 *
 */
public class FileEntity {
	
	
	private String fileName;
	
	private String showFileName;
	
	
	private String createTime;
	
	private String filePath;
	
	private List<FileEntity> fileEntitys;
	
	private boolean hasMenu = true;

	private boolean isFile = false;
	
	private String target;
	
	private String MENU_URL = "javascript:requestNextChild();";
	
	public  FileEntity(){}
	
	public FileEntity(String fileName, String createTime, String filePath, List<FileEntity> fileEntitys,boolean isFile) {
		super();
		this.fileName = fileName;
		this.createTime = createTime;
		this.filePath = filePath;
		this.fileEntitys = fileEntitys;
		this.target = fileName+";"+filePath+";"+createTime;
		this.showFileName = fileName+"&nbsp;&nbsp;（"+this.createTime+"）";
		this.isFile = isFile;
	}
	public String getMENU_URL() {
		return MENU_URL;
	}
	
	

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public void setMENU_URL(String mENU_URL) {
		MENU_URL = mENU_URL;
	}

	public String getFileName() {
		return fileName;
	}

	
	
	public String getShowFileName() {
		return showFileName;
	}

	public void setShowFileName(String showFileName) {
		this.showFileName = showFileName;
	}

	public boolean isHasMenu() {
		return hasMenu;
	}

	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.target = this.fileName+";"+filePath+";"+this.createTime;
		this.filePath = filePath;
	}

	public List<FileEntity> getFileEntitys() {
		return fileEntitys;
	}

	public void setFileEntitys(List<FileEntity> fileEntitys) {
		this.fileEntitys = fileEntitys;
	}
	
	
	
	

}
