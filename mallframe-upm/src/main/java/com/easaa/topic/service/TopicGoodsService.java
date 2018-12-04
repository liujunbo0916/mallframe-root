package com.easaa.topic.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.topic.dao.TopicGoodsMapper;
@Service
public class TopicGoodsService extends EABaseService{
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Override
	public EADao getDao(){
		return topicGoodsMapper;
	}
}