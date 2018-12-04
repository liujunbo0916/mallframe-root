package com.easaa.order.entity;

import java.util.List;

public class Compo {
	
	
	private String compoId;
	
	private String bsId;
	
	private String compoName;
	
	private String compoStartTime;
	
	private String compoEndTime;
	
	private String compoLimitNum;//限购数量
	
	private String compoPrice;//价格
	
	private String compoPostage;//运费
	
	private String compoSales;
	
	private List<CompoPro> pros; //活动商品
	
	public String getCompoId() {
		return compoId;
	}

	public void setCompoId(String compoId) {
		this.compoId = compoId;
	}

	public String getCompoName() {
		return compoName;
	}

	public void setCompoName(String compoName) {
		this.compoName = compoName;
	}


	public String getCompoStartTime() {
		return compoStartTime;
	}


	public void setCompoStartTime(String compoStartTime) {
		this.compoStartTime = compoStartTime;
	}


	public String getCompoEndTime() {
		return compoEndTime;
	}


	public void setCompoEndTime(String compoEndTime) {
		this.compoEndTime = compoEndTime;
	}


	public String getCompoLimitNum() {
		return compoLimitNum;
	}


	public void setCompoLimitNum(String compoLimitNum) {
		this.compoLimitNum = compoLimitNum;
	}


	public String getCompoPrice() {
		return compoPrice;
	}


	public void setCompoPrice(String compoPrice) {
		this.compoPrice = compoPrice;
	}


	public String getCompoPostage() {
		return compoPostage;
	}


	public void setCompoPostage(String compoPostage) {
		this.compoPostage = compoPostage;
	}


	public String getCompoSales() {
		return compoSales;
	}


	public void setCompoSales(String compoSales) {
		this.compoSales = compoSales;
	}


	public String getBsId() {
		return bsId;
	}


	public void setBsId(String bsId) {
		this.bsId = bsId;
	}


	public List<CompoPro> getPros() {
		return pros;
	}


	public void setPros(List<CompoPro> pros) {
		this.pros = pros;
	}
	
	
}
