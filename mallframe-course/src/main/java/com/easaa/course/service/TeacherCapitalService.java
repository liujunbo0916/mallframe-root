package com.easaa.course.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.Tools;
import com.easaa.course.dao.TeacherCapitalMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;

@Service
public class TeacherCapitalService extends EABaseService {
	@Autowired
	private TeacherCapitalMapper teacherCapitalMapper;
	@Autowired
	private UserAccountMapper userAccountMapper;
	@Autowired
	private UserAccountLogMapper userAccountLogMapper;

	@Override
	public EADao getDao() {
		return teacherCapitalMapper;
	}

	/**
	 * 给老师分成
	 */
	public void insertCapital(PageData pd) throws Exception{
		PageData model= new PageData();
		//线上分成
		if(pd.getAsString("capital_type").equals("0")){
			model.put("course_id", pd.getAsString("course_id"));
			model.put("teacher_id", pd.getAsString("teacher_id"));
			model.put("capital", pd.getAsString("capital"));
			model.put("capital_type", pd.getAsString("capital_type"));
			model.put("admin_note", pd.getAsString("admin_note"));
		}else 
			//线下分成
		if(pd.getAsString("capital_type").equals("1")){
			model.put("course_id", pd.getAsString("course_id"));
			model.put("capital_type", pd.getAsString("capital_type"));
			model.put("admin_note", pd.getAsString("admin_note"));
		}else{
			throw new Exception("页面传递数据有误！请检查！");
		}
		model.put("admin_name", pd.getAsString("admin_name"));
		model.put("admin_id", pd.getAsString("admin_id"));
		model.put("create_time", EADate.getCurrentTime());
		Tools.replaceEmpty(model);
		teacherCapitalMapper.insert(model);
		//线上分成（更改用户钱包数据，插入钱包更改日志）
		if(pd.getAsString("capital_type").equals("0") &&
				EAUtil.isNotEmpty(pd.getAsString("teacher_id")) && 
				!pd.getAsString("teacher_id").equals("0")){
			/**
			 * 更改用户钱包
			 */
			Page paramPage = new Page();
			PageData payLog=new PageData();
			payLog.put("user_id", pd.getAsString("user_id"));
			paramPage.setPd(payLog);
			List<PageData> userAccounts = userAccountMapper.selectByMap(paramPage);
			if (userAccounts != null && userAccounts.size() > 0) {
				payLog = userAccounts.get(0);
				BigDecimal userMoney = new BigDecimal(payLog.getAsString("user_money"));
				payLog.put("user_money", userMoney.add(pd.getAsBigDecimal("capital")));
				userAccountMapper.update(payLog);
			}
			/**
			 * 更改钱包流水
			 */
			payLog.clear();
			payLog.put("user_id", pd.getAsString("user_id"));
			payLog.put("log_money", pd.getAsBigDecimal("capital"));
			payLog.put("log_type", "11");
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_note", "课程老师分成金额");
			payLog.put("log_symbol", 0);
			userAccountLogMapper.insert(payLog);
		}
	}
	
	public List<PageData> selectAllByPage(Page page){
		return teacherCapitalMapper.selectAllByPage(page);
	};
	
}
