package com.easaa.content.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface ContentCommentMapper extends EADao {

	public int deleteComment(PageData pd);
	
	public int insertThumbsup(PageData pd);
	
	public PageData selectThumbsupById(PageData pd);
	
	public List<PageData> selectThumbsuplist(PageData pd);
}
