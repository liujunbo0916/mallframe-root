package com.easaa.template.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.template.dao.TemplateMapper;
@Service
public class TemplateService extends EABaseService {
	@Autowired
	private TemplateMapper templateMapper;
	
	public List<PageData> selectTemplateList(PageData pd){
		Page page=new Page();
		page.setPd(pd);
		return  templateMapper.selectTemplateList(page);
	}
	
	@Override
	public EADao getDao() {
		return templateMapper;
	}

}
