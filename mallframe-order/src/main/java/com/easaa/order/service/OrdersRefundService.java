package com.easaa.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsMapper;
import com.easaa.goods.entity.Stock;
import com.easaa.order.dao.OrderMapper;
import com.easaa.order.dao.OrdersGoodsMapper;
import com.easaa.order.dao.OrdersRefundMapper;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserPointsLogMapper;

@Service
public class OrdersRefundService extends EABaseService {
	@Autowired
	private OrdersRefundMapper ordersRefundMapper;
	@Autowired
	private UserAccountLogMapper userAccountLogMapper;

	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private OrdersGoodsMapper ordersGoodsMapper;

	@Autowired
	private GoodsMapper goodsMapper;
	
	@Autowired
	private UserPointsLogMapper userPointsLogMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	

	@Override
	public EADao getDao() {
		return ordersRefundMapper;
	}

	public void insertRefundLog(PageData pd) {
		ordersRefundMapper.insertRefundLog(pd);
	}

	public void insertRefund(PageData pd) {
		ordersRefundMapper.insert(pd);
	}

	public PageData selectRefund(Integer pd) {
		return ordersRefundMapper.selectRefund(pd);
	}

	// 用户退款
	public boolean updateReturnAccount(PageData returnPd) throws Exception {
		Page  page = new Page();
		//退回积分
		Integer returnPoint = returnPd.getAsInteger("returnPoint")==null?0:returnPd.getAsInteger("returnPoint");
		BigDecimal returnMoney = returnPd.getAsBigDecimal("returnMoney")==null?BigDecimal.valueOf(0):returnPd.getAsBigDecimal("returnMoney");
		//回收积分
		int recovery = Integer.parseInt(StringUtils.isNotEmpty(returnPd.getAsString("recovery"))?returnPd.getAsString("recovery"):"0");
		//用户退一次货得到的积分  可能是负数
		int difference = returnPoint-recovery;
		//更用户钱包  将返回的金额加入钱包
		page.setPd(returnPd);
		List<PageData> accountPds = userAccountMapper.selectByMap(page);
		if(accountPds != null && accountPds.size() > 1){
			throw new Exception("用户钱包异常");
		}
		if(accountPds == null || accountPds.size() == 0){
			//创建钱包
			PageData newAccount = new PageData();
			newAccount.put("user_id", returnPd.get("user_id"));
			newAccount.put("user_money", "0");
			newAccount.put("frozen_money", "0");
			newAccount.put("charge_money", "0");
			newAccount.put("withdraw_money", "0");
			newAccount.put("order_money", "0");
			newAccount.put("rebate_money", "0");
			newAccount.put("frozen_points", "0");
			newAccount.put("user_points", "0");
			newAccount.put("charge_points", "0");
			newAccount.put("use_points", "0");
			newAccount.put("order_points", "0");
			newAccount.put("rebate_points", "0");
			newAccount.put("share_points", "0");
			newAccount.put("change_time", "0");
			newAccount.put("tea_hoursubsidy", "0");
			newAccount.put("u_commission", "0");
			userAccountMapper.insert(newAccount);
			accountPds = userAccountMapper.selectByMap(page);
		}
		//查询用户钱包  将用户余额加上退回金额  将订单金额减去退款金额
		PageData accountPd = accountPds.get(0);
		if(EAUtil.isEmpty(returnPd.getAsString("reback_type")) || "0".equals(returnPd.getAsString("reback_type"))){// 0退钱包，1原路返回 不加上退回金额
			if(accountPd.getAsString("user_money")!= null){
				accountPd.put("user_money", accountPd.getAsBigDecimal("user_money").add(returnMoney));
			}else{
				accountPd.put("user_money",returnMoney);
			}
		}
		if(accountPd.getAsString("order_money") != null){
			BigDecimal orderMoney = accountPd.getAsBigDecimal("order_money").subtract(returnMoney);
			if(orderMoney.compareTo(BigDecimal.valueOf(0)) == -1){
				orderMoney = BigDecimal.valueOf(0);
			}
			accountPd.put("order_money",orderMoney);
		}else{
			accountPd.put("order_money",0);
		}
		int usePoints = Integer.parseInt(StringUtils.isNotEmpty(accountPd.getAsString("use_points"))?accountPd.getAsString("use_points"):"0");
		if(returnPoint > usePoints){
			usePoints = 0;
		}else{
			usePoints = usePoints - returnPoint;
		}
		accountPd.put("use_points",usePoints);
		int orderPoints = Integer.parseInt(StringUtils.isNotEmpty(accountPd.getAsString("order_points"))?accountPd.getAsString("order_points"):"0");
		if(recovery > orderPoints){
			orderPoints = 0;
		}else{
			orderPoints = orderPoints - recovery;
		}
		accountPd.put("order_points",orderPoints);
		int userPoints = Integer.parseInt(StringUtils.isNotEmpty(accountPd.getAsString("user_points"))?accountPd.getAsString("user_points"):"0");
		accountPd.put("use_points",userPoints + difference);
		accountPd.put("change_time", EADate.getCurrentTime());
		//更新钱包
		userAccountMapper.update(accountPd);
		//更新订单商品表里面的退款字段 如果该订单所有的商品都退款 更改订单状态为交易关闭
		PageData orderGoods = new PageData();
		orderGoods.put("order_goods_id", returnPd.getAsString("order_goods_id"));
		orderGoods.put("is_refund", 3);
		ordersGoodsMapper.update(orderGoods);
		/**
		 * 新加内容  判断订单的所有商品是否退款完成  如果退款完成更改订单状态为交易关闭
		 */
		boolean isAllRefund = true;
		List<PageData> orderItems = ordersGoodsMapper.selectByOrderId(EAString.stringToInt(returnPd.getAsString("order_id"), 0));
		for (PageData tempItem : orderItems) {
			if("2".equals(tempItem.getAsString("is_refund"))){
				isAllRefund = false;
				break;
			}
		}
		if(isAllRefund){
			orderGoods.clear();
			orderGoods.put("order_id", returnPd.getAsString("order_id"));
			orderGoods.put("order_status", "9");
			orderMapper.update(orderGoods);
		}
		//更新库存表库存或者更新商品表库存  待续==========================
		orderGoods.clear();
		orderGoods.put("goods_id", returnPd.getAsString("goods_id"));
		orderGoods.put("sku_id", returnPd.getAsString("sku_id"));
		Stock stock = goodsMapper.queryStockBySkuId(orderGoods);
		Integer goodsStock = 0;
		if(stock != null){
			goodsStock = Integer.parseInt(StringUtils.isNotEmpty(stock.getStock())?stock.getStock():"0")+Integer.parseInt(returnPd.getAsString("returnStore"));
			stock.setStock((goodsStock>0?goodsStock:0)+"");
			goodsMapper.updateStockByPid(stock);
		}
		PageData goodsPd = goodsMapper.selectById(returnPd.getAsInt("goods_id"));
		if(goodsPd != null){
			goodsStock = Integer.parseInt(StringUtils.isNotEmpty(goodsPd.getAsString("goods_stock"))?goodsPd.getAsString("goods_stock"):"0")+Integer.parseInt(returnPd.getAsString("returnStore"));
			goodsStock = goodsStock>0?goodsStock:0;
			goodsPd.put("goods_stock",goodsStock);
			goodsMapper.update(goodsPd);
		}
		//插入退货单  并且默认退货单已经完成
		PageData refundOrder = new PageData();
		refundOrder.put("refund_id", returnPd.get("refund_id"));
		refundOrder.put("refund_status", 3);
		refundOrder.put("creator", returnPd.getAsString("user_name"));
		refundOrder.put("refund_remark", "管理员"+returnPd.getAsString("user_name")+",同意了"+refundOrder.getAsString("goods_name")+"该商品的退款申请");
		ordersRefundMapper.update(refundOrder);
		//退款单日志
		PageData refundLog = new PageData();
		refundLog.put("refund_id", returnPd.get("refund_id"));
		refundLog.put("log_note", "退款状态：已退货。退款金额为:"+returnMoney+",退回积分为："+returnPoint+",系统回收积分为："+recovery+"。");
		refundLog.put("creator", returnPd.getAsString("user_name"));
		refundLog.put("create_time", EADate.getCurrentTime());
		ordersRefundMapper.insertRefundLog(refundLog);
		//钱包日志
		PageData logAccount = new PageData();
		logAccount.put("user_id", returnPd.getAsString("user_id"));
		logAccount.put("log_money", returnMoney);
		logAccount.put("log_points", difference);
		logAccount.put("log_type", "9");
		logAccount.put("log_symbol", "0");
		logAccount.put("log_time", EADate.getCurrentTime());
		logAccount.put("admin_name", returnPd.getAsString("user_name"));
		logAccount.put("log_order", returnPd.getAsString("order_sn"));
		logAccount.put("log_note", "退货："+"订单编号为"+returnPd.getAsString("order_sn")+"里商品名字为"+returnPd.getAsString("goods_name")+"执行了退货操作。退款金额为:"+returnMoney+",退回积分为："+returnPoint+",系统回收积分为："+recovery);
		userAccountLogMapper.insert(logAccount);
		//积分日志
		/**
		 * 更改钱包流水
		 */
		PageData payLog = new PageData();
		payLog.put("user_id", returnPd.getAsString("user_id"));
		payLog.put("log_pay_order", returnPd.getAsString("order_sn"));
		payLog.put("log_time", EADate.getCurrentTime());
		payLog.put("log_role", "1");
		payLog.put("log_points", recovery);
		payLog.put("log_expenditure", "1"); // 收入
		payLog.put("log_type", "8");
		payLog.put("log_note", "退货：："+"订单编号为"+ returnPd.getAsString("order_sn") + "里商品名字为"+returnPd.getAsString("goods_name")+"执行了退货操作。退回积分为："+returnPoint+",系统回收积分为："+recovery);
		userPointsLogMapper.insert(payLog);
		return true;
	}
	
	
	
}
