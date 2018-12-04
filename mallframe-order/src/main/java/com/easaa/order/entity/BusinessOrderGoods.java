package com.easaa.order.entity;

import org.apache.commons.lang.StringUtils;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

public class BusinessOrderGoods {

	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	private String id;
	//商户id
	private String borderId;
	//商品id
	private String goodsId;
	//商品名字
	private String goodsName;
	//商品图像
	private String listImg;
	//商品数量
	private String goodsCount;
	//商品价格
	private String goodsPrice;
	//商品属性
	private String goodsAttr;
	//商品库存
	private String skuId;
	//是否礼物
	private boolean isGift;
	
	public String getListImg() {
		return listImg;
	}
	public void setListImg(String listImg) {
		if(StringUtils.isNotEmpty(listImg) && !listImg.startsWith("http")){
			listImg = PROPERTIESHELPER.getValue("imageShowPath")+listImg;
		}
		this.listImg = listImg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBorderId() {
		return borderId;
	}
	public void setBorderId(String borderId) {
		this.borderId = borderId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsAttr() {
		return goodsAttr;
	}
	public void setGoodsAttr(String goodsAttr) {
		this.goodsAttr = goodsAttr;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public boolean isGift() {
		return isGift;
	}
	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}
}
