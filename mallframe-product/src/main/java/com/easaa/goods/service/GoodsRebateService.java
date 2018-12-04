package com.easaa.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.goods.dao.GoodsRebateMapper;
@Service
public class GoodsRebateService extends EABaseService {

	@Autowired
	private GoodsRebateMapper goodsRebateMapper;
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return goodsRebateMapper;
	}

}
