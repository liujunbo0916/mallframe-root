package com.easaa.topic.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.topic.dao.TopicMapper;
@Service
public class TopicService extends EABaseService{
	@Autowired
	private TopicMapper topicMapper;
	
	public List<PageData> getByGoods(PageData pd){
		return topicMapper.selectByGoods(pd);
	}
	
	@Override
	public EADao getDao(){
		return topicMapper;
	}
}