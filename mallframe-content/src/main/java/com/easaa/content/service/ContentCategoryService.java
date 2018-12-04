package com.easaa.content.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.content.dao.ContentCategoryMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class ContentCategoryService extends EABaseService {

	@Autowired
	private ContentCategoryMapper contentCategoryMapper;
	
	@Override
	public EADao getDao() {
		return contentCategoryMapper;
	}
}
