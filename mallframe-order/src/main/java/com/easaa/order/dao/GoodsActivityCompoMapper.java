package com.easaa.order.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.order.entity.Compo;

public interface GoodsActivityCompoMapper extends EADao{
	
	
	public List<Compo> selectCompoByPage(Page page);
	
	
	public Compo selectCompoInfo(PageData pd);


	public void addCompo(PageData pd);
	
	public List<Compo> selectCompoPorlist(Page page);
	
	public List<Compo> selectCompoPorlistByPage(Page page);
	
	public List<Compo> selectCompoPorlistByGoodsId(Page Page);
	
	public List<PageData> selectProByCondition(PageData pd);
	
	
	public void insertPro(PageData pd);
	
	
	public void updateCompo(PageData pd);

	public void delCompoPro(PageData pd);
	
	public void delCompo(PageData pd);

}
