package com.easaa.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.code.dao.CodeMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
@Service
public class CodeService extends EABaseService {
	
	@Autowired
	private CodeMapper codeMapper;
	public PageData getCreateSql(String	tableName){
		PageData pd=new PageData();
		pd.put("tableName", tableName);
		return codeMapper.selectCreateSql(pd);
	}
	@Override
	public EADao getDao() {
		return codeMapper;
	}

}
