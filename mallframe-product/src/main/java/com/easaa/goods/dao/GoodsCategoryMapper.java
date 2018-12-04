package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface GoodsCategoryMapper extends EADao {

	public List<PageData> selectMaxOrder(PageData pd);
	public List<PageData> selectHeatList();
	public List<PageData> sublings(PageData pd);
}
