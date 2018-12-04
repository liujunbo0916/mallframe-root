package com.easaa.goods.entity.data;

import java.io.Serializable;
import java.util.List;

import com.easaa.goods.entity.Stock;
import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

/**
 * 返回前端的商品实体对象
 * @author liujunbo
 */
public class ProductData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.SYS);
	//商品id
	private String id;
	//商品名字
	private String goods_name;
	//商品代码
	private String goods_sn;
	//商品标签
    private String goods_tags;
    //售价
	private String shop_price;
	//市场价格
	private String market_price;
	//描述  一般列表接口无值
	private String goods_desc;
	//商品库存
	private String goods_stock;
	//列表图片
	private String list_img;
	//分类名称
	private String category_name;

	//商品摘要
	private String goods_summary;
	//品牌名称
	private String brand_name;
	//虚拟销售量
	private String virtual_sales;
	//库存信息 列表接口一般无值
	private String category1_id;//商品一级分类   1
	
	private String category2_id;//商品二级分类   0
	
	private String category3_id;//商品三级分类   0
	
	private String category_id;//商品分类   1
	private String category1_name;
	private String category2_name;
	private String category3_name;
	private String sku_id;
	private String bs_category_id;//本店分类 （多选1,2,3）0
	private String is_gift; // 是否礼物   默认0   
	private String is_virtual; // 是否虚拟商品  默认0 
	private String is_offer_shop; // 是否为平台供货商  默认0 
	private String is_own_shop; // 是否为平台自营  默认0 
	private String bar_code;//商品默认库存国际编码 0
	private String goods_freight;//商品运费    1
	private String goods_storage_alarm; // 库存报警值  0
	private String house_id;//发货仓  默认传0
	private String is_popular; //人气推荐
	private String is_special;//特别推荐
	private String click_count;//商品浏览量
	private Integer is_goods_like;//用户收藏标示
	
	private List<StockData> stocks;
	
	//商品属性集合
	private List<GoodsAttr> goodsAttr;
	
	//商品详情轮播图
	private List<Picture> pictures;
	//评论列表
	private List<CommentsData> comments;
	
	private Stock stock;//折扣上品库存
	
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public String getGoods_summary() {
		return goods_summary;
	}
	public void setGoods_summary(String goods_summary) {
		this.goods_summary = goods_summary;
	}
	public List<CommentsData> getComments() {
		return comments;
	}
	public void setComments(List<CommentsData> comments) {
		this.comments = comments;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_sn() {
		return goods_sn;
	}
	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}
	public String getGoods_tags() {
		return goods_tags;
	}
	public void setGoods_tags(String goods_tags) {
		this.goods_tags = goods_tags;
	}
	public String getShop_price() {
		return shop_price;
	}
	public void setShop_price(Float shop_price) {
		if(shop_price == null){
			shop_price = (float) 0.0;
		}
		this.shop_price = shop_price+"";
	}
	public String getMarket_price() {
		return market_price;
	}
	public void setMarket_price(Float market_price) {
		if(market_price == null){
			market_price = (float) 0.0;
		}
		this.market_price = market_price+"";
	}
	public String getGoods_stock() {
		return goods_stock;
	}
	public void setGoods_stock(String goods_stock) {
		this.goods_stock = goods_stock;
	}
	public String getList_img() {
		return list_img;
	}
	public void setList_img(String list_img) {
		if(!list_img.startsWith("http")){
			list_img = PROPERTIESHELPER.getValue("imageShowPath")+list_img;
		}
		this.list_img = list_img;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getVirtual_sales() {
		return virtual_sales;
	}
	public void setVirtual_sales(String virtual_sales) {
		this.virtual_sales = virtual_sales;
	}
	public String getGoods_desc() {
		return goods_desc;
	}
	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}
	
	public List<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	public List<GoodsAttr> getGoodsAttr() {
		return goodsAttr;
	}
	public void setGoodsAttr(List<GoodsAttr> goodsAttr) {
		this.goodsAttr = goodsAttr;
	}
	public String getSku_id() {
		return sku_id;
	}
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}
	public String getCategory1_id() {
		return category1_id;
	}
	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}
	public String getCategory2_id() {
		return category2_id;
	}
	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}
	public String getCategory3_id() {
		return category3_id;
	}
	public void setCategory3_id(String category3_id) {
		this.category3_id = category3_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}
	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}
	public String getBs_category_id() {
		return bs_category_id;
	}
	public void setBs_category_id(String bs_category_id) {
		this.bs_category_id = bs_category_id;
	}
	public String getIs_gift() {
		return is_gift;
	}
	public void setIs_gift(String is_gift) {
		this.is_gift = is_gift;
	}
	public String getIs_virtual() {
		return is_virtual;
	}
	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
	}
	public String getIs_offer_shop() {
		return is_offer_shop;
	}
	public void setIs_offer_shop(String is_offer_shop) {
		this.is_offer_shop = is_offer_shop;
	}
	public String getIs_own_shop() {
		return is_own_shop;
	}
	public void setIs_own_shop(String is_own_shop) {
		this.is_own_shop = is_own_shop;
	}
	public String getBar_code() {
		return bar_code;
	}
	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}
	public String getGoods_freight() {
		return goods_freight;
	}
	public void setGoods_freight(String goods_freight) {
		this.goods_freight = goods_freight;
	}
	public String getGoods_storage_alarm() {
		return goods_storage_alarm;
	}
	public void setGoods_storage_alarm(String goods_storage_alarm) {
		this.goods_storage_alarm = goods_storage_alarm;
	}
	public String getHouse_id() {
		return house_id;
	}
	public void setHouse_id(String house_id) {
		this.house_id = house_id;
	}
	public String getCategory1_name() {
		return category1_name;
	}
	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}
	public String getCategory2_name() {
		return category2_name;
	}
	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
	}
	public String getCategory3_name() {
		return category3_name;
	}
	public void setCategory3_name(String category3_name) {
		this.category3_name = category3_name;
	}
	public List<StockData> getStocks() {
		return stocks;
	}
	public void setStocks(List<StockData> stocks) {
		this.stocks = stocks;
	}
	public String getIs_popular() {
		return is_popular;
	}
	public void setIs_popular(String is_popular) {
		this.is_popular = is_popular;
	}
	public String getIs_special() {
		return is_special;
	}
	public void setIs_special(String is_special) {
		this.is_special = is_special;
	}
	public String getClick_count() {
		return click_count;
	}
	public void setClick_count(String click_count) {
		this.click_count = click_count;
	}
	public Integer getIs_goods_like() {
		return is_goods_like;
	}
	public void setIs_goods_like(Integer is_goods_like) {
		this.is_goods_like = is_goods_like;
	}
	
}
