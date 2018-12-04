package com.easaa.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.bank.dao.BankMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;

@Service
public class BankService extends EABaseService{

	@Autowired
	private BankMapper bandMapper;
	@Override
	public EADao getDao() {
		return bandMapper;
	}
	
	public void updateBankBin(PageData pd){
		bandMapper.updateBankBin(pd);
	}
	
	public PageData selectByBankNo(String pd){
		return bandMapper.selectByBankNo(pd);
	}
}
