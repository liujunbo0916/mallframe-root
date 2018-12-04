package com.easaa.business.dao;

import java.util.List;

import com.easaa.business.entity.DecorationGoods;
import com.easaa.business.entity.ReceiveDecoration;
import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface BusinessDecorationMapper extends EADao {

	/**
	 * 店铺关联模板
	 * @param pd
	 */
	public void insertDecorationBlock(PageData pd);

	public PageData selectByDbId(Integer id);
	/**
	 * 店铺关联模板列表（排序）
	 * @param pd
	 */
	public List<ReceiveDecoration> selectByDbMap(PageData pd);
	/**
	 * 修改店铺关联模板
	 * @param pd
	 */
	public void updatetbdb(PageData pd);
	/**
	 * 某一个模板关联商品
	 * @param pd
	 */
	public void insertDecorationGoods(PageData pd);
	/**
	 * 某一个模板关联商品
	 * @param pd
	 */
	public PageData selectDGByDBId(Integer id);
	/**
	 * 模板关联商品列表
	 * @param pd
	 */
	public List<PageData> selectByDgMap(PageData pd);
	/**
	 * 修改模板关联商品
	 * @param pd
	 */
	public void updatetbdg(PageData pd);

	public void deleteDecorationBlock(ReceiveDecoration pd);
	
	public void deleteDecorationGoods(PageData pd);

	public PageData selectDecorationByCode(String code);
	/**
	 * 级联删除商家装修店铺
	 */
	public void cascadeTplByBsid(PageData pd);
	
	
	
}
