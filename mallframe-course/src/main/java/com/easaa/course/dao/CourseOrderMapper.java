package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface CourseOrderMapper extends EADao {

	public List<PageData> selectByTeaCoursePage(Page page);

	public List<PageData> selectJoinByPage(Page page);

	public List<PageData> selecMyCourseOrdersByPage(Page page);

	public List<PageData> selectByCourseId(Integer i);
}
