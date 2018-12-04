package com.easaa.hautecouture.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

/**
 * 用户高端定制申请记录DAO接口
 * @author hsia
 * @version 2016-08-23
 */
public interface ApplicationMapper extends EADao {
	
	public List<PageData> selectByIds(PageData pd);

	public int deleteByPhysics(PageData pd);

	public int deleteByLogic(PageData pd);

	public int createOrd(PageData pd);

	public int uncheck(PageData pd);

	public void audit(PageData aPLC);

	public List<PageData> selectApplicsPage(Page page);

	public PageData selectApplicInfo(String iD);

}