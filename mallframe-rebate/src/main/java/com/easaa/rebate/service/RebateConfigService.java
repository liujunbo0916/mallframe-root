package com.easaa.rebate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.PageData;
import com.easaa.rebate.dao.RebateConfigMapper;

/**
 * 配置类,数据会缓存;请不要使用selectById
 * 
 * @author 约
 *
 */
@Service
public class RebateConfigService extends EABaseService {
	@Autowired
	private RebateConfigMapper rebateConfigMapper;

	private static PageData config = new PageData();

	public PageData getConfig() {
		return rebateConfigMapper.selectConfig();
	}

	@Override
	public EADao getDao() {
		return rebateConfigMapper;
	}

	/**
	 * 修改分销设置参数
	 * 
	 * @param pd
	 */
	public void updataRebateConfig(PageData pd) {
		rebateConfigMapper.update(pd);
	}

}