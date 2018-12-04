package com.easaa.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.ShareMapper;
@Service
public class ShareService extends EABaseService {
	
	@Autowired
	private ShareMapper shareMapper;
	
	public PageData selectByShareSn(String share_sn){
		 
		return shareMapper.selectByShareSn(share_sn);
	}
	
	public int updateByShareSn(PageData pd){
		
		return shareMapper.updateByShareSn(pd);
	}
	
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return shareMapper;
	}

}
