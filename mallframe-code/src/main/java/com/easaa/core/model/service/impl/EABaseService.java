package com.easaa.core.model.service.impl;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.cache.annotation.Cacheable;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.EAService;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public abstract class EABaseService implements EAService{
	@Override
	public abstract EADao getDao();

	@Override
	public List<PageData> getByPage(Page parameter) {
		return getDao().selectByPage(parameter);
	}

	@Override
	public List<PageData> getByMap(PageData parameter) {
		Page page=new Page();
		page.setPd(parameter);
		return getDao().selectByMap(page);
	}

	@Override
	public PageData getOneByMap(PageData parameter) {
		Page page=new Page();
		page.setPd(parameter);
		List<PageData> list=getDao().selectByMap(page);
		if(EAUtil.isNotEmpty(list) && list.size() == 1){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 指定条件的数据总行数
	 * @param map
	 * @return
	 */
	@Override
	public int getCount(PageData model){
		return getDao().getCount(model);
	}
	
	@Override
	public PageData getById(Integer parameter) {
		// TODO Auto-generated method stub
		return getDao().selectById(parameter);
	}

	@Override
	public PageData getById(String parameter) {
		// TODO Auto-generated method stub
		return getDao().selectById(parameter);
	}
	@Override
	
	public int create(PageData model) {
		// TODO Auto-generated method stub
		return getDao().insert(model);
	}

	@Override
	public int edit(PageData model) {
		// TODO Auto-generated method stub
		return getDao().update(model);
	}

	@Override
	public int delete(PageData parameter) {
		Page page=new Page();
		page.setPd(parameter);
		return getDao().delete(page);
	}
	
	public List<PageData> getByIds(String ids){
		if(EAString.isNullStr(ids)){
			return null;
		}
		if(ids.substring(ids.length()-1).equals(",")){
			ids=ids.substring(0, ids.length()-1);
		}
		PageData page=new PageData();
		page.put("ids", "("+ids+")");
		return getDao().selectByIds(page);
	}
	/*public int deleteBytype(PageData parameter) throws Exception{
		return getDao().deleteBytype(parameter);
	}*/
}
