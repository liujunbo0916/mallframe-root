package com.easaa.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.course.dao.SubjectMapper;

@Service
public class SubjectService  extends EABaseService{
	@Autowired
	private SubjectMapper subjectMapper;
	@Override
	public EADao getDao() {
		return subjectMapper;
	}

}
