package com.easaa.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAccountLogMapper;
/**
 * 会员账户余额明细
 */
@Service
public class UserAccountLogService extends EABaseService{
	@Autowired
	private UserAccountLogMapper userAccountLogMapper;
	
	@Override
	public EADao getDao(){
		return userAccountLogMapper;
	}
	
	public List<PageData> getUserDetail(PageData parameter) {
		Page page=new Page();
		page.setApp(true);
		page.setCurrentPage(parameter.getAsInt("currentPage"));
		page.setShowCount(15);
		page.setPd(parameter);
		return getDao().selectByMap(page);
	}
	
	
}