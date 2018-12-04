package com.easaa.article.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.article.dao.ArticleMapper;
@Service
public class ArticleService extends EABaseService{
	@Autowired
	private ArticleMapper articleMapper;
	@Override
	public EADao getDao(){
		return articleMapper;
	}
}