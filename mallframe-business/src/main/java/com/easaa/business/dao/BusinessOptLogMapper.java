package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface BusinessOptLogMapper  extends EADao{
	
	public void insertExcetion(PageData pd);
	public List<PageData> selectExceptionByPage(Page page);
}
