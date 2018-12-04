package com.easaa.business.entity;

import java.io.Serializable;

/**
 * 会员对象
 * @author liujunbo
 *
 */
public class Member  implements Serializable{

	private String userName;
	
	private String passWord;
	
	private String userId;
	
	private String phone;
	
	private String nikeName;
	
	private String registerTime;
	
	private String userMoney;
	
	private String userPoints;
	
	private String status;
	
	private Integer isRebate;
	
	private Integer isBuyer;
	
	private Integer isLoginer;
	
	private Integer isCommenter;
	
	private Integer userType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(String userMoney) {
		this.userMoney = userMoney;
	}

	public String getUserPoints() {
		return userPoints;
	}

	public void setUserPoints(String userPoints) {
		this.userPoints = userPoints;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsRebate() {
		return isRebate;
	}

	public void setIsRebate(Integer isRebate) {
		this.isRebate = isRebate;
	}

	public Integer getIsBuyer() {
		return isBuyer;
	}

	public void setIsBuyer(Integer isBuyer) {
		this.isBuyer = isBuyer;
	}

	public Integer getIsLoginer() {
		return isLoginer;
	}

	public void setIsLoginer(Integer isLoginer) {
		this.isLoginer = isLoginer;
	}

	public Integer getIsCommenter() {
		return isCommenter;
	}

	public void setIsCommenter(Integer isCommenter) {
		this.isCommenter = isCommenter;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	
	
	
}
