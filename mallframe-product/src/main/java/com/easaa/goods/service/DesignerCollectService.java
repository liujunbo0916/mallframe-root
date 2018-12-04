package com.easaa.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.DesignerCollectMapper;
@Service
public class DesignerCollectService extends EABaseService {
	@Autowired
	private DesignerCollectMapper designerCollectMapper;
	
	public List<PageData> designerCollectList(PageData pd){
		
		return  designerCollectMapper.designerCollectList(pd);
	}
	
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return designerCollectMapper;
	}

}
