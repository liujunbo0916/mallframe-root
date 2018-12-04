package com.easaa.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessGradeMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;

@Service
public class BusinessGradeService extends EABaseService{

	@Autowired
	private BusinessGradeMapper businessGradeMapper;
	@Override
	public EADao getDao() {
		return businessGradeMapper;
	}
}
