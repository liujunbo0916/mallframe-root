package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface DesignerCollectMapper extends EADao {
	
	public List<PageData> designerCollectList(PageData pd);
	
}
