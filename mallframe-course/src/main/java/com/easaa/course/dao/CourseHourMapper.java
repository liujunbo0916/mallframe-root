package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface CourseHourMapper extends EADao {

	public List<PageData> selectByALL(PageData pd);

	public List<PageData> selectByListings(PageData pd);

	public List<PageData> selectAllListPage(Page pg);
	
	public List<PageData> selectCourseForms(PageData pd);
}