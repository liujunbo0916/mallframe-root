package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.ActivitySeckillMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;

@Service
public class ActivitySeckillService  extends EABaseService{

	@Autowired
	private ActivitySeckillMapper activitySeckillMapper;
	
	@Override
	public EADao getDao() {
		return activitySeckillMapper;
	}
	/**
	 * 秒杀活动商品
	 * @param id
	 * @return
	 */
	public List<PageData> killProlist(Integer id){
		return activitySeckillMapper.selectProByKillId(id);
	}
	
	public int deitkillPro(PageData pd) {
		return activitySeckillMapper.editProById(pd);
	}
	
	public List<PageData> selectProByMap(PageData pd) {
		return activitySeckillMapper.selectProByMap(pd);
	}
	
	public int insertKillGoods(PageData pd) {
		return activitySeckillMapper.insertKillGoods(pd);
	}
	/**
	 * kill_id 或 id 必传其一  否则删除全部
	 * @param pd
	 * @return
	 */
	public int deletekillGoods(PageData pd) {
		return activitySeckillMapper.deleteKillGoods(pd);
	}
	
	public PageData selectProById(Integer id) {
		return activitySeckillMapper.selectProById(id);
	}
	
	public int updateKillGoods(PageData pd) {
		return activitySeckillMapper.updateKillGoods(pd);
	}
	
}
