package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.SellerMapper;
import com.easaa.business.entity.Seller;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;

/**
 * 商家服务service
 * @author liujunbo
 *
 */
@Service
public class SellerService  extends EABaseService{

	@Autowired
	private SellerMapper sellerMapper;
	
	@Override
	public EADao getDao() {
		return sellerMapper;
	}
	
	public Seller selectByConditionOne(PageData pd){
		
		List<Seller> sellers = sellerMapper.findByCondition(pd);
		if(sellers == null || sellers.size() == 0){
			return null;
		}else{
			return sellers.get(0);
		}
		
	}
	public List<Seller> selectByCondition(PageData pd ){
		return sellerMapper.findByCondition(pd);
	}

}
