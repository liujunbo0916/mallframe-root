package com.easaa.entity;

import java.io.Serializable;
/**
 * 消息实体
 * @author ryy
 *
 */
public class SendMsg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 发给谁
	 */
	public String[] userIds;
	/**
	 * 内容
	 */
	public String content;
	/**
	 * 类型 （1、只推送 2 、只发短信 3、推送短信都发）
	 */
	public Integer type;
	/**
	 * 短信电话
	 */
	public String phone;
	
	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
