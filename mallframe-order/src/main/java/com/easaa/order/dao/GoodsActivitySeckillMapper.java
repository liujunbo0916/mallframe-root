package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.order.entity.SecKillGoodsList;
import com.easaa.order.entity.Seckill;

public interface GoodsActivitySeckillMapper extends EADao{

	public List<Seckill> selectAppByPage(Page pd);
	
	public SecKillGoodsList selectDetail(PageData pd);
	
	public void  insertPro(PageData pd);
	
	public SecKillGoodsList selectProByGoodsId(PageData pd);
	
	public List<PageData> selectProByCondition(PageData pd);
	
	public void  deleteProById(PageData pd);
	
	public SecKillGoodsList selectProByPage(Page page);
	
	public PageData selectByProId(PageData pd);
	
	
}
