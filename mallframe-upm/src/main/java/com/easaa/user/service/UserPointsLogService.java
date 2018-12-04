package com.easaa.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.user.dao.UserPointsLogMapper;

/**
 * 积分明细service
 * @author liujunbo
 *
 */
@Service
public class UserPointsLogService  extends EABaseService{
	@Autowired
	private UserPointsLogMapper userPointsLogMapper;
	@Override
	public EADao getDao() {
		return userPointsLogMapper;
	}

}
