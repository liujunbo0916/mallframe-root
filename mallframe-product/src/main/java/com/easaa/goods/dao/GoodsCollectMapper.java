package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface GoodsCollectMapper extends EADao {
	
	public List<PageData> selectByUserId(PageData pd);
	
	public List<PageData> selectAllByIdPage(Page pd);
	
	public List<PageData> selectCollect(Page pd);

}
