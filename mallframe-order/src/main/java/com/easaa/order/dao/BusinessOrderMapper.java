package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.order.entity.BusinessOrder;

public interface BusinessOrderMapper extends EADao{
	
	public List<BusinessOrder>  selectEntityBusinessOrder(Page page);
	
	public BusinessOrder selectEntityByID(Integer orderId);
	
	public List<PageData> selectOrderMap(PageData pd);
	
	public List<PageData> selectAllByMap(Page pd);
	
	public Integer insertOrderGoods(PageData pd);
	
	public Integer countOrderByStatus(PageData pd);

	public List<PageData> selectOrderCounts(PageData pd);
	
	public int deleteOrder(int order_id);

	public PageData selectByOrderSn(String orderNo);
	
	public List<PageData>  selectListByPage(Page page);
	
	
	/**
	 * 按订单状态查询订单数
	 * @param pd
	 * @return
	 */
	public Integer statisticsOrderStatus(PageData pd);
	
	/**
	 * 按月份查询销售金额
	 * @param pd
	 * @return
	 */
	public List<PageData> statisticsOrderByMonth(PageData pd);
	
	/**
	 * 查询该商铺总访客量
	 * 
	 */
	public Integer selectVisitCount(PageData pd);
	
	/**
	 * 查询店铺总销售额
	 */
	public String statisticsOrderMoeny(PageData pd);
	
	/**
	 * 查询最近5天的访客量
	 * 
	 */
	public List<PageData> selectVisitRecord(PageData pd);
	
	
	
}
