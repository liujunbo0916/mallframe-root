package com.easaa.wechatMenu.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.wechatMenu.dao.WechatReplyMapper;
@Service
public class WechatReplyService extends EABaseService{
	@Autowired
	private WechatReplyMapper wechatReplyMapper;
	@Override
	public EADao getDao(){
		return wechatReplyMapper;
	}
	public PageData findByKw(PageData data) {
		return null;
	}
}