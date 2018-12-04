package com.easaa.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.goods.dao.GoodsWarehouseMapper;

@Service
public class GoodsWarehouseService extends EABaseService {
	@Autowired
	private GoodsWarehouseMapper GoodsWarehouseMapper;

	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return GoodsWarehouseMapper;
	}
	
}
