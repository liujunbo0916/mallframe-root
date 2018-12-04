package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface OrdersGoodsMapper extends EADao{
	
	public PageData selectGRById(PageData pd);
	
	public List<PageData> selectByOrderId(int orderid);
}