package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface ActivitySeckillMapper extends EADao{

	public List<PageData> selectProByKillId(Integer id);
	
	public int editProById(PageData pd);
	
	public List<PageData> selectProByMap(PageData pd);
	
	public int insertKillGoods(PageData pd);
	
	public int deleteKillGoods(PageData pd);
	
	public int updateKillGoods(PageData pd);
	
	public PageData selectProById(Integer id);
}
