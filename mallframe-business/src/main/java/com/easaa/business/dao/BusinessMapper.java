package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface BusinessMapper  extends EADao{
	
	public List<PageData> selectStayPage(Page pd);
	
	public PageData selectStaydata(PageData pd);
	
	public PageData selectByUserId(Integer pd);
	
	public PageData selectBusinessJoinById(Integer pd);
	
	public PageData selectBusinessJoinByUserId(Integer pd);
	
	public int insertBusinessJoin(PageData pd);
	
	public List<PageData> selectBusinessJoinByPage(Page pd);
	
	public List<PageData> selectBusinessJoinByMap(PageData pd);
	
	public int deleteBusinessJoin(PageData pd);
	
	public int updateBusinessJoin(PageData pd);
}
