package com.easaa.user.dao;
import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
public interface UserBindingMapper extends EADao{
	public List<PageData> selectBinding(PageData page);
}