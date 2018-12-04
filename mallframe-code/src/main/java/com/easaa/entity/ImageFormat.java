package com.easaa.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 图片规格
 * @author liujunbo
 *
 */
public class ImageFormat  implements Serializable{

	private static final long serialVersionUID = 1L;
	//图片宽
	private double width;
	//图片高
	private double height;
	//图片名称标识
	private String flag;
	//图片名称
	private String fileName;
	//后缀名
	private String suffix;
	//保存剪切之后的图片对象
	private File file;
	
	public ImageFormat(double width, double height, String flag) {
		super();
		this.width = width;
		this.height = height;
		this.flag = flag;
	}
	public ImageFormat(double width, double height, String flag,String fileName) {
		super();
		this.width = width;
		this.height = height;
		this.flag = flag;
		this.fileName = fileName;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
