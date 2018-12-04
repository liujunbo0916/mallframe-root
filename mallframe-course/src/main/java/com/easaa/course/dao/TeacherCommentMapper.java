package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface TeacherCommentMapper extends EADao {
	
	public List<PageData> selectTeaCommentsPage(Page page);
	
	public int deleteComment(PageData pd);
	
}
