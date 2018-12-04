package com.easaa.entity;

import java.util.List;

public class TrackMsgEntity {
	
	public String EBusinessID;
	
	public String Success;
	
	public String LogisticCode;
	
	public String State;
	
	public String ShipperCode;
	
	public String ShipperName;
	
	
	public List<TrackEntity> Traces;


	public String getEBusinessID() {
		return EBusinessID;
	}


	public void setEBusinessID(String eBusinessID) {
		EBusinessID = eBusinessID;
	}


	public String getSuccess() {
		return Success;
	}


	public void setSuccess(String success) {
		Success = success;
	}


	public String getLogisticCode() {
		return LogisticCode;
	}


	public void setLogisticCode(String logisticCode) {
		LogisticCode = logisticCode;
	}


	public String getState() {
		return State;
	}


	public void setState(String state) {
		State = state;
	}


	public String getShipperCode() {
		return ShipperCode;
	}


	public void setShipperCode(String shipperCode) {
		ShipperCode = shipperCode;
	}


	public String getShipperName() {
		return ShipperName;
	}


	public void setShipperName(String shipperName) {
		ShipperName = shipperName;
	}


	public List<TrackEntity> getTraces() {
		return Traces;
	}


	public void setTraces(List<TrackEntity> traces) {
		Traces = traces;
	}
	
}
