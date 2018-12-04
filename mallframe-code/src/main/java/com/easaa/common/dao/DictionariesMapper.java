package com.easaa.common.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Dictionaries;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface DictionariesMapper extends EADao {
	
	public void save(PageData pd);
	public void delete(PageData pd);
	public void edit(PageData pd);
	public List<PageData> datalistPage(Page page);
	public PageData findById(PageData pd);
	public PageData findByBianma(PageData pd);
	public List<Dictionaries> listSubDictByParentId(String parentId);
	public PageData findFromTbs(PageData pd);
	public List<PageData> dataList();
}
