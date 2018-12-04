package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface ActivityCompoMapper extends EADao{

	public List<PageData> compoAllProList(int stringToInt);

	public List<PageData> selectCompoGoodsByMap(PageData pd);

	public void deleteCompoGoods(PageData pd);

	public void addCompoGoods(PageData pd);
	
	
}
