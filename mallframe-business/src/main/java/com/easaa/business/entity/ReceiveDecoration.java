package com.easaa.business.entity;

import java.util.List;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

public class ReceiveDecoration {
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	public String block_id;
	public String decoration_code; // 模板code
	public String bs_id;// 店铺ID 
	public String update_time;
	public String creater_name;
	public String sort;// 模板排序
	public List<DecorationGoods> goods; // 模板关联商品

	public String getBlock_id() {
		return block_id;
	}

	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}

	public String getDecoration_code() {
		return decoration_code;
	}

	public void setDecoration_code(String decoration_code) {
		this.decoration_code = decoration_code;
	}

	public String getBs_id() {
		return bs_id;
	}

	public void setBs_id(String bs_id) {
		this.bs_id = bs_id;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getCreater_name() {
		return creater_name;
	}

	public void setCreater_name(String creater_name) {
		this.creater_name = creater_name;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public List<DecorationGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<DecorationGoods> goods) {
		this.goods = goods;
	}
}

