package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface OrderMapper extends EADao{
	public List<PageData> selectDetails(PageData pd);
	
	public PageData selectOrderDetails(PageData pd);
	
	/**
	 * 根据订单号查询
	 * @param orderSn
	 * @return
	 */
	public PageData selectByOrderSn(String orderSn);
	
	public PageData selectOneByOrderSn(String orderSn);
	
	public List<PageData> selectDetailsGoodslist(PageData pd);
	
	public List<PageData> selectOrderGoods(PageData pd);
	
	public int deleteOrder(int order_id);
	
	public List<PageData> selectAppByPage(Page page);
	
	public List<PageData> selectRebateOrderByPage(Page page);
	
	public List<PageData> selectRebateOrder(PageData pd);
	
	public int updateOrderAdress(PageData pd);
	
	public List<PageData> selectOrderCounts(PageData pd);

}
