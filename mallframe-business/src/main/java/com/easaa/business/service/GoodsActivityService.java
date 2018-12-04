package com.easaa.business.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.GoodsActivityMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

import freemarker.template.utility.DateUtil;

@Service
public class GoodsActivityService  extends EABaseService{

	@Autowired
	private GoodsActivityMapper goodsActivityMapper;
	
	@Override
	public EADao getDao() {
		return goodsActivityMapper;
	}
	public PageData payCallBack(PageData pd) throws Exception{
		pd.put("order_no", pd.getAsString("out_trade_no"));
		PageData order = goodsActivityMapper.selectActivityOrderByOrderNo(pd);
		if(order == null){
			throw new Exception("订单不存在");
		}
		String buyMonth = order.getAsString("buy_month");
		PageData bsInfo = goodsActivityMapper.selectBsInfoByBsId(pd);
		PageData bsInfoParam = new PageData();
		bsInfoParam.put("ac_id", order.getAsString("activity_id"));
		bsInfoParam.put("bs_id", order.getAsString("bs_id"));
		bsInfoParam.put("buy_money", order.getAsString("pay_money"));
		bsInfoParam.put("buy_time", EADate.getCurrentTime());
		Calendar calendar = Calendar.getInstance();
		if(bsInfo == null){
			//说明是第一次购买
			bsInfoParam.put("begin_buy_time", EADate.getCurrentTime());
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, EAString.stringToInt(buyMonth, 0));
			bsInfoParam.put("end_buy_time", EADate.date2Str(calendar.getTime()));
			goodsActivityMapper.insertBsInfo(bsInfoParam);
		}else{
			String endBuyTime = bsInfo.getAsString("end_buy_time");
			calendar.setTime(EADate.stringToDate(endBuyTime));
			calendar.add(Calendar.MONTH, EAString.stringToInt(buyMonth, 0));
			bsInfoParam.put("end_buy_time", EADate.date2Str(calendar.getTime()));
			goodsActivityMapper.updateBsInfo(bsInfoParam);
		}
		/**
		 * 更新订单状态
		 * 
		 */
		order.put("pay_status", 1);
		goodsActivityMapper.update(order);
		return null;
	}
	public List<PageData> activityOrderByPage(Page page){
		return goodsActivityMapper.activityOrderByPage(page);
	}
	public void updateActivityType(PageData pd){
		goodsActivityMapper.updateActivityType(pd);
	}
	public List<PageData> activityType(PageData pd){
		return goodsActivityMapper.activityType(pd);
	}
	
	public List<PageData> priceList(PageData pd){
		return goodsActivityMapper.priceList(pd);
	}
	public void insertPrice(PageData pd){
		goodsActivityMapper.insertPrice(pd);
	}
	public void updatePrice(PageData pd){
		goodsActivityMapper.updatePrice(pd);
	}
	
	public PageData selectBsInfoByBsId(PageData pd){
		return goodsActivityMapper.selectBsInfoByBsId(pd);
	}
	
	/**
	 * 查询平台活动 以及商家开通情况
	 * @param pd
	 * @return
	 */
	public List<PageData> selectActivityByBusi(PageData pd){
		List<PageData> result = goodsActivityMapper.selectActivityByBusi(pd);
		for(PageData tempResult : result){
			String endTime = tempResult.getAsString("end_buy_time");
			tempResult.put("isOpen", false);
			if(StringUtils.isNotEmpty(endTime)){
				if(EADate.stringToDate(endTime, "yyyy-MM-dd HH:mm:ss").getTime() > new Date().getTime()){
					//商家开通了此活动
					tempResult.put("isOpen", true);
				}
			}
		}
		return result;
	}
}
