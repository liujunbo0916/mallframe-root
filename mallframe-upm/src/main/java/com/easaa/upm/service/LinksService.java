package com.easaa.upm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.upm.dao.LinksMapper;

@Service
public class LinksService extends EABaseService {

	@Autowired
	private LinksMapper linksMapper;
	
	@Override
	public EADao getDao() {
		return linksMapper;
	}
	
}
