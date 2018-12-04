package com.easaa.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.DesignerMapper;
@Service
public class DesignerService extends EABaseService {
	@Autowired
	private DesignerMapper designerMapper;
	
	public List<PageData> designerDetail(Integer id){
		
		return designerMapper.designerDetail(id);
	}
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return designerMapper;
	}

}
