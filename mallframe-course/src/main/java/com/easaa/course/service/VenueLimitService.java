package com.easaa.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.course.dao.VenueLimitMapper;
import com.easaa.entity.PageData;

@Service
public class VenueLimitService  extends EABaseService{

	
	@Autowired
	private VenueLimitMapper venueLimitMapper;

	@Override
	public EADao getDao() {
		return venueLimitMapper;
	}
	public List<PageData> selectByListings(PageData pd){
		return venueLimitMapper.selectByListings(pd);
	}
	
}
