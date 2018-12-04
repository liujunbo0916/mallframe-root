package com.easaa.core.model.service;

import java.util.List;
import java.util.Map;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface EAService {
	public EADao getDao();
	public List<PageData> getByPage(Page parameter);
	public List<PageData> getByMap(PageData parameter);
	public List<PageData> getByIds(String ids);
	public PageData getOneByMap(PageData parameter);
	public PageData getById(Integer parameter);
	public PageData getById(String parameter);
	public int create(PageData model);
	public int edit(PageData model);
	public int delete(PageData model);
	public int getCount(PageData parameter);
}
