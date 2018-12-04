package com.easaa.code.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface CodeMapper extends EADao    {
	public PageData selectCreateSql(PageData pd);
}
