package com.easaa.order.entity;

import java.io.Serializable;

public class CircleGoods implements Serializable{

	public String goods_id;
	public String goods_name;
	public String list_img;
	public String shop_price;
	public String goods_count;
	public String goods_attr;
	public String goods_sku_id;
	public CircleGoods(String goods_id, String goods_name, String list_img, String shop_price, String goods_count,
			String goods_attr, String goods_sku_id) {
		super();
		this.goods_id = goods_id;
		this.goods_name = goods_name;
		this.list_img = list_img;
		this.shop_price = shop_price;
		this.goods_count = goods_count;
		this.goods_attr = goods_attr;
		this.goods_sku_id = goods_sku_id;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
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
	public String getShop_price() {
		return shop_price;
	}
	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}
	public String getGoods_count() {
		return goods_count;
	}
	public void setGoods_count(String goods_count) {
		this.goods_count = goods_count;
	}
	public String getGoods_attr() {
		return goods_attr;
	}
	public void setGoods_attr(String goods_attr) {
		this.goods_attr = goods_attr;
	}
	public String getGoods_sku_id() {
		return goods_sku_id;
	}
	public void setGoods_sku_id(String goods_sku_id) {
		this.goods_sku_id = goods_sku_id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goods_sku_id == null) ? 0 : goods_sku_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CircleGoods other = (CircleGoods) obj;
		if (goods_sku_id == null) {
			if (other.goods_sku_id != null)
				return false;
		} else if (!goods_sku_id.equals(other.goods_sku_id))
			return false;
		return true;
	}
	
	
	
	
}
