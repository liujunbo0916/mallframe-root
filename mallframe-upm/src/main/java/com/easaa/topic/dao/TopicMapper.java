package com.easaa.topic.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface TopicMapper extends EADao{
	public List<PageData> selectByGoods(PageData pd);
}
