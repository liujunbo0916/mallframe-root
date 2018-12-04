package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface SysLogMapper extends EADao{
	public List<PageData> selectExceptionByPage(Page page);
}
