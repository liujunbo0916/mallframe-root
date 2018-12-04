package com.easaa.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.common.dao.DictionariesMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Dictionaries;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service     
public class DictionariesService extends EABaseService {
	
	@Autowired
	private DictionariesMapper dictionariesMapper;
	
	public void save(PageData pd) {
		dictionariesMapper.save(pd);
	}
	
	public void del(PageData pd) {
		dictionariesMapper.delete(pd);
	}
	
	public void update(PageData pd) {
		dictionariesMapper.edit(pd);
	}
	
	public List<PageData> datalistPage(Page page) {
		return dictionariesMapper.datalistPage(page);
	}
	
	public PageData findById(PageData pd) {
		return dictionariesMapper.findById(pd);
	}
	
	public PageData findByBianma(PageData pd) {
		return dictionariesMapper.findByBianma(pd);
	}
	public List<Dictionaries> listSubDictByParentId(String parentId) {
		return dictionariesMapper.listSubDictByParentId(parentId);
	}
	
	public PageData findFromTbs(PageData pd) {
		return dictionariesMapper.findFromTbs(pd);
	}
	public List<PageData> dataList() {
		return dictionariesMapper.dataList();
	}
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDict(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("/dictionaries/goEdit.do?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
			dict.setSubDict(this.listAllDict(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	public List<PageData> listAll() throws Exception {
		return dictionariesMapper.dataList();
	}
	
	
	
	@Override
	public EADao getDao() {
		return dictionariesMapper;
	}

}
