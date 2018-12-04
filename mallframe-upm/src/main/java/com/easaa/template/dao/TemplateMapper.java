package com.easaa.template.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface TemplateMapper extends EADao {

	public List<PageData> selectTemplateList(Page page);

}
