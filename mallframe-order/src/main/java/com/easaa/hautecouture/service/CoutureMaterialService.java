package com.easaa.hautecouture.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.hautecouture.dao.CoutureMaterialMapper;


/**
 * 用户高端定制申请记录Service
 * @author hsia
 * @version 2016-08-23
 */
@Service
public class CoutureMaterialService extends EABaseService {
	@Autowired
	private CoutureMaterialMapper coutureMaterialMapper;

	
	public List<PageData> selectByIds(PageData pd){
 		
	    return 	coutureMaterialMapper.selectByIds(pd);
	}
	public int deleteBytype(PageData pd) throws Exception{
		String delType = pd.getString("delType");
		if("0".equals(delType)){
			return coutureMaterialMapper.deleteByPhysics(pd);
		}else if("1".equals(delType)){
			pd.put("del_flag", "1");
			return coutureMaterialMapper.deleteByLogic(pd);
		}
		return 0;
	}
	@Override
	public EADao getDao() {
		return coutureMaterialMapper;
	}
	
	
}