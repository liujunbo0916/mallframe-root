package com.easaa.appoint.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.appoint.dao.AppServeMapper;
@Service
public class AppServeService extends EABaseService{
	@Autowired
	private AppServeMapper appServeMapper;
	@Override
	public EADao getDao(){
		return appServeMapper;
	}
}