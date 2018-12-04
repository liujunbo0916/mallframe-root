package com.easaa.business.entity;

import java.io.Serializable;
import java.util.List;

public class ReceiveDecorationModel implements Serializable{
	
	public String m_id;
	public String t_id;
	public String m_high;
	public String m_width;
	public String m_code;
	
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getM_high() {
		return m_high;
	}
	public void setM_high(String m_high) {
		this.m_high = m_high;
	}
	public String getM_width() {
		return m_width;
	}
	public void setM_width(String m_width) {
		this.m_width = m_width;
	}
	public String getM_code() {
		return m_code;
	}
	public void setM_code(String m_code) {
		this.m_code = m_code;
	}
	public String getT_id() {
		return t_id;
	}
	public void setT_id(String t_id) {
		this.t_id = t_id;
	}
	
	
}


