package com.easaa.bank.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface BankMapper extends EADao{

	public void  updateBankBin(PageData pd);
	
	public PageData selectByBankNo(String  no);
	
}
