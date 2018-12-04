package com.easaa.user.dao;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface UserAccountMapper extends EADao {
	public PageData selectByUserId(int id);
	/**
	 * 更新会员推荐统计
	 * @param pd
	 * @return
	 */
	public int updateRecommenderCount(PageData pd);
	
	public int updatePoints(PageData pd);
}
