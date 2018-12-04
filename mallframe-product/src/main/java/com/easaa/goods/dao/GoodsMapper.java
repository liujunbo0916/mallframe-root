package com.easaa.goods.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.entity.Stock;
import com.easaa.goods.entity.data.PageExtend;
import com.easaa.goods.entity.data.ProductData;

public interface GoodsMapper extends EADao {

	public List<PageData> selectBySkuIds(PageData pd);

	public List<PageData> selectAllByCartids(PageData pd);

	public List<PageData> selectGoodsisFreight(PageData pd);

	public List<PageData> selectByIds(PageData pd);

	public List<PageData> selectByIdsStr(PageData pd);

	public List<PageData> selectGoodsPage(Page page);

	public List<PageData> categoryGoodsList(Page page);

	public PageData goodsIdCId(PageData pd);

	public int initStockByProductId(Stock st);

	public List<Stock> findStockByProductId(PageData pd);

	public List<Stock> findStockNoDefault(PageData pd);

	public Stock findStockByAttr(PageData pd);

	public List<Stock> queryStockByGoodsId(PageData pd);

	public Stock queryStockBySkuId(PageData pd);
	
	public List<PageData> queryStockAllPage(Page pd);
	
	public int updateStockByPid(Stock stock);

	public int clearStockByGoodsId(PageData pd);

	public void deleteStockByGoodsId(PageData pd);

	public List<PageData> selectTopGoods();

	public List<PageData> selectGoodsHeat(Page pg);

	public List<PageData> selectGoodsLikes(Page pg);

	public List<PageData> selectGoodsLikeNo(Page pg);

	public List<PageData> goodsDiscounts(Page pg);

	public List<PageData> goodsRelationCourse(PageData pd);

	public List<PageData> selectPriceList();

	public List<PageData> selectGoodsSetting();
	
	public List<PageData> selectMemberPackages();
	
	public void updateGoodsSetting(PageData pd);

	public void updateDefaultStore(PageData pd);

	public List<PageData> selectByBandId(Integer pd);
	
	public PageData selectSellerById(Integer pd);
	
	public PageData selectByGoodsId(Integer pd);
	/**
	 * 商品复制
	 */
	public void copyGoods(PageData pd);
	
	public ProductData selectItemEntity(PageData pd);
	
	/**
	 * pc端商品查询
	 */
	public List<PageData> selectByPcPage(PageExtend pd);
	
	
	
}
