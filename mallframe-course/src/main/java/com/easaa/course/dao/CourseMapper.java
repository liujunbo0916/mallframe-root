package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
public interface CourseMapper  extends EADao{
	
	public List<PageData> selectBySubjectIDPage(Page pg);
	
	public PageData selectDetail(Page pg);
	
	public List<PageData> selectBySubjectID(Page pg);
	/**
	 * 课程复制
	 * @param pd
	 */
	public void copyCourse(PageData pd);
}
