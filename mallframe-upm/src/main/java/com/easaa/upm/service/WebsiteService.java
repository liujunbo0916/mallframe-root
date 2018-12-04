package com.easaa.upm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.upm.dao.WebsiteMapper;

@Service
public class WebsiteService extends EABaseService {

	@Autowired
	private WebsiteMapper websiteMapper;
	
	@Override
	public EADao getDao() {
		return websiteMapper;
	}
	
}
