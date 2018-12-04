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
import com.easaa.hautecouture.dao.CoutureCategoryMapper;


/**
 * 用户高端定制申请记录Service
 * @author hsia
 * @version 2016-08-23
 */
@Service
public class CoutureCategoryService extends EABaseService {
	@Autowired
	private CoutureCategoryMapper coutureCategoryMapper;

	
	public List<PageData> selectByIds(PageData pd){
 		
	    return 	coutureCategoryMapper.selectByIds(pd);
	}
	public int deleteBytype(PageData pd) throws Exception{
		String delType = pd.getString("delType");
		if("0".equals(delType)){
			return coutureCategoryMapper.deleteByPhysics(pd);
		}else if("1".equals(delType)){
			pd.put("del_flag", "1");
			return coutureCategoryMapper.deleteByLogic(pd);
		}
		return 0;
	}
	@Override
	public EADao getDao() {
		return coutureCategoryMapper;
	}
	public PageData getByCode(String code)throws Exception {
		return coutureCategoryMapper.getByCode(code);
	}
	
	
	
	
	
}