package com.easaa.hautecouture.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.entity.PageData;
import com.easaa.hautecouture.dao.ApplicationMapper;
import com.easaa.hautecouture.dao.CoutureDialogMapper;


/**
 * 用户高端定制沟通记录Service
 * @author hsia
 * @version 2016-09-01
 */
@Service
public class CoutureDialogService extends EABaseService {
	@Autowired
	private CoutureDialogMapper coutureDialogMapper;

	
	public List<PageData> selectByIds(PageData pd){
 		
	    return 	coutureDialogMapper.selectByIds(pd);
	}
	public int deleteBytype(PageData pd) throws Exception{
		String delType = pd.getString("delType");
		if("0".equals(delType)){
			return coutureDialogMapper.deleteByPhysics(pd);
		}else if("1".equals(delType)){
			pd.put("del_flag", "1");
			return coutureDialogMapper.deleteByLogic(pd);
		}
		return 0;
	}
	@Override
	public EADao getDao() {
		return coutureDialogMapper;
	}
	public List<PageData> getByOrdSn(String order_sn) {
		return coutureDialogMapper.selectByOrdSn(order_sn);
	}
	
	
	
}