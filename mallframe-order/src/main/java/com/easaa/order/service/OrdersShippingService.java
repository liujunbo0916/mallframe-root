package com.easaa.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easaa.entity.TrackEntity;
import com.easaa.entity.TrackMsgEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsMapper;
import com.easaa.order.dao.BusinessOrderMapper;
import com.easaa.order.dao.OrderMapper;
import com.easaa.order.dao.OrdersShippingMapper;
import com.easaa.upm.dao.LogisticsMapper;
import com.easaa.user.dao.UserAddressMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class OrdersShippingService extends EABaseService{
	@Autowired
	private OrdersShippingMapper ordersShippingMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private BusinessOrderMapper businessOrderMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	
	@Autowired
	private UserAddressMapper userAddressMapper;
	
	
	@Autowired
	private LogisticsMapper logisticsMapper;
	
	@Override
	public EADao getDao() {
		return ordersShippingMapper;
	}
	
	public List<PageData> getAll(Page pd){
		return ordersShippingMapper.selectAll(pd);
	}
	
	/**
	 * 添加发货单
	 * */
	public int create(PageData model,PageData pd) {
		model.put("order_id", pd.getAsString("order_id"));
		model.put("order_sn", pd.getAsString("order_sn"));
		if(StringUtils.isNotEmpty(model.getAsString("ex_name"))){
			model.put("ex_name", model.getAsString("ex_name"));
		}else{
			model.put("ex_name", pd.getAsString("shipping_type"));
		}
		if(StringUtils.isNotEmpty(model.getAsString("ex_money"))){
			model.put("ex_money", model.getAsString("ex_money"));
		}else{
			model.put("ex_money", pd.getAsString("shipping_money"));
		}
		model.put("create_time", EADate.getCurrentTimestamp());
		/*model.put("ex_content", pd.getAsString("ex_content"));*/
		model.put("shipping_time", EADate.getCurrentTimestamp());
		model.put("conphone", pd.getAsString("contact_phone"));
		model.put("conperson", pd.getAsString("contact_name"));
		model.put("address", "["+pd.getAsString("province")+pd.getAsString("city")+pd.getAsString("area")+"]"+pd.getAsString("address"));
		PageData track = new PageData();
		track.put("order_id", pd.getAsString("order_id"));
		track.put("accept_time", EADate.getCurrentTime());
		track.put("accept_station","商家已发货交由快递公司");
		ordersShippingMapper.insertTrack(track);
		return getDao().insert(model);
	}
	
	/**
	 * 快递查询
	 * @throws Exception 
	 */
	public TrackMsgEntity queryExpressStatus(PageData pd) throws Exception{
		/*Page page = new Page();
		if(StringUtils.isEmpty(pd.getAsString("order_id")))
			throw new Exception("订单号不能为空");
		//查询订单
		PageData order = businessOrderMapper.selectById(EAString.stringToInt(pd.getAsString("order_id"), 0));
		
		if(StringUtils.isEmpty(order.getAsString("order_status")) || 
				(!"4".equalsIgnoreCase(order.getAsString("order_status")) && !"5".equalsIgnoreCase(order.getAsString("order_status")) &&  !"6".equalsIgnoreCase(order.getAsString("order_status")))){
			throw new Exception("该订单不支持物流查询");
		}
		if(StringUtils.isEmpty(order.getAsString("shipping_status")) || 
				(!"4".equalsIgnoreCase(order.getAsString("shipping_status"))&& !"5".equalsIgnoreCase(order.getAsString("shipping_status"))
						&& !"6".equalsIgnoreCase(order.getAsString("shipping_status")))){
			throw new Exception("该订单不支持物流查询");
		}
		//查询发货单
		page.setPd(pd);
		List<PageData> shippingPd = ordersShippingMapper.selectByMap(page);
		if(shippingPd == null || shippingPd.size() == 0){
			throw new Exception("并未查到发货信息");
		}
		if(shippingPd.size() > 1){
			throw new Exception("系统存在多张发货单请联系管理员");
		}
		KdniaoTrackQueryAPI   kdNiao = new KdniaoTrackQueryAPI(shippingPd.get(0).getAsString("ex_code"),shippingPd.get(0).getAsString("bill_no"));
		TrackMsgEntity tracks =  kdNiao.getOrderTracesByJson();
		List<PageData> systemTrackPd = ordersShippingMapper.selectTrackByOrderId(pd);
		List<TrackEntity> systemTrack = new ArrayList<>();
		for(PageData tempPd :systemTrackPd){
			systemTrack.add(new TrackEntity(tempPd.getAsString("accept_time")
					,tempPd.getAsString("accept_station"),
					tempPd.getAsString("remark")));
		}
		tracks.Traces.addAll(systemTrack);
		//查询时间
//		Collections.sort(tracks.Traces, new Comparator<TrackEntity>(){  
//	            public int compare(TrackEntity o1, TrackEntity o2) {  
//	                //按照学生的年龄进行升序排列  
//	            	long o1Long = Long.valueOf(o1.getAcceptTime().replaceAll("[-\\s:]",""));
//	            	long o2Long = Long.valueOf(o2.getAcceptTime().replaceAll("[-\\s:]",""));
//	                if(o1Long < o2Long){  
//	                    return 1;  
//	                }  
//	                return -1;  
//	            }  
//	        });  
		//查询系统
		return tracks;*/
		return null;
	}

	 
	/**
	 * 计算订单运费
	 * 
	 * {address_id:12,bsInfo:[{'bs_id':1,'pinfo':[{"p_id":123,"number":3},{"goods_id":123,"number":3}]},{'bs_id':2,'pinfo':[{"p_id":123,"number":3},{"p_id":123,"number":3}]}]}
	 * 
	 * {bs_id:}
	 * 
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> calculatedFreight(String jsonStr) throws Exception{
		
		List<PageData> result = new ArrayList<PageData>();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		String addressId = jsonObject.getString("address_id");
		/**
		 * 查询地址
		 */
		PageData address = userAddressMapper.selectById(EAString.stringToInt(addressId, 0));
		if(address == null){
			throw new Exception("地址不存在");
		}
		String cityId = address.getAsString("city_id");
		if(StringUtils.isEmpty(cityId)){
			throw new Exception("地址不合法请重新选择地址");
		}
		JSONArray bsInfos = jsonObject.getJSONArray("bsInfo");
		for(int i = 0 ; i < bsInfos.size() ; i++){
			JSONObject bsInfo = bsInfos.getJSONObject(i);
			String bsId = bsInfo.getString("bs_id");
			JSONArray productInfos = bsInfo.getJSONArray("pinfo");
			Map<String,String> productMapNum = new HashMap<String,String>();
			StringBuffer productIds = new StringBuffer("(");
			for(int k = 0 ; k < productInfos.size() ; k++){
				productMapNum.put(productInfos.getJSONObject(k).getString("p_id"), productInfos.getJSONObject(k).getString("number"));
				productIds.append(productInfos.getJSONObject(k).getString("p_id")+",");
			}
			PageData selectPd = new PageData();
			selectPd.put("goodsIdsStr", productIds.substring(0, productIds.length()-1)+")");
			BigDecimal bsFreight = BigDecimal.valueOf(0);
			List<PageData> goodsList = goodsMapper.selectByIdsStr(selectPd);
			for(PageData goods : goodsList){
				if("0".equals(goods.getAsString("is_free_shipping"))){ //如果不免运费
					if("1".equals(goods.getAsString("is_freight_tpl"))){
						//使用费用模板
						if(StringUtils.isNotEmpty(goods.getAsString("freight_tpl_id"))){
							//如果模板id存在
							selectPd.clear();
							selectPd.put("cityId", cityId);
							selectPd.put("tpl_id", goods.getAsString("freight_tpl_id"));
							PageData useFee = null;
							try{
								useFee = logisticsMapper.selectUseFee(selectPd);
								if(useFee == null){
									useFee = logisticsMapper.selectDefaultFee(selectPd);
								} 
								bsFreight = bsFreight.add(calSingleFee(EAString.stringToInt(productMapNum.get(goods.getAsString("goods_id")), 0), useFee));
							}catch(Exception e){
							}
						}
					}else{
						//商品自定义运费
						bsFreight = bsFreight.add(BigDecimal.valueOf(StringUtils.isEmpty(goods.getAsString("goods_freight"))?0:EAString.stringToInt(goods.getAsString("goods_freight"), 0)));
					}
				}
			}
			//将计算结果放进 结果集
			PageData resultItem = new PageData();
			resultItem.put("bs_id", bsId);
			resultItem.put("freight", bsFreight);
			result.add(resultItem);
		}
		return result;
	}
	
	public BigDecimal calSingleFee(Integer goodsNumber,PageData useFee){
		int firstNum = useFee.getAsInt("first_num");
		BigDecimal calMoney = BigDecimal.valueOf(0);
		int different = goodsNumber - firstNum;
		if(different <= 0){
			calMoney = useFee.getAsBigDecimal("first_fee");
		}else{
			calMoney.add(useFee.getAsBigDecimal("first_fee"));
			int followNum = useFee.getAsInt("follow_num");
			if(different % followNum != 0){
				different = different/followNum + 1;
			}else{
				different = different/followNum;
			}
			calMoney = calMoney.add(BigDecimal.valueOf(different).multiply(useFee.getAsBigDecimal("follow_fee")));
		}
		return calMoney;
	}
}