package com.easaa.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessAuthenticationMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;

/**
 * 社企认证
 *
 */
@Service
public class BusinessAuthenticationService  extends EABaseService{

	@Autowired
	private BusinessAuthenticationMapper businessAuthenticationMapper;
	
	@Override
	public EADao getDao() {
		return businessAuthenticationMapper;
	}

}
