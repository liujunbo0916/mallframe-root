package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessRecommendMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;

/**
 * 店铺推荐service
 * @author 
 */
@Service
public class BusinessRecommendService  extends EABaseService{
	
	@Autowired
	private BusinessRecommendMapper businessRecommendMapper;
	
	@Override
	public EADao getDao() {
		return businessRecommendMapper;
	}

	public PageData selectOrder(PageData pageData) {
		/**
		 * 查询最大值
		 */
		List<PageData> result = businessRecommendMapper.selectMaxOrder(pageData);
		if(result == null || result.size() == 0){
			pageData.put("maxValue", pageData.getAsString("orderValue"));
			pageData.put("orderValue", 1);
		}else{
			pageData.put("maxValue", result.get(0).getAsString("recommend_order"));
			pageData.put("orderValue", result.size()+1);
		}
		return pageData;
	}
}
