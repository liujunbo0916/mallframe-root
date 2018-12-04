package com.easaa.goods.entity.data;

import java.io.Serializable;

import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

/**
 * 评论实体
 * @author liujunbo
 *
 */
public class CommentsData implements Serializable{
	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	

	private String commentId;
	private String goodsId;
	//评论内容
	private String content;
	//评论类型 1商品  2课程
	private String commentType;
	//评论时间
	private String createTime;
	//评论等级
	private String star;
	//评论用户名
	private String userName;
	//评论者图像
	private String userPhoto;
	
	//评论图片
	private String commentImgs;
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCommentType() {
		return commentType;
	}
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		if(!userPhoto.startsWith("http")){
			userPhoto = PROPERTIESHELPER.getValue("imageShowPath")+userPhoto;
		}
		this.userPhoto = userPhoto;
	}
	public String getCommentImgs() {
		return commentImgs;
	}
	public void setCommentImgs(String commentImgs) {
		this.commentImgs = commentImgs;
	}
	
	
}
