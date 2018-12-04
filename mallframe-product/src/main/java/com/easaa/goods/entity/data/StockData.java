package com.easaa.goods.entity.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

/**
 * 库存实体对象
 * @author liujunbo
 *
 */
public class StockData  implements Serializable{
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	//库存id
	private String sku_id;
	//库存json串
	private String attr_json;
	//商品库存
	private String stock;
	//售价
	private String price;
	//商品id
	private String goods_id;
	
	private String bar_code;
	
	private String stock_alarm;

	private String img_ary;
	
	private List<String> pictures = new ArrayList<String>();
	
	public String getSku_id() {
		return sku_id;
	}
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}
	public String getAttr_json() {
		return attr_json;
	}
	public void setAttr_json(String attr_json) {
		this.attr_json = attr_json;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public List<String> getPictures() {
		return pictures;
	}
	
	public void setPictures(Object picturesTarget) {
		String pictures = null;
		if(picturesTarget instanceof java.lang.String){
			pictures = (String) picturesTarget;
		}
		if(picturesTarget instanceof java.util.List){
			this.pictures = (List<String>) picturesTarget;
		}
		if(StringUtils.isNotEmpty(pictures)){
			String[] tempPictures = pictures.split("\\,");
			for(int i = 0 ;i < tempPictures.length ; i++){
				if(!tempPictures[i].startsWith("http")){
					this.pictures.add(PROPERTIESHELPER.getValue("imageShowPath")+tempPictures[i]);
				}
			}
		}
	}
	public String getBar_code() {
		return bar_code;
	}
	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}
	public String getStock_alarm() {
		return stock_alarm;
	}
	public void setStock_alarm(String stock_alarm) {
		this.stock_alarm = stock_alarm;
	}
	public String getImg_ary() {
		return img_ary;
	}
	public void setImg_ary(String img_ary) {
		if(!img_ary.isEmpty() && !img_ary.startsWith("http")){
			this.pictures.add(PROPERTIESHELPER.getValue("imageShowPath")+img_ary);
		}
		this.img_ary = img_ary;
	}
	
	
	
}
