package com.easaa.template.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.template.dao.TemplateItemMapper;
@Service
public class TemplateItemService extends EABaseService {
	@Autowired
	private TemplateItemMapper templateItemMapper;
	
	public List<PageData> selectItemList(PageData pd){
		Page page=new Page();
		page.setPd(pd);
		return templateItemMapper.selectItemList(page);
	}
	
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return templateItemMapper;
	}

}
