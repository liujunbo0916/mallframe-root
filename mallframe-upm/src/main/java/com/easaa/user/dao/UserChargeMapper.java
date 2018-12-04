package com.easaa.user.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface UserChargeMapper extends EADao {
	
	public PageData selectBySn(String sn);

}
