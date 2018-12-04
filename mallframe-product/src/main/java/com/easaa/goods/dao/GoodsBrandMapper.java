package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface GoodsBrandMapper extends EADao {
	
	
	public List<PageData> selectByGroup(PageData pd);

	public List<PageData> recursion(PageData pd);
	
	public List<PageData> selectByCIds(PageData pd);
	
	public List<PageData> selectlSQByPage(Page pd);
}
