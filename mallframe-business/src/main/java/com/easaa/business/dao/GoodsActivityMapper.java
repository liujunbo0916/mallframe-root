package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface GoodsActivityMapper  extends EADao{
	
	public PageData selectBsInfoByBsId(PageData pd);
	
	public PageData selectActivityOrderByOrderNo(PageData pd);
	
	public void insertBsInfo(PageData pd);
	
	public void updateBsInfo(PageData pd);
	
	/**
	 * 平台开放的活动类型
	 */
	public List<PageData> activityType(PageData pd);
	
	
	public void updateActivityType(PageData pd);
	
	/**
	 * 购买活动订单
	 * 
	 * 
	 */
	public List<PageData> activityOrderByPage(Page page);

	public List<PageData> priceList(PageData pd);
	
	
	public void insertPrice(PageData pd);
	
	public void updatePrice(PageData pd);
	
	/**
	 * 查询商家开通活动列表
	 * 
	 */
    public List<PageData> selectActivityByBusi(PageData pd);  
	
	
	
	
}
