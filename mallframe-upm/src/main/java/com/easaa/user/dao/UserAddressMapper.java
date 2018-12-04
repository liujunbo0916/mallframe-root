package com.easaa.user.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface UserAddressMapper extends EADao {

	public PageData getDefaultByUid(String uid);

}
