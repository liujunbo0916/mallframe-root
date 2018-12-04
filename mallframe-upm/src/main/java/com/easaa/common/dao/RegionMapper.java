package com.easaa.common.dao;
import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface RegionMapper extends EADao{
	
	public List<PageData> selectByProvince(PageData pd);
	
	public List<PageData> selectByCity(PageData pd);
	
	public List<PageData> selectByArea(PageData pd);
}