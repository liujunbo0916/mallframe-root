package com.easaa.business.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface SellerGoodsCategoryMapper extends EADao {
	
	public List<PageData> bindCategoryList(PageData pd);
	
	public List<PageData> bindCategoryListPage(Page page);
	
    //申请经营类目
	public void  insertBindCategory(PageData pd);
	//查询申请分类是否包含选择分类
	public List<PageData> selectApplyCategroy(PageData pd);
	//删除申请的分类
	public void delApply(PageData pd);
	
}
