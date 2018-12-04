package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface CouponMapper extends EADao {
	
	public int insertrecord(PageData pd);
	
	public List<PageData> selectAppByPage(Page page);
	
	public List<PageData> selectrecord(PageData pd);
	
	public List<PageData> selectAllOwer(PageData pd);
	
	public int updaterecord(PageData pd);
}

