package com.easaa.business.dao;

import java.util.List;

import com.easaa.business.entity.Seller;
import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface SellerMapper extends EADao {
	
	/**
	 * 根据传入条件查询
	 * @return
	 */
	public List<Seller> findByCondition(PageData pd);

}
