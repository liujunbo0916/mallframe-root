package com.easaa.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.DesignerGoodsMapper;
@Service
public class DesignerGoodsService extends EABaseService {

	@Autowired
	private DesignerGoodsMapper designerGoodsMapper;
	
	public PageData getByGoodsId(Integer goods_id){
		
		return designerGoodsMapper.getByGoodsId(goods_id);
	}
	
	public List<PageData> getByDesignerId(Integer designer_id){
		
		return  designerGoodsMapper.getByDesignerId(designer_id);
	}
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return designerGoodsMapper;
	}

}
