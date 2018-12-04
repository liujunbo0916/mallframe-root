package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface ButtonRightsMapper extends EADao{

	public List<PageData> listAllBrAndQxname(PageData pd);
}
