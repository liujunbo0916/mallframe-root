package com.easaa.wechatMenu.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface WechatReplyMapper extends EADao{

	PageData findByKw(PageData data);
}