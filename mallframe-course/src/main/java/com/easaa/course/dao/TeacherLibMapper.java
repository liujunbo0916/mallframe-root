package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface TeacherLibMapper  extends EADao{
	/**
	 * 老师关注列表
	 * @param pd
	 * @return
	 */
	public List<PageData> selectTeacherCollect(PageData pd);
	/**
	 * 老师关注
	 * @param pd
	 * @return
	 */
	public int insertTeacherCollect(PageData pd);
	/**
	 * 老师取消关注
	 * @param pd
	 * @return
	 */
	public int deleteTeacherCollect(PageData pd);
	/**
	 * 老师列表
	 * @param pd
	 * @return
	 */
	public List<PageData> selectTeaListsPage(Page page);

	public PageData selectByUserId(int userid);
}
