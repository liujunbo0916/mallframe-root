package com.easaa.order.entity;

import org.apache.commons.lang.StringUtils;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

/**
 * 秒杀活动
 * 
 * 
 * @author liujunbo
 *
 */
public class Seckill {
	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	
	private String skId;
	//活动logo
	private String skLogo;
	//秒杀名字
	private String skName;
	//秒杀开始时间
	private String skStartTime;
	//秒杀结束时间
	private String skEndTime;
	//秒杀限制商品个数
	private String skLimitB;
	//秒杀描述
	private String skDesc;
	public String getSkId() {
		return skId;
	}
	public void setSkId(String skId) {
		this.skId = skId;
	}
	public String getSkLogo() {
		return skLogo;
	}
	public void setSkLogo(String skLogo) {
		if(StringUtils.isNotEmpty(skLogo) && !skLogo.startsWith("http")){
			skLogo = PROPERTIESHELPER.getValue("imageShowPath")+skLogo;
		}
		this.skLogo = skLogo;
	}
	public String getSkName() {
		return skName;
	}
	public void setSkName(String skName) {
		this.skName = skName;
	}
	public String getSkStartTime() {
		return skStartTime;
	}
	public void setSkStartTime(String skStartTime) {
		this.skStartTime = skStartTime;
	}
	public String getSkEndTime() {
		return skEndTime;
	}
	public void setSkEndTime(String skEndTime) {
		this.skEndTime = skEndTime;
	}
	public String getSkLimitB() {
		return skLimitB;
	}
	public void setSkLimitB(String skLimitB) {
		this.skLimitB = skLimitB;
	}
	public String getSkDesc() {
		return skDesc;
	}
	public void setSkDesc(String skDesc) {
		this.skDesc = skDesc;
	}
	
}
