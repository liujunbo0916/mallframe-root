package com.easaa.order.entity;

import org.apache.commons.lang.StringUtils;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

public class CompoPro {
	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);	
	
	private String compoId;
	
	private String proId;
	
	private String goodsName;
	
	private String skuId;
	
	private String goodsId;
	
	private String attrJson;
	
	private String goodsPrice;
	
	private String goodsSales;
	
	private String stock;
	
	private String goodsImg;
	
	private String goodsBsId;
	
	private String goodsFreight;
	
	public String getGoodsFreight() {
		return goodsFreight;
	}

	public void setGoodsFreight(String goodsFreight) {
		this.goodsFreight = goodsFreight;
	}

	public String getGoodsBsId() {
		return goodsBsId;
	}

	public void setGoodsBsId(String goodsBsId) {
		this.goodsBsId = goodsBsId;
	}

	public String getCompoId() {
		return compoId;
	}

	public void setCompoId(String compoId) {
		this.compoId = compoId;
	}

	public String getAttrJson() {
		return attrJson;
	}

	public void setAttrJson(String attrJson) {
		this.attrJson = attrJson;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
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

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsSales() {
		return goodsSales;
	}

	public void setGoodsSales(String goodsSales) {
		this.goodsSales = goodsSales;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		if(StringUtils.isNotEmpty(goodsImg) && !goodsImg.startsWith("http")){
			goodsImg =  PROPERTIESHELPER.getValue("imageShowPath") + goodsImg;
		}
		this.goodsImg = goodsImg;
	}
}
