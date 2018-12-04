package com.easaa.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsAttrMapper;
@Service
public class GoodsAttrService extends EABaseService {

	@Autowired
	private GoodsAttrMapper goodsAttrMapper;	
	
	public List<PageData> queryGoodsId(PageData pd){
		
		return goodsAttrMapper.queryGoodsId(pd);
	}
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return goodsAttrMapper;
	}

	public int deleteGoodAttr(PageData pd){
		return goodsAttrMapper.deleteByAttrId(pd);
	}
	
}
