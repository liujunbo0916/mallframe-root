package com.easaa.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessClassMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;

/**
 * 店铺分类service
 * @author 
 */
@Service
public class BusinessClassService  extends EABaseService{

	@Autowired
	private BusinessClassMapper businessClassMapper;
	
	@Override
	public EADao getDao() {
		return businessClassMapper;
	}

}
