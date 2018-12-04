package com.easaa.business.entity;

import java.io.Serializable;
import java.util.Date;

import com.easaa.core.util.EADate;
import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

/**
 * 商家对象
 * @author liujunbo
 *
 */
public class Seller  implements Serializable{
	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	
	private Integer sellerId;
	
	private String userName;
	
	private String passWord;
	/**
	 * 保存会员对象
	 */
	private Member member;
	/**
	 * 保存店铺对象
	 */
	private Business business;
	
	//最后操作时间
	private Date optionTime; 
	//登陆状态  是否失效
	private boolean loginState; 
	
	//用户最后访问的一个链接
	private String lastVisitPath;
	
	
	
	
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	//其余业务字段
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
	public Date getOptionTime() {
		return optionTime;
	}
	public void setOptionTime(Date currentTime) {
		if(optionTime != null){
			System.out.println(EADate.getMillis(optionTime));
			System.out.println(EADate.getMillis(currentTime));
			long intervalTime = EADate.getMillis(currentTime) - EADate.getMillis(optionTime);
			System.out.println(intervalTime);
			if(intervalTime > Integer.parseInt(PROPERTIESHELPER.getValue("login_timeout"))*1000){
				loginState = true;
			}else{
				loginState = false;
			}
		}else{
			loginState = false;
		}
		this.optionTime = currentTime;
	}
	public boolean isLoginState() {
		return loginState;
	}
	public void setLoginState(boolean loginState) {
		this.loginState = loginState;
	}
	public String getLastVisitPath() {
		return lastVisitPath;
	}
	public void setLastVisitPath(String lastVisitPath) {
		this.lastVisitPath = lastVisitPath;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
}
