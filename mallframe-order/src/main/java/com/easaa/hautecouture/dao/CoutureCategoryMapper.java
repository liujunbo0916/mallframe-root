package com.easaa.hautecouture.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

/**
 * 用户高端定制申请记录DAO接口
 * @author hsia
 * @version 2016-08-23
 */
public interface CoutureCategoryMapper extends EADao {
	
	public List<PageData> selectByIds(PageData pd);

	public int deleteByPhysics(PageData pd);

	public int deleteByLogic(PageData pd);

	public PageData getByCode(String code);

	
}