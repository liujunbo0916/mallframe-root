package com.easaa.hautecouture.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.hautecouture.dao.ApplicationMapper;


/**
 * 用户高端定制申请记录Service
 * @author hsia
 * @version 2016-08-23
 */
@Service
public class ApplicationService extends EABaseService {
	@Autowired
	private ApplicationMapper applicationMapper;

	
	public List<PageData> selectByIds(PageData pd){
 		
	    return 	applicationMapper.selectByIds(pd);
	}
	public int deleteBytype(PageData pd) throws Exception{
		String delType = pd.getString("delType");
		if("0".equals(delType)){
			return applicationMapper.deleteByPhysics(pd);
		}else if("1".equals(delType)){
			pd.put("del_flag", "1");
			return applicationMapper.deleteByLogic(pd);
		}
		return 0;
	}
	@Override
	public EADao getDao() {
		return applicationMapper;
	}
	
	public int createOrd(PageData pd)throws Exception{
		pd.put("reviewed", "1");
		pd.put("auditor", "");
		pd.put("audit_time", EADate.getCurrentTime());
		return applicationMapper.uncheck(pd);
	}
	public void audit(PageData aPLC)throws Exception {
		applicationMapper.audit(aPLC);
	}
	public List<PageData> selectApplcList(Page page) throws Exception {
		return applicationMapper.selectApplicsPage(page);
	}
	public PageData selectApplicInfo(String ID) throws Exception {
		return applicationMapper.selectApplicInfo(ID);
	}
}