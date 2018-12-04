package com.easaa.order.entity;

import java.math.BigDecimal;
import java.util.List;

public class BusinessOrder {
	
	//订单id
	private int orderId;
	//订单编号
	private String orderSn;
	//平台订单id
	private String platformOrderId;
	//商家id
	private String businessId;
	//用户id
	private String userId;
	//用户名字
	private String userName;
	//订单金额
	private BigDecimal orderMoney;
	//商品金额
	private BigDecimal goodsMoney;
	
	//快递费用  用户支付
	private BigDecimal shippingMoney;
	//快递费用  公司支付
	private BigDecimal shippingFee;
	//订单状态
	private String orderStatus;
	//货运状态
	private String shippingStatus;
	//货运类型
	private String shippingType;
	//支付状态
	private String payStatus;
	//支付类型
	private String payType;
	//支付金额
	private BigDecimal payByMoney;
	//下单时间
	private String createTime;
	//支付时间
	private String payTime;
	//联系人姓名
	private String contactName;
	//联系人电话
	private String contactPhone;
	
	private String provinceId;
	
	private String province;
	
	private String cityId;
	
	private String city;
	
	private String areaId;
	
	private String area;
	
	private String address;
	//订单商品
	private List<BusinessOrderGoods> orderGoods;
	
	private BigDecimal userPayPoints; 
	
	private BigDecimal givePoints; 
	
	public BigDecimal getGivePoints() {
		return givePoints;
	}

	public void setGivePoints(BigDecimal givePoints) {
		this.givePoints = givePoints;
	}

	public BigDecimal getUserPayPoints() {
		return userPayPoints;
	}

	public void setUserPayPoints(BigDecimal userPayPoints) {
		this.userPayPoints = userPayPoints;
	}

	
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public List<BusinessOrderGoods> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<BusinessOrderGoods> orderGoods) {
		this.orderGoods = orderGoods;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getPlatformOrderId() {
		return platformOrderId;
	}

	public void setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getGoodsMoney() {
		return goodsMoney;
	}

	public void setGoodsMoney(BigDecimal goodsMoney) {
		this.goodsMoney = goodsMoney;
	}

	public BigDecimal getShippingMoney() {
		return shippingMoney;
	}

	public void setShippingMoney(BigDecimal shippingMoney) {
		this.shippingMoney = shippingMoney;
	}

	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getPayByMoney() {
		return payByMoney;
	}

	public void setPayByMoney(BigDecimal payByMoney) {
		this.payByMoney = payByMoney;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}


	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}



	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}



	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
