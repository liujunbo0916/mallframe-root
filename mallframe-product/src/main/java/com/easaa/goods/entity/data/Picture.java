package com.easaa.goods.entity.data;

import java.io.Serializable;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;


public class Picture implements Serializable{
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	
	
	private String albumId;
	private String goodsId;
	
	//原图路径
	private String originalImg;
	
	//缩略图
/*	private String originalDetail;
	private String originalList;
	private String originalListbig;
	private String originalListmiddle;
	private String originalListsmall;*/
	
	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getOriginalImg() {
		return originalImg;
	}

	public void setOriginalImg(String originalImg) {
		//拼接缩略图
		/*if(StringUtils.isNotEmpty(originalImg)){
			String prefix = originalImg.substring(0,originalImg.lastIndexOf("."));
			String suffix = originalImg.substring(originalImg.lastIndexOf("."),originalImg.length());
			originalDetail = prefix+"detail"+suffix;
			originalList = prefix+"list"+suffix;
			originalList = prefix+"list"+suffix;
			originalListbig = prefix+"listbig"+suffix;
			originalListmiddle = prefix+"listmiddle"+suffix;
			originalListsmall = prefix+"listsmall"+suffix;
		}*/
		if(!originalImg.startsWith("http")){
			originalImg = PROPERTIESHELPER.getValue("imageShowPath")+originalImg;
		}
		this.originalImg = originalImg;
	}
}
