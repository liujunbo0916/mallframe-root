package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
import com.easaa.upm.entity.Menu;

public interface MenuMapper extends EADao{

	public List<Menu> listSubMenuByParentId(String menuId);
	
	public void insertMenu(Menu menu);
	
	public PageData findMaxId(PageData pd);
	
	public void deleteMenuById(String menuId);
	
	public void updateMenu(Menu menu);
	
	public PageData editicon(PageData pd);
	
}
