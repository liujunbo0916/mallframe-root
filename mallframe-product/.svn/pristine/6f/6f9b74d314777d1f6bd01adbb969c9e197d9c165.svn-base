package com.easaa.goods.entity;

import java.io.Serializable;
import java.util.List;


/**
 * 评论树状结构
 * @author ryy
 * 
 * Map<Comment,List<Comment>>
 *
 */
public class Comment implements Serializable {

	private String commentId;
	private String userId;

	private String goodsId;
	private String courseId;

	private String content;
	private String star;

	private String commentType;
	private String commentImg;
	private String createTime;
	
	private String nickName;
	private String headPortrait;
	
	/**
	 * 回复列表
	 */
	private List<Comment> comments;
	

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getCommentImg() {
		return commentImg;
	}

	public Comment(String commentId, String userId, String goodsId, String courseId, String content, String star,
			String commentType, String commentImg, String createTime,String nickName,String headPortrait) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.goodsId = goodsId;
		this.courseId = courseId;
		this.content = content;
		this.star = star;
		this.commentType = commentType;
		this.commentImg = commentImg;
		this.createTime = createTime;
		this.nickName = nickName;
		this.headPortrait = headPortrait;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
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
		Comment other = (Comment) obj;
		if (commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!commentId.equals(other.commentId))
			return false;
		return true;
	}

	public Comment() {
		super();
	}

	public void setCommentImg(String commentImg) {
		this.commentImg = commentImg;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	
}
