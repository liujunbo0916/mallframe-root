package com.easaa.business.entity;

import java.io.Serializable;
import java.util.List;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

/**
 * 返回前端的商品实体对象
 * 
 * @author liujunbo
 */
public class DecorationGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory.getPropertiesHelper(PropertiesFile.SYS);
	// 商品id
	private String id;
	// 商品名字
	private String goods_name;
	// 商品代码
	private String goods_sn;
	// 商品标签
	private String goods_tags;
	// 售价
	private String shop_price;
	// 市场价格
	private String market_price;
	// 描述 一般列表接口无值
	private String goods_desc;
	// 商品库存
	private String goods_stock;
	// 列表图片
	private String list_img;
	// 虚拟销售量
	private String virtual_sales;
	// 装修小模块ID
	private String m_id;
	// 装修小模块长度
	private String m_high;
	// 装修小模块宽度
	private String m_width;

	private String main_title;

	private String sub_title;
	
	private String img_url;
	
	private String t_id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_sn() {
		return goods_sn;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public String getGoods_tags() {
		return goods_tags;
	}

	public void setGoods_tags(String goods_tags) {
		this.goods_tags = goods_tags;
	}

	public String getShop_price() {
		return shop_price;
	}

	public void setShop_price(Float shop_price) {
		if (shop_price == null) {
			shop_price = (float) 0.0;
		}
		this.shop_price = shop_price + "";
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(Float market_price) {
		if (market_price == null) {
			market_price = (float) 0.0;
		}
		this.market_price = market_price + "";
	}

	public String getGoods_stock() {
		return goods_stock;
	}

	public void setGoods_stock(String goods_stock) {
		this.goods_stock = goods_stock;
	}

	public String getList_img() {
		return list_img;
	}

	public void setList_img(String list_img) {
		if (!list_img.startsWith("http")) {
			list_img = PROPERTIESHELPER.getValue("imageShowPath") + list_img;
		}
		this.list_img = list_img;
	}

	public String getVirtual_sales() {
		return virtual_sales;
	}

	public void setVirtual_sales(String virtual_sales) {
		this.virtual_sales = virtual_sales;
	}

	public String getGoods_desc() {
		return goods_desc;
	}

	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getM_high() {
		return m_high;
	}

	public void setM_high(String m_high) {
		this.m_high = m_high;
	}

	public String getM_width() {
		return m_width;
	}

	public void setM_width(String m_width) {
		this.m_width = m_width;
	}

	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getMain_title() {
		return main_title;
	}

	public void setMain_title(String main_title) {
		this.main_title = main_title;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		if (!img_url.isEmpty() && !img_url.startsWith("http") && img_url.length() < 100) {
			img_url = PROPERTIESHELPER.getValue("imageShowPath") + img_url;
		}
		this.img_url = img_url;
	}
	public void setMisImg_url(String img_url) {
		if (!img_url.isEmpty()) {
			img_url =img_url.replace(PROPERTIESHELPER.getValue("imageShowPath"), "");
		}
		this.img_url = img_url;
	}

	public String getT_id() {
		return t_id;
	}

	public void setT_id(String t_id) {
		this.t_id = t_id;
	}
	
}
