package com.easaa.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.Tools;
import com.easaa.course.dao.CourseHourMapper;
import com.easaa.course.dao.CourseMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class CourseHourService extends EABaseService {

	@Autowired
	private CourseHourMapper courseHourMapper;
	@Autowired
	private CourseMapper courseMapper;
	
	@Autowired
	private VenueLimitService venueLimitService;

	@Override
	public EADao getDao() {
		return courseHourMapper;
	}

	/**
	 * 查询课程内容列表
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getSelectlistbyid(PageData pd) {
		return courseHourMapper.selectByALL(pd);
	}

	/**
	 * 模糊查询
	 */
	public List<PageData> selectByListings(PageData pd) {
		return courseHourMapper.selectByListings(pd);

	}

	public void createHour(PageData pd) {
		this.create(pd);
		/**
		 * 插入场馆限制表
		 */
		pd.put("limit_type", "1");
		pd.put("limit_target_id", pd.getAsString("hour_id"));
		venueLimitService.delete(pd);
		pd.put("limit_listings", pd.getAsString("hour_venue_listings"));
		pd.put("limit_date", pd.getAsString("hour_date"));
		venueLimitService.create(pd);
	}

	public void editHour(PageData pd) {
		this.edit(pd);
		/**
		 * 先删除再创建
		 */
		pd.put("limit_type", "1");
		pd.put("limit_target_id", pd.getAsString("id"));
		venueLimitService.delete(pd);
		pd.put("limit_listings", pd.getAsString("hour_venue_listings"));
		pd.put("limit_date", pd.getAsString("hour_date"));
		venueLimitService.create(pd);
	}

	public void deleteHour(PageData pd) {
		this.delete(pd);
		pd.put("limit_type", "1");
		pd.put("limit_target_id", pd.getAsString("id"));
		venueLimitService.delete(pd);
	}

	/**
	 * 查询课程内容列表(后台)
	 * @param pd
	 * @return
	 */
	public List<PageData> selectAllList(Page pg) {
		return courseHourMapper.selectAllListPage(pg);
	}
	
	/**
	 * 我的课程
	 * @param pd
	 * @return
	 */
	public List<PageData> selectMyCourseForm(PageData pd) throws Exception{
		if(EAUtil.isEmpty(pd.getAsString("user_id")) ){
			throw new Exception("用户名不能为空");
		}
		Tools.replaceEmpty(pd);
		List<PageData> chlist=courseHourMapper.selectCourseForms(pd);
		for (PageData pageData : chlist) {
			PageData course= courseMapper.selectById(pageData.getAsInt("course_id"));
			pageData.put("course_name", course.getAsString("course_name"));
		}
		return chlist;
	}
	
}
