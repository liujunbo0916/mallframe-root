package com.easaa.order.dao;
import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface OrdersPayMapper extends EADao{
	
	public PageData selectByPaySnId(String pay_sn);
}