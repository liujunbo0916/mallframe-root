package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessOptLogMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class BusinessOptLogService  extends EABaseService{

	@Autowired
	private BusinessOptLogMapper businessOptLogMapper;
	
	@Override
	public EADao getDao() {
		return businessOptLogMapper;
	}
	
	
	public List<PageData> getExceptionByPage(Page page){
		return businessOptLogMapper.selectExceptionByPage(page);
	}

}
