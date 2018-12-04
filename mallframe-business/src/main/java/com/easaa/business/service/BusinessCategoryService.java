package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessCategoryMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.PageData;

/**
 * 店铺分类service
 * @author 
 */
@Service
public class BusinessCategoryService  extends EABaseService{

	@Autowired
	private BusinessCategoryMapper businessCategoryMapper;
	
	@Override
	public EADao getDao() {
		return businessCategoryMapper;
	}
	
	/**
	 * 店铺分类类目表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> goodsCategoryList(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("bs_parent_id"))) {
			throw new Exception("类目ID不能为空");
		}
		return getByMap(pd);
	}
	

	/**
	 * 查看一个类目是否 是叶子节点
	 */
	public boolean queryCategoryIsLeaf(PageData pd) {
		/**
		 * 如果没有上级 则不是叶子节点 没有达到三级
		 */
		if (pd.getAsInteger("bs_parent_id") == 0) {
			return false;
		}
		PageData param = new PageData();
		param.put("bs_category_id", pd.getAsInt("bs_parent_id"));
		PageData parentCategory = getByMap(param).get(0);
		/**
		 * 如果父亲没有上级 则也不是叶子节点
		 */
		if (parentCategory.getAsInt("bs_parent_id") == 0) {
			return false;
		} else {
			return true;
		}
	}
}
