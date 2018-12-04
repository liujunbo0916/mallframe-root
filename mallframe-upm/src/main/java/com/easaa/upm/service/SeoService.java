package com.easaa.upm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.upm.dao.SeoMapper;

@Service
public class SeoService extends EABaseService {

	@Autowired
	private SeoMapper seoMapper;
	
	@Override
	public EADao getDao() {
		return seoMapper;
	}
	
}
