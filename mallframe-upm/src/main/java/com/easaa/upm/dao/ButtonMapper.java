package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface ButtonMapper extends EADao{

	public List<PageData> listAll(PageData pd);

	public void deleteAll(String[] arrayDATA_IDS);

	public List<PageData> findByCondition(PageData pd);
}
