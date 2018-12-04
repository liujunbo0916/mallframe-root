package com.easaa.upm.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.upm.dao.ButtonMapper;

@Service
public class ButtonService extends EABaseService{

	
	@Autowired
	private ButtonMapper buttonMapper;
	@Override
	public EADao getDao() {
		return buttonMapper;
	}
	public List<PageData> listAll(PageData pd) {
		return buttonMapper.listAll(pd);
	}
	public void save(PageData pd) {
		buttonMapper.insert(pd);
	}
	public void deleteAll(PageData pd) {
		String ids = pd.getAsString("DATA_IDS");
		if(StringUtils.isNotEmpty(ids)){
			String ArrayDATA_IDS[] = ids.split(",");
			buttonMapper.deleteAll(ArrayDATA_IDS);
		}
	}
	public PageData findByCondition(PageData pd) {
		buttonMapper.findByCondition(pd);
		return null;
	}
	public List<PageData> list(Page page) {
		return buttonMapper.selectByPage(page);
	}
}
