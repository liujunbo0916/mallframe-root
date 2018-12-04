package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.ActivityCompoMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;

@Service
public class ActivityCompoService  extends EABaseService{

	@Autowired
	private ActivityCompoMapper activityCompoMapper;
	@Override
	public EADao getDao() {
		return activityCompoMapper;
	}
	public List<PageData> compoAllProList(int stringToInt) {
		return activityCompoMapper.compoAllProList(stringToInt);
	}
	public List<PageData> selectCompoGoodsByMap(PageData pd) {
		return activityCompoMapper.selectCompoGoodsByMap(pd);
	}
	/**
	 * pd compo_id 或者id  必传其一  否则删除全部
	 * 
	 */
	public void deleteCompoGoods(PageData pd) {
		 activityCompoMapper.deleteCompoGoods(pd);
	}
	public void addCompoGoods(PageData pd) {
		 activityCompoMapper.addCompoGoods(pd);
	}
}
