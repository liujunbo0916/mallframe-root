package com.easaa.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessMsgMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;

/**
 * 店铺消息service
 * @author 
 */
@Service
public class BusinessMsgService  extends EABaseService{

	@Autowired
	private BusinessMsgMapper businessMsgMapper;
	
	@Override
	public EADao getDao() {
		return businessMsgMapper;
	}

}
