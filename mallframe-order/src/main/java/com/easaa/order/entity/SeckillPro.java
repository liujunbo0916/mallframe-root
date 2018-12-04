package com.easaa.order.entity;

import org.apache.commons.lang.StringUtils;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

public class SeckillPro {
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	
	private String spId;
	
	private String goodsName;
	
	private String skuId;
	
	private String goodsPrice;
	
	private String discountPrice;
	
	private String skId;
	
	private String goodsId;
	private String bsId;
	
	private String killStock;
	
	private String killStatus;
	
	private String createTime;

	private String goodsImg;
	
	private String goodsAttr;
	
	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getSkId() {
		return skId;
	}

	public void setSkId(String skId) {
		this.skId = skId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getKillStock() {
		return killStock;
	}

	public void setKillStock(String killStock) {
		this.killStock = killStock;
	}

	public String getKillStatus() {
		return killStatus;
	}

	public void setKillStatus(String killStatus) {
		this.killStatus = killStatus;
	}

	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		if(StringUtils.isNotEmpty(goodsImg) && !goodsImg.startsWith("http")){
			goodsImg = PROPERTIESHELPER.getValue("imageShowPath")+goodsImg;
		}
		this.goodsImg = goodsImg;
	}
	public String getGoodsAttr() {
		return goodsAttr;
	}
	public void setGoodsAttr(String goodsAttr) {
		this.goodsAttr = goodsAttr;
	}

	public String getBsId() {
		return bsId;
	}
	

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}
	
}
