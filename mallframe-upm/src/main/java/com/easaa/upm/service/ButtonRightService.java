package com.easaa.upm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.upm.dao.ButtonRightsMapper;

@Service
public class ButtonRightService extends EABaseService{

	@Autowired
	private ButtonRightsMapper buttonRightsMapper;
	@Override
	public EADao getDao() {
		return buttonRightsMapper;
	}
	public List<PageData> listAll(PageData pd) {
		return buttonRightsMapper.listAllBrAndQxname(pd);
	}
	public PageData findById(PageData pd) {
		return buttonRightsMapper.selectById(pd.getAsString("FHBUTTON_ID"));
	}
	public void save(PageData pd) {
		buttonRightsMapper.insert(pd);
	}

	
	
}
