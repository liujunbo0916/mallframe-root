package com.easaa.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessDecorationMapper;
import com.easaa.business.entity.DecorationGoods;
import com.easaa.business.entity.ReceiveDecoration;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.FtpUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 店铺装修service
 * @author 
 */
@Service
public class BusinessDecorationService  extends EABaseService{

	@Autowired
	private BusinessDecorationMapper businessDecorationMapper;
	
	@Override
	public EADao getDao() {
		return businessDecorationMapper;
	}
	/**
	 * 获取商家店铺装修模板列表
	 * 
	 * @return
	 */
	public List<ReceiveDecoration> selectDecorationBlocklist(PageData pd) throws Exception {
		if (pd.getAsString("bs_id").isEmpty()) {
			throw new Exception("店铺ID不能为空！");
		}
		return businessDecorationMapper.selectByDbMap(pd);
	}

	/**
	 * 获取平台装修模板列表
	 * 
	 * @return
	 */
	public List<PageData> selectDecorationlist(PageData pd) throws Exception {
		Page page = new Page();
		page.setPd(pd);
		return businessDecorationMapper.selectByMap(page);
	}
	
	public PageData selectDecorationByOne(PageData pd) throws Exception {
		Page page = new Page();
		page.setPd(pd);
		List<PageData> list=businessDecorationMapper.selectByMap(page);
		if(EAUtil.isNotEmpty(list) && list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 修改店铺装修
	 * @param pd
	 */
	public void insertDecorationBlock(List<ReceiveDecoration> pd) throws Exception {

		for (ReceiveDecoration receiveDecoration : pd) {
			if (receiveDecoration.getDecoration_code().isEmpty()) {
				throw new Exception("装修模板Code不能为空！");
			}
			if (receiveDecoration.getBs_id().isEmpty()) {
				throw new Exception("店铺ID不能为空！");
			}
			if (receiveDecoration.getSort().isEmpty()) {
				throw new Exception("装修模板排序不能为空！");
			}
			PageData blockpd = new PageData();
			blockpd.put("db_id", receiveDecoration.getBlock_id());

			businessDecorationMapper.deleteDecorationBlock(receiveDecoration);
			businessDecorationMapper.deleteDecorationGoods(blockpd);

			// 插入
			blockpd.clear();
			blockpd.put("decoration_code", receiveDecoration.getDecoration_code());
			blockpd.put("bs_id", receiveDecoration.getBs_id());
			blockpd.put("creater_name", EADate.getCurrentTime());
			blockpd.put("sort", receiveDecoration.getSort());
			businessDecorationMapper.insertDecorationBlock(blockpd);
			String dbID = blockpd.getAsString("block_id");

			blockpd.clear();
			List<DecorationGoods> goods = (List<DecorationGoods>) receiveDecoration.getGoods();
			if (EAUtil.isEmpty(goods)) {
				throw new Exception("关联商品不能空");
			}
			for (DecorationGoods decorationGoods : goods) {
				if (EAUtil.isEmpty(decorationGoods.getId())) {
					throw new Exception("关联商品ID不能空");
				}
				if (EAUtil.isEmpty(decorationGoods.getT_id())) {
					throw new Exception("装修模板关联ID不能为空！");
				}
				blockpd.put("db_id", dbID);
				blockpd.put("goods_id", decorationGoods.getId());
				blockpd.put("update_time", EADate.getCurrentTime());
				blockpd.put("main_title", decorationGoods.getMain_title());
				blockpd.put("sub_title", decorationGoods.getSub_title());
				blockpd.put("t_id", decorationGoods.getT_id());
				sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
				try {
					byte[] bt = decoder.decodeBuffer(decorationGoods.getImg_url());
					if (bt != null && bt.length > 0 && !decorationGoods.getImg_url().startsWith("http")) {
						String blnesavePath = FtpUtil.upload(bt, "mallframe", "sellerGoods",
								EADate.getCurrentTimeAsNumber() + EAString.getRandomString(5) + ".jpg");
						blockpd.put("img_url", blnesavePath);
					} else {
						decorationGoods.setMisImg_url(decorationGoods.getImg_url());
						blockpd.put("img_url", decorationGoods.getImg_url());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				businessDecorationMapper.insertDecorationGoods(blockpd);
			}
		}
	}
	
	public List<PageData> selectDGByMap(Integer dbid){
		PageData pd= new PageData();
		pd.put("db_id", dbid);
		return businessDecorationMapper.selectByDgMap(pd);
	}
	/**
	 * 应用设置的模板
	 * [{"decoration_code":"style_temp_tc1-1","sort":99,"goods":
	 * [{"goods_id":"230","img_url":"/business/propaganda/2017/11/16/1510829406449JLwihuf.jpg","temp_id":"2"},
	 * {"goods_id":"289","img_url":"/business/propaganda/2017/11/16/1510829406494LWv2mSI.jpg","temp_id":"3"}]
	 * }
	 * ]
	 * @param paramStr
	 */
	public void addtpl(PageData paramStr) {
		//删除商家以前的模板
		businessDecorationMapper.cascadeTplByBsid(paramStr);
		JSONArray jsonArray = JSONArray.fromObject(paramStr.getAsString("paramStr"));
		for(int i = 0 ; i < jsonArray.size() ; i++){
			JSONObject decorationObj = jsonArray.getJSONObject(i);
			PageData decorationPd = new PageData();
			decorationPd.put("decoration_code", decorationObj.getString("decoration_code"));
			decorationPd.put("sort", decorationObj.getString("sort"));
			decorationPd.put("create_time", EADate.getCurrentTime());
			decorationPd.put("update_time", EADate.getCurrentTime());
			decorationPd.put("bs_id", paramStr.getAsString("bs_id"));
			businessDecorationMapper.insertDecorationBlock(decorationPd);
			/**
			 * 插入模块商品数据
			 */
			JSONArray goodsAry = decorationObj.getJSONArray("goods");
			for(int j = 0 ; j < goodsAry.size() ; j++){
				PageData goodsPd = new PageData();
				JSONObject goodsObj = goodsAry.getJSONObject(j);
				goodsPd.put("db_id", decorationPd.getAsString("block_id"));
				goodsPd.put("goods_id", goodsObj.getString("goods_id"));
				goodsPd.put("img_url", goodsObj.getString("img_url"));
				goodsPd.put("t_id", goodsObj.getString("temp_id"));
				goodsPd.put("caeate_time", EADate.getCurrentTime());
				if(goodsObj.get("main_title") != null){
					goodsPd.put("main_title", goodsObj.getString("main_title"));
				}
				if(goodsObj.get("sub_title") != null){
					goodsPd.put("sub_title", goodsObj.getString("sub_title"));
				}
				businessDecorationMapper.insertDecorationGoods(goodsPd);
			}
		}
	}
}
