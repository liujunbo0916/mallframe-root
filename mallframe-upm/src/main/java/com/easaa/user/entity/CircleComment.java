package com.easaa.user.entity;

import java.io.Serializable;

public class CircleComment implements Serializable{

	public String comment_author_logo;
	public String comment_id;
	public String circle_id;
	public String comment_context_time;
	public String comment_context;
	public String user_id;
	public String p_id;
	public String comment_author;
	public String comment_author_alinas;

	public CircleComment(String comment_author_logo, String comment_id, String circle_id, String comment_context_time,
			String comment_context, String user_id, String p_id, String comment_author) {
		super();
		this.comment_author_logo = comment_author_logo;
		this.comment_id = comment_id;
		this.circle_id = circle_id;
		this.comment_context_time = comment_context_time;
		this.comment_context = comment_context;
		this.user_id = user_id;
		this.p_id = p_id;
		this.comment_author = comment_author;
	}
	
	
	public String getComment_author_alinas() {
		return comment_author_alinas;
	}

	public void setComment_author_alinas(String comment_author_alinas) {
		this.comment_author_alinas = comment_author_alinas;
	}

	public String getComment_author_logo() {
		return comment_author_logo;
	}
	public void setComment_author_logo(String comment_author_logo) {
		this.comment_author_logo = comment_author_logo;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getCircle_id() {
		return circle_id;
	}
	public void setCircle_id(String circle_id) {
		this.circle_id = circle_id;
	}
	public String getComment_context_time() {
		return comment_context_time;
	}
	public void setComment_context_time(String comment_context_time) {
		this.comment_context_time = comment_context_time;
	}
	public String getComment_context() {
		return comment_context;
	}
	public void setComment_context(String comment_context) {
		this.comment_context = comment_context;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public String getComment_author() {
		return comment_author;
	}
	public void setComment_author(String comment_author) {
		this.comment_author = comment_author;
	}

	
	
	
}
