package com.easaa.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.order.dao.TestMapper;

@Service
public class TestService extends EABaseService {
	@Autowired
	private TestMapper testMapper;

	@Override
	public EADao getDao() {
		return testMapper;
	}
	
	
	
}
