package com.easaa.hautecouture.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface CoutureOrderLogMapper  extends EADao{
	public List<PageData> selectall();

	public List<PageData> selectByOrdId(String order_id);
}
