package com.easaa.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsCollectMapper;

@Service
public class GoodsCollectService extends EABaseService {
	@Autowired
	private GoodsCollectMapper goodsCollectMapper;

	public List<PageData> selectByUserId(PageData pd) {
		return goodsCollectMapper.selectByUserId(pd);
	}

	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return goodsCollectMapper;
	}

	/**
	 * 收藏列表(商品，课程)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> collectList(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("当前页不能为空");
		}
		page.setApp(true);
		page.setCurrentPage(pd.getAsInt("currentPage"));
		page.setShowCount(15);
		page.setPd(pd);
		return goodsCollectMapper.selectAllByIdPage(page);
	}
	
	/**
	 * 商品收藏 和课程收藏
	 * collect_type 1查询商品收藏
	 * collect_type 2课程收藏
	 */
	public List<PageData> selectCollect(PageData pd){
		Page page = new Page();
		page.setPd(pd);
		return goodsCollectMapper.selectCollect(page);
	}
	
	/**
	 * 商品收藏 
	 * 1 收藏成功
	 * 0取消收藏
	 * 2已收藏
	 */
	public Integer updateCollect(PageData pd){
		Page page= new Page();
		page.setPd(pd);
		List<PageData> collectList = goodsCollectMapper.selectByMap(page);
		if (EAUtil.isEmpty(collectList) && pd.getAsString("is_goods_like").equals("1")) {
			pd.put("create_time", EADate.getCurrentTime());
			goodsCollectMapper.insert(pd);
			return 1;
		} else if (pd.getAsString("is_goods_like").equals("0")) {
			goodsCollectMapper.delete(page);
			return 0;
		} else {
			return 2;
		}
	}
	
	
}
