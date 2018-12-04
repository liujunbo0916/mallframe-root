package com.easaa.order.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface OrdersRefundMapper extends EADao{
	
	public int insertRefundLog(PageData pd);

	public PageData selectRefund(Integer pd);

}
