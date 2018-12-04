package com.easaa.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.course.dao.VenuePlaceMapper;

@Service
public class VenuePlaceService extends EABaseService{

	
	@Autowired
	private VenuePlaceMapper venuePlaceMapper;
	@Override
	public EADao getDao() {
		return venuePlaceMapper;
	}

	
	
}
