package com.easaa.rebate.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface RebateConfigMapper extends EADao{

	/**
	 * 查出分销配置
	 * @return
	 */
	public PageData selectConfig();
	
}
