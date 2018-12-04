package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface DesignerGoodsMapper extends EADao {
	
	/**
	 * 关联设计师表  获得设计师id
	 * @param id
	 * @return
	 */
	public PageData getByGoodsId(Integer goods_id);
	
	/**
	 * 关联商品表
	 * @param designer_id
	 * @return
	 */
	public List<PageData> getByDesignerId(Integer designer_id);

}
