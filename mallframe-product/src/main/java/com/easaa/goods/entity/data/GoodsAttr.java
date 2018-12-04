package com.easaa.goods.entity.data;

import java.io.Serializable;

/**
 * 商品属性实体
 * @author liujunbo
 *
 */
public class GoodsAttr implements Serializable{

	//商品规格id
	private String attrId;
	//商品id
	private String goodsId;
	//规格名字
	private String attrName;
	//规格值
	private String attrValue;
	//规格排序
	private String attrSort;
	//是否销售属性
	private int isSale;

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String[] getAttrValue() {
		return attrValue.split("\\,");
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getAttrSort() {
		return attrSort;
	}

	public void setAttrSort(String attrSort) {
		this.attrSort = attrSort;
	}

	public int getIsSale() {
		return isSale;
	}

	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}


	
}
