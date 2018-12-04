package com.easaa.course.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.course.dao.TeacherCommentMapper;
import com.easaa.course.dao.TeacherLibMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class TeacherLibService extends EABaseService {
	@Autowired
	private TeacherLibMapper teacherLibMapper;
	@Autowired
	private TeacherCommentMapper teacherCommentMapper;
	
	@Override
	public EADao getDao() {
		return teacherLibMapper;
	}

	/**
	 * 前端接口注册时候查询老师库 如果有老师资料 返回真 并且更新这边的user_id字段
	 */
	public boolean queryTeacherlibByPhone(PageData pd) {
		PageData parameter = new PageData();
		parameter.put("teacher_phone", pd.getAsString("phone"));
		List<PageData> teachers = getByMap(parameter);

		if (teachers == null || teachers.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 申请成为老师
	 */
	public boolean addTeacher(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("teacher_real_name"))) {
			throw new Exception("teacher_real_name不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_idcard"))) {
			throw new Exception("teacher_idcard不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_phone"))) {
			throw new Exception("teacher_phone不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_seniority"))) {
			throw new Exception("teacher_seniority不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_education"))) {
			throw new Exception("teacher_education不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_self_introduction"))) {
			throw new Exception("teacher_self_introduction不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_sex"))) {
			throw new Exception("teacher_sex不能为空");
		}
		PageData tpd = new PageData();
		tpd.put("user_id", pd.getAsString("user_id"));
		Page page = new Page();
		page.setPd(tpd);
		List<PageData> tlists = teacherLibMapper.selectByMap(page);
		if (tlists != null && tlists.size() > 0) {
			throw new Exception("老师已存在！");
		}
//		pd.put("teacher_introduce_photo", EAString.returnSavePath(pd.getAsString("teacher_introduce_photo")));
		pd.put("teacher_auditing", "0");
		pd.put("create_date", EADate.getCurrentTime());
		// 插入老师列表
		teacherLibMapper.insert(pd);
		return true;
	}

	/**
	 * 老师列表
	 */
	public List<PageData> selectTeacher(PageData pd) {
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(EAString.stringToInt(pd.getAsString("currentPage"), 1));
		page.setShowCount(15);
		page.setPd(pd);
		return teacherLibMapper.selectTeaListsPage(page);
	}

	/**
	 * 插入用户关注老师
	 */
	public boolean insertTeacherCollect(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("teacher_id"))) {
			throw new Exception("teacher_id不能为空");
		}
		List<PageData> list = teacherLibMapper.selectTeacherCollect(pd);
		if (EAUtil.isEmpty(list) && list.size() == 0) {
			pd.put("create_time", EADate.getCurrentTime());
			int userc = teacherLibMapper.insertTeacherCollect(pd);
			if (userc > 0) {
				return true;
			}
			return false;
		} else {
			throw new Exception("已经关注过该老师了");
		}
	}
	/**
	 * 用户关注老师列表
	 */
	public List<PageData> selectTeacherCollect(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		return teacherLibMapper.selectTeacherCollect(pd);
	}
	/**
	 * 查询用户是否关注老师
	 */
	public int selectTeacherconcern(PageData pd) throws Exception {
		List<PageData> list = teacherLibMapper.selectTeacherCollect(pd);
		if (list != null && list.size() > 0) {
			return 1;
		}
		return 0;
	}
	/**
	 *取消关注老师
	 */
	public int deleteTeacherCollect(PageData pd) throws Exception {
			if (EAUtil.isEmpty(pd.getAsString("user_id")) && EAUtil.isEmpty(pd.getAsString("teacher_id"))) {
				throw new Exception("请求参数有误");
			}
			return teacherLibMapper.deleteTeacherCollect(pd);
	}
	/**
	 * 评论老师
	 */
	public boolean insertTeacherComment(PageData pd) throws Exception {
		pd.put("comment_context_time", EADate.getCurrentTime());
		if (teacherCommentMapper.insert(pd) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 老师回复
	 */
	public boolean updateTeacherComment(PageData pd) throws Exception {
		if (teacherCommentMapper.update(pd) > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 老师评论删除
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean deleteComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("tea_id"))) {
			throw new Exception("tea_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("comment_id"))) {
			throw new Exception("comment_id不能为空");
		}
		Page page = new Page();
		page.setPd(pd);
		List<PageData> commentlist=teacherCommentMapper.selectTeaCommentsPage(page);
		if (EAUtil.isEmpty(commentlist) || commentlist.size()==0) {
			throw new Exception("评论不存在！");
		}
		PageData dpd = new PageData();
		dpd.put("comment_ids", pd.getAsString("comment_id"));
		int incback = teacherCommentMapper.deleteComment(dpd);
		if (incback > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 评论老师列表
	 */
	public List<PageData> selectTeacherComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("tea_id"))) {
			throw new Exception("tea_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("currentpage"))) {
			throw new Exception("currentpage不能为空");
		}
		Page page= new Page();
		page.setApp(true);
		page.setCurrentPage(pd.getAsInt("currentpage"));
		page.setShowCount(15);
		page.setPd(pd);
		return teacherCommentMapper.selectTeaCommentsPage(page);
	}
	
	/**
	 * 查询老师Userid
	 */
	public PageData selectTeacherByUserId(Integer userid) throws Exception {
		return teacherLibMapper.selectByUserId(userid);
	}

}
