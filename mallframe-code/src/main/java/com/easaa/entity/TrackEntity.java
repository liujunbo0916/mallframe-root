package com.easaa.entity;

public class TrackEntity {
	
	public String AcceptTime;
	
	public String AcceptStation;
	
	public String Remark;

	public String getAcceptTime() {
		return AcceptTime;
	}

	public TrackEntity(){}
	
	public TrackEntity(String acceptTime, String acceptStation, String remark) {
		super();
		AcceptTime = acceptTime;
		AcceptStation = acceptStation;
		Remark = remark;
	}

	public String getAcceptStation() {
		return AcceptStation;
	}

	public void setAcceptStation(String acceptStation) {
		AcceptStation = acceptStation;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public void setAcceptTime(String acceptTime) {
		AcceptTime = acceptTime;
	}
	
	
}
