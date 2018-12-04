package com.easaa.hautecouture.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface CoutureOrderMapper extends EADao{
	public List<PageData> selectDetails(PageData pd);
	public int deleteByPhysics(PageData pd);

	public int deleteByLogic(PageData pd);
	public int editByMode(PageData pd);
	public PageData selectAddrById(Integer asInt);
	public int updateStatus(PageData pd);
	public PageData selectByOsn(String order_sn);
	public List<PageData> selectOrderList(Page page);
}
