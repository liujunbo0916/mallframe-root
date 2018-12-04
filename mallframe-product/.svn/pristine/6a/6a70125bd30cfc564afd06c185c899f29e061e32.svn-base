package com.easaa.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.goods.dao.GoodsTypeAttrMapper;

@Service
public class GoodsTypeAttrService extends EABaseService {

	@Autowired
	private GoodsTypeAttrMapper goodsTypeAttrMapper;
	
	@Override
	public EADao getDao() {
		return goodsTypeAttrMapper;
	}

}
