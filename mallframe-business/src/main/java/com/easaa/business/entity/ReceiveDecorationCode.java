package com.easaa.business.entity;

import java.util.List;

public class ReceiveDecorationCode {
	
	public String decoration_id;
	public String decoration_name;
	public String decoration_setting;
	public String decoration_code;
	
	public List<ReceiveDecorationModel> model;

	public String getDecoration_id() {
		return decoration_id;
	}

	public void setDecoration_id(String decoration_id) {
		this.decoration_id = decoration_id;
	}

	public String getDecoration_name() {
		return decoration_name;
	}

	public void setDecoration_name(String decoration_name) {
		this.decoration_name = decoration_name;
	}

	public String getDecoration_setting() {
		return decoration_setting;
	}

	public void setDecoration_setting(String decoration_setting) {
		this.decoration_setting = decoration_setting;
	}

	public String getDecoration_code() {
		return decoration_code;
	}

	public void setDecoration_code(String decoration_code) {
		this.decoration_code = decoration_code;
	}

	public List<ReceiveDecorationModel> getModel() {
		return model;
	}

	public void setModel(List<ReceiveDecorationModel> model) {
		this.model = model;
	}
	
}

