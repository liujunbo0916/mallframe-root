package com.easaa.upm.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.upm.entity.Log;

public interface LogMapper extends EADao{
	
	int insert(Log log);
}
