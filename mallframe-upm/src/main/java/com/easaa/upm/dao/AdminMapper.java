package com.easaa.upm.dao;


import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
import com.easaa.upm.entity.Admin;

public interface AdminMapper extends EADao{

	public void updateLastLogin(PageData pd);
	
	public PageData getUserInfo(PageData pd);
	
	public List<PageData> getUserByCondition(PageData pd);
	
	public Admin  getUserAndRoleById(PageData pd);
	
	public List<PageData> listAllUserByRoldId(PageData pd);
	
	public void delete(PageData pd);
	
	public void deleteBatch(String[] ids);
}
