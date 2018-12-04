package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface VenueListingsMapper  extends EADao{
	public void deleteByMap(Page page);
	public PageData selectIdByCId(Integer page);
	
	public List<PageData> selectTimeByCId(PageData data);
}
