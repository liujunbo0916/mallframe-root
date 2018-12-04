package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface GoodsCartMapper extends EADao {
	
	public int deleteBatch(int [] carts_id);
	
	public List<PageData> selectByUserId(PageData pd);
	
	public List<PageData> selectByUserCartId(PageData pd);
	
	public List<PageData> selectByIds(int [] carts_id);

	public PageData selectForgeBySkuId(PageData pd);
}
