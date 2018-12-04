package com.easaa.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.easaa.core.util.EAString;

/**
 * 库存数据对象
 * 
 * @author Administrator
 *
 */
public class Stock implements Serializable {

	private String sku_id;

	private String goods_id;

	private String price;

	/**
	 * 成本价格
	 */
	private String cost_price;
	
	/**
	 * 市场价格
	 */
	private String market_price;
	
	/**
	 * 图片集 逗号分割
	 */
	private String img_ary;
	
	/**
	 * 条形码（商品编码）
	 */
	private String bar_code;
	
	/**
	 * 物流重量 
	 */
	private String logistics_weight;
	
	/**
	 * 商品名字
	 */
	private String goods_name;
	
	
	/**
	 * 
	 * 商品图片
	 */
	private String list_img;
	
	/**
	 * 购买数量
	 */
	private String buyNum;
	
	/**
	 * 是否免运费  0不免  1免
	 */
	private String is_free_shipping;
	
	/**
	 * 运费
	 */
	private BigDecimal goods_freight; 
	
	private String stockType="0"; //0 普通 1限时折扣

	private String endTime;//倒计时毫秒
	
	private String minNum;//购买下限(限时折扣用)
	
	private String proId;
	
	private String stock;

	private String attr_json;
	
	public Stock() {}
	
	public Stock(String sku_id, String goods_id, String price, String stock, String attr_json) {
		super();
		this.sku_id = sku_id;
		this.goods_id = goods_id;
		this.price = price;
		this.stock = stock;
		this.attr_json = attr_json;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getAttr_json() {
		return attr_json;
	}

	public void setAttr_json(String attr_json) {
		this.attr_json = attr_json;
	}

	public String getCost_price() {
		return cost_price;
	}

	public void setCost_price(String cost_price) {
		this.cost_price = cost_price;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getImg_ary() {
		return img_ary;
	}

	public void setImg_ary(String img_ary) {
		this.img_ary = img_ary;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	public String getLogistics_weight() {
		return logistics_weight;
	}

	public void setLogistics_weight(String logistics_weight) {
		this.logistics_weight = logistics_weight;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getList_img() {
		return list_img;
	}

	public void setList_img(String list_img) {
		this.list_img = list_img;
	}

	public String getBuyNum() {
		return buyNum;
	}
	
	public String getIs_free_shipping() {
		return is_free_shipping;
	}

	public void setIs_free_shipping(String is_free_shipping) {
		this.is_free_shipping = is_free_shipping;
	}

	public BigDecimal getGoods_freight() {
		return goods_freight;
	}

	public void setGoods_freight(BigDecimal goods_freight) {
		this.goods_freight = goods_freight;
	}

	public void setBuyNum(String buyNum) {
		if(EAString.stringToInt(buyNum, 1)>=EAString.stringToInt(this.stock, 1)){
			this.buyNum = this.stock;
		}else{
			this.buyNum = buyNum;
		}
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMinNum() {
		return minNum;
	}

	public void setMinNum(String minNum) {
		this.minNum = minNum;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}
	
}
