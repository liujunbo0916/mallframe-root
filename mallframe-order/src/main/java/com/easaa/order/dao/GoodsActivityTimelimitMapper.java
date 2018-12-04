package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface GoodsActivityTimelimitMapper  extends EADao{
	
	public List<PageData> selectProductByPage(Page page);
	
	public List<PageData> selectProductByMap(PageData pd);
	
	public void insertProduct(PageData pd);
	
	public void updateProduct(PageData pd);
	
	public void deleteProductByID(PageData pd);
	
	public void deleteProductByLimitID(PageData pd);
	
	public PageData selectProductById(Integer id);
	
	public PageData selectByProId(PageData pd);
	
}
