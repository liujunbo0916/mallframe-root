package com.easaa.wechatMenu.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface WechatMenuMapper extends EADao{
	/**
	 * 删除微信菜单
	 * @param id
	 * @return 
	 */
	public int delete(String id);
	
	/**
	 * 根据条件查询总数
	 * @param id
	 * @return 
	 */
	public int getCount(PageData pd);
}