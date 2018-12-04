package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface BusinessRecommendMapper extends EADao {

	public List<PageData> selectMaxOrder(PageData pd);

}
