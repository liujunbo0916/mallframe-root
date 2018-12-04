package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
public interface OrdersShippingMapper extends EADao{
	public List<PageData> selectAll(Page pd);
	public List<PageData> selectTrackByOrderId(PageData pd);
	public void insertTrack(PageData pd);
}