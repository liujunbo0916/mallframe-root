package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.ActivityTimeLimitMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;

@Service
public class ActivityTimeLimitService extends EABaseService {

	@Autowired
	private ActivityTimeLimitMapper activityTimeLimitMapper;

	@Override
	public EADao getDao() {
		return activityTimeLimitMapper;
	}

	public List<PageData> limitAllProList(int stringToInt) {
		return activityTimeLimitMapper.selectLimitProList(stringToInt);
	}
	/**
	 * pd limit_id 或者id  必传其一  否则删除全部
	 * 
	 */
	public void deleteLimitGoods(PageData pd) {
		activityTimeLimitMapper.deleteLimitGoods(pd);
	}
	public void updateLimitGoods(PageData pd) {
		activityTimeLimitMapper.updateLimitGoods(pd);
	}
	public void insertLimitGoods(PageData pd) {
		activityTimeLimitMapper.insertLimitGoods(pd);
	}
	public PageData selectLimitGoodsByID(Integer pd) {
		return activityTimeLimitMapper.selectLimitGoodsByID(pd);
	}
	public List<PageData> selectLimitGoodsByMap(PageData pd) {
		return activityTimeLimitMapper.selectLimitGoodsByMap(pd);
	}
	
}
