package com.easaa.content.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.content.dao.ContentCommentMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;

@Service
public class ContentCommentService  extends EABaseService{

	@Autowired
	private ContentCommentMapper contentCommentMapper;
	
	@Override
	public EADao getDao() {
		return contentCommentMapper;
	}
	
	
	
	
}
