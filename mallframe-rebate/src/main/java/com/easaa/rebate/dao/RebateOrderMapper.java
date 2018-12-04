package com.easaa.rebate.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface RebateOrderMapper extends EADao{
	public List<PageData>  selectByMapPage(Page pg);
	/**
	 * 查询一级分销下面的所有订单
	 */
	public Integer selectOrderCount(PageData pd);
}
