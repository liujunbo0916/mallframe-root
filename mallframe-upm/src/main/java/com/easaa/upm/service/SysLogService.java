package com.easaa.upm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.upm.dao.SysLogMapper;
/**
 * 系统日志信息
 * @author ryy
 *
 */
@Service
public class SysLogService extends EABaseService{

	@Autowired
	private SysLogMapper sysLogMapper;

	public EADao getDao() {
		return sysLogMapper;
	}
	/**
	 * 获取系统异常信息
	 * @param pg
	 * @return
	 */
	public List<PageData> selectExceptionByPage(Page pg){
		return sysLogMapper.selectExceptionByPage(pg);
	}
}
