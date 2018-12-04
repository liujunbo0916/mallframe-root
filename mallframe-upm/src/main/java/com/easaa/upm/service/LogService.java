package com.easaa.upm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.upm.dao.LogMapper;

@Service
public class LogService extends EABaseService{

	@Autowired
	private LogMapper logMapper;

	public EADao getDao() {
		return logMapper;
	}
}
