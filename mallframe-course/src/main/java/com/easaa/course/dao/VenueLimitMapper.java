package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface VenueLimitMapper extends EADao{
	
	public List<PageData> selectByListings(PageData pd);
	public PageData selectByPd(PageData pd);
}
