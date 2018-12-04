package com.easaa.course.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.course.dao.CourseMapper;
import com.easaa.course.dao.TeacherLibMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class CourseService extends EABaseService {

	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private TeacherLibMapper teacherLibMapper;
	
	@Override
	public EADao getDao() {
		return courseMapper;
	}

	/**
	 * 查询课程列表
	 * 
	 * @param
	 * @return
	 */
	public List<PageData> getSelectLists(PageData pd) throws Exception {
		Page page = new Page();
		// 销量降序
		if (!EAUtil.isEmpty(pd.getAsString("is_buyer_total"))) {
			if (pd.getAsString("is_buyer_total").equals("1")) {
				pd.put("is_buyer_total", "1");
			}
		}
		// 价格升降序
		if (!EAUtil.isEmpty(pd.getAsString("is_course_price"))) {
			if (pd.getAsString("is_course_price").equals("1")) {
				// 降序
				pd.put("is_course_price", "1");
			} else {
				// 升序
				pd.put("is_course_price", "0");
			}
		}
		page.setApp(true);
		page.setCurrentPage(EAString.stringToInt(pd.getAsString("currentPage"), 1));
		page.setShowCount(15);
		page.setPd(pd);
		return courseMapper.selectBySubjectIDPage(page);
	}

	/**
	 * 课程列表（老师的）
	 * course_status 课程状态 0报名，1 截止报名， 2正在上课，3课程结束
	 * @param
	 * @return
	 */
	public List<PageData> selectTeaCourseList(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id为空");
		}
		//根据user_id 查询老师列表
		PageData teacher=teacherLibMapper.selectByUserId(pd.getAsInt("user_id"));
		if(EAUtil.isNotEmpty(teacher)){
			pd.put("teacher_id", teacher.getAsString("id"));
			pd.put("currentPage", EAString.stringToInt(pd.getAsString("currentPage"), 1));
			pd.put("course_examine", 1);
			pd.put("course_shelf_shelves", 1);
			page.setApp(true);
			page.setCurrentPage(pd.getAsInt("currentPage"));
			page.setShowCount(15);
			page.setPd(pd);
			return courseMapper.selectBySubjectIDPage(page);
		}else{
			throw new Exception("当前用户不是老师！");
		}
	}
	
	/**
	 * 课程推荐
	 * 
	 * @param
	 * @return
	 */
	public List<PageData> getSelectRecommend(PageData pd) {
		Page page = new Page();
		pd.put("limit", 3);
		pd.put("orderby","1");
		page.setPd(pd);
		return courseMapper.selectBySubjectID(page);
	}

	/**
	 * 课程详情（所有）
	 * @param
	 * @return
	 */
	public PageData getSelectDetail(PageData pd) throws Exception {
		Page pg = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("id"))) {
			throw new Exception("课程ID为空");
		}
		pg.setPd(pd);
		return courseMapper.selectDetail(pg);
	}

	/**
	 * 课程详情ByID
	 * 
	 * @param
	 * @return
	 */
	public PageData getDetailByID(PageData pd) throws Exception {
		Page pg = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("id"))) {
			throw new Exception("课程ID为空");
		}
		pg.setPd(pd);
		return courseMapper.selectDetail(pg);
	}

	/**
	 * 编辑删除关联商品（ course_type 1编辑 0 删除)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData updateCourseByType(PageData pd) {
		PageData data = new PageData();
		Integer id = pd.getAsInt("id");
		data.put("id", id);
		String course_goods_ids = pd.getAsString("course_goods_ids");
		// 编辑
		if (pd.getAsInt("course_type") == 1 && EAUtil.isNotEmpty(id)) {
			PageData course = courseMapper.selectById(id);
			data.put("category_id", course.getAsString("category_id"));
			if (EAUtil.isEmpty(course_goods_ids)) {		
				pd.put("course_goods_ids", "0");
				pd.put("course_goods_relation", "0");
			} else {
				pd.put("course_goods_relation", 1);
			}
		} else
		// 删除
		if (pd.getAsInt("course_type") == 0 && EAUtil.isNotEmpty(id)) {
			PageData course = courseMapper.selectById(id);
			data.put("category_id", course.getAsString("category_id"));
			String[] gids = course.getAsString("course_goods_ids").split(",");
			course_goods_ids = "";
			if (gids.length > 0) {
				List<String> gidslist = new ArrayList<String>();
				for (String string : gids) {
					gidslist.add(string);
				}
				for (int i = 0; i < gidslist.size(); i++) {
					if (gidslist.get(i).equals(pd.getAsString("course_goods_ids"))) {
						gidslist.remove(gidslist.get(i));
						i--;
					} else {
						course_goods_ids += gidslist.get(i) + ",";
					}
				}
				if (gidslist.size() > 0) {
					pd.put("course_goods_relation", "1");
					pd.put("course_goods_ids", course_goods_ids.substring(0, course_goods_ids.length() - 1));
				} else {
					pd.put("course_goods_ids", "0");
					pd.put("course_goods_relation", "0");
				}
			} else {
				pd.put("course_goods_ids", "0");
				pd.put("course_goods_relation", "0");
			}
		}
		pd.remove("course_type");
		courseMapper.update(pd);
		return data;
	}
	/**
	 * 课程复制 待审核  未上架
	 * @param pageData
	 * @return
	 */
	public Integer addCopyCourse(PageData pageData) {
		courseMapper.copyCourse(pageData);
		Integer courseId = pageData.getAsInteger("id");
		return courseId;
	}
	
}
