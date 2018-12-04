package com.easaa.content.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface AdvertMapper  extends EADao{
	
	public List<PageData> selectadvertlistByPage(Page page);
	public List<PageData> selectpositionByPage(Page page);
	public List<PageData> selectpositionByMap(PageData pd);
	public PageData selectpositionById(PageData pd);
	public List<PageData> selectpositionByIds(PageData pd);
	public int insertposition(PageData pd);
	public int updateposition(PageData pd);
	public int deleteposition(PageData pd);
}
