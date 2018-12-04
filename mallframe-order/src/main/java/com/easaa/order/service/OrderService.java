package com.easaa.order.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.CouponMapper;
import com.easaa.goods.dao.GoodsCartMapper;
import com.easaa.goods.dao.GoodsCommentMapper;
import com.easaa.goods.dao.GoodsMapper;
import com.easaa.goods.entity.Stock;
import com.easaa.order.dao.BusinessOrderMapper;
import com.easaa.order.dao.GoodsActivityCompoMapper;
import com.easaa.order.dao.GoodsActivitySeckillMapper;
import com.easaa.order.dao.GoodsActivityTimelimitMapper;
import com.easaa.order.dao.OrderMapper;
import com.easaa.order.dao.OrdersGoodsMapper;
import com.easaa.order.dao.OrdersRefundMapper;
import com.easaa.order.dao.OrdersShippingMapper;
import com.easaa.order.entity.BusinessOrder;
import com.easaa.order.entity.BusinessOrderGoods;
import com.easaa.order.entity.Compo;
import com.easaa.order.entity.CompoPro;
import com.easaa.upm.dao.LogisticsMapper;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserAddressMapper;
import com.easaa.user.dao.UserMapper;
import com.easaa.user.dao.UserPointsLogMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class OrderService extends EABaseService {
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrdersShippingMapper ordersShippingMapper;

	@Autowired
	private UserAddressMapper userAddressMapper;

	@Autowired
	private LogisticsMapper logisticsMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsCartMapper goodsCartMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OrdersGoodsMapper ordersGoodsMapper;
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private CouponMapper couponMapper;

	@Autowired
	private OrdersRefundMapper ordersRefundMapper;
	@Autowired
	private UserAccountLogMapper userAccountLogMapper;

	@Autowired
	private UserPointsLogMapper userPointsLogMapper;

	@Autowired
	private BusinessOrderMapper businessOrderMapper;

	@Autowired
	private BusinessMapper businessMapper;
	/**
	 * 限时秒杀活动
	 */
	@Autowired
	private GoodsActivitySeckillMapper goodsActivitySeckillMapper;
	/**
	 * 商品打包活动
	 * 
	 */
	@Autowired
	private GoodsActivityCompoMapper goodsActivityCompoMapper;
	/**
	 * 限时折扣活动
	 */
	@Autowired
	private GoodsActivityTimelimitMapper goodsActivityTimelimitMapper;

	@Autowired
	private OrdersShippingService ordersShippingService;

	@Autowired
	public EADao getDao() {
		return orderMapper;
	}

	/**
	 * 查询订单列表(旧 作废 勿删)
	 */
	public List<PageData> getByPage(Page parameter) {
		return orderMapper.selectByPage(parameter);
	}

	/**
	 * 查询订单列表
	 */
	public List<PageData> getBSByPage(Page parameter) {
		return businessOrderMapper.selectListByPage(parameter);
	}

	/**
	 * 查询订单列表byID
	 */
	public PageData getBSById(Integer parameter) {
		return businessOrderMapper.selectById(parameter);
	}

	/**
	 * 查询订单数量使用
	 */
	public List<PageData> getOrderCounts(PageData parameter) {
		return orderMapper.selectOrderCounts(parameter);
	}

	/**
	 * 查询新订单数量使用
	 */
	public List<PageData> getBSOrderCounts(PageData parameter) {
		return businessOrderMapper.selectOrderCounts(parameter);
	}

	/**
	 * 新增订单
	 * 
	 * @throws Exception
	 */
	public int addOrder(PageData model) throws Exception {
		double order_money = 0;// 订单总金额
		double received_money = 0;// 公司实收金额
		double goods_money = model.getAsDouble("goods_money");
		model.put("order_sn", EAString.getFourSn());
		model.put("rebate_money", "0");
		model.put("shipping_fee", model.getAsString("shipping_money"));
		model.put("pay_fee", "0");
		model.put("shipping_status", "0");
		model.put("shipping_type", model.getAsString("shipping_type"));
		model.put("rebate_status", "未返利");
		model.put("create_time", EADate.getCurrentTime());
		model.put("mer_invoice", "0");
		// 订单新增类型 1 合并支付，order_money传递进来 其他计算
		if (!"1".equals(model.getAsString("aType"))) {
			order_money = goods_money + model.getAsDouble("shipping_money");
			received_money = goods_money - model.getAsDouble("mer_invoice") - model.getAsDouble("shipping_fee")
					- model.getAsDouble("pay_fee") - model.getAsDouble("pay_by_points");
			model.put("order_money", order_money);
		}
		model.put("received_money", received_money);
		return orderMapper.insert(model);
	}

	/**
	 * 修改订单状态 logType状态代表值: pay 设为付款（1 1 1） nopay 设为未付款（0 0 0） confirmOrder
	 * 确定订单（2 2 1） finishCargo 配货完成（3 3 1） goCargo 去发货（4 4 1） cancelCargo 取消发货（4
	 * 4 1） endDelivery 设为已送达（5 5 1） signFor 签收（6 6 1） refundIn 退款中（8 8 4）
	 * refundFinish 退款完成（8 8 3） cancelOrder 取消订单（7 7 2）
	 * 
	 * 支付状态：0为待付款，1为已付款，2为已取消，3为已退款，4为退款中
	 * 
	 * 配送状态：0为待付款，1为待配货，2为配货中，3为待发货，4为已发货，5为已收货 6为已签收，7为已取消，8为已退货
	 * 
	 * 订单状态：0为待付款，1为待确认，2为配货中，3为待发货，4为已发货，5为已送达 6为交易成功，7已取消，8为已退货
	 */
	public int edits(PageData model) {
		PageData track = new PageData();
		track.put("order_id", model.getAsString("order_id"));
		String logType = model.get("logType").toString();
		if ("pay".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "1");
			model.put("order_status", "1");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "等待商家确认");
			ordersShippingMapper.insertTrack(track);
		}
		if ("nopay".equals(logType)) {
			model.put("pay_status", "0");
			model.put("shipping_status", "0");
			model.put("order_status", "0");
		}
		if ("confirmOrder".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "2");
			model.put("order_status", "2");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "订单已确认，配货中");
			ordersShippingMapper.insertTrack(track);
		}
		if ("finishCargo".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "3");
			model.put("order_status", "3");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "商家配货完成，等待发货");
			ordersShippingMapper.insertTrack(track);
		}
		if ("goCargo".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "4");
			model.put("order_status", "4");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "商家已发货交由快递公司");
			ordersShippingMapper.insertTrack(track);
		}
		if ("cancelCargo".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "3");
			model.put("order_status", "3");
			// 删除发货单
			Page page = new Page();
			PageData delShipPd = new PageData();
			delShipPd.put("order_id", model.getAsString("order_id"));
			page.setPd(delShipPd);
			ordersShippingMapper.delete(page);
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "商家取消发货");
			ordersShippingMapper.insertTrack(track);
			// 将卖家快递费用更新成0
			track.put("shipping_fee", "0");
			orderMapper.update(track);
		}
		if ("confirmReceipt".equals(logType)) {// 确定收货
			model.put("pay_status", "1");
			model.put("shipping_status", "6");
			model.put("order_status", "6");
		}
		if ("endDelivery".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "5");
			model.put("order_status", "5");
		}
		if ("signFor".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "6");
			model.put("order_status", "5");
		}
		/*
		 * if("refundIn".equals(logType)){ model.put("pay_status", "4");
		 * model.put("shipping_status","8"); model.put("order_status","8"); }
		 */
		if ("refundFinish".equals(logType)) {
			model.put("pay_status", "3");
			model.put("shipping_status", "8");
			model.put("order_status", "8");
		}
		if ("cancelOrder".equals(logType)) {
			model.put("pay_status", "2");
			model.put("shipping_status", "7");
			model.put("order_status", "7");
		}
		return getDao().update(model);
	}

	/**
	 * 修改订单状态 logType状态代表值: pay 设为付款（1 1 1） nopay 设为未付款（0 0 0） confirmOrder
	 * 确定订单（2 2 1） finishCargo 配货完成（3 3 1） goCargo 去发货（4 4 1） cancelCargo 取消发货（4
	 * 4 1） endDelivery 设为已送达（5 5 1） signFor 签收（6 6 1） refundIn 退款中（8 8 4）
	 * refundFinish 退款完成（8 8 3） cancelOrder 取消订单（7 7 2）
	 * 
	 * 支付状态：0为待付款，1为已付款，2为已取消，3为已退款，4为退款中
	 * 
	 * 配送状态：0为待付款，1为待配货，2为配货中，3为待发货，4为已发货，5为已收货 6为已签收，7为已取消，8为已退货
	 * 
	 * 订单状态：0为待付款，1为待确认，2为配货中，3为待发货，4为已发货，5为已送达 6为交易成功，7已取消，8为已退货
	 */
	public int editsBsOrder(PageData model) {
		PageData track = new PageData();
		track.put("order_id", model.getAsString("order_id"));
		String logType = model.get("logType").toString();
		if ("pay".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "1");
			model.put("order_status", "1");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "等待商家确认");
			ordersShippingMapper.insertTrack(track);
		}
		if ("nopay".equals(logType)) {
			model.put("pay_status", "0");
			model.put("shipping_status", "0");
			model.put("order_status", "0");
		}
		if ("confirmOrder".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "2");
			model.put("order_status", "2");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "订单已确认，配货中");
			ordersShippingMapper.insertTrack(track);
		}
		if ("finishCargo".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "3");
			model.put("order_status", "3");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "商家配货完成，等待发货");
			ordersShippingMapper.insertTrack(track);
		}
		if ("goCargo".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "4");
			model.put("order_status", "4");
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "商家已发货交由快递公司");
			ordersShippingMapper.insertTrack(track);
		}
		if ("cancelCargo".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "3");
			model.put("order_status", "3");
			// 删除发货单
			Page page = new Page();
			PageData delShipPd = new PageData();
			delShipPd.put("order_id", model.getAsString("order_id"));
			page.setPd(delShipPd);
			ordersShippingMapper.delete(page);
			track.put("accept_time", EADate.getCurrentTime());
			track.put("accept_station", "商家取消发货");
			ordersShippingMapper.insertTrack(track);
			// 将卖家快递费用更新成0
			track.put("shipping_fee", "0");
			orderMapper.update(track);
		}
		if ("confirmReceipt".equals(logType)) {// 确定收货
			model.put("pay_status", "1");
			model.put("shipping_status", "6");
			model.put("order_status", "6");
		}
		if ("endDelivery".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "5");
			model.put("order_status", "5");
			model.put("send_time", EADate.getCurrentTime());
		}
		if ("signFor".equals(logType)) {
			model.put("pay_status", "1");
			model.put("shipping_status", "6");
			model.put("order_status", "5");
			model.put("sign_time", EADate.getCurrentTime());
		}
		/*
		 * if("refundIn".equals(logType)){ model.put("pay_status", "4");
		 * model.put("shipping_status","8"); model.put("order_status","8"); }
		 */
		if ("refundFinish".equals(logType)) {
			model.put("pay_status", "3");
			model.put("shipping_status", "8");
			model.put("order_status", "8");
		}
		if ("cancelOrder".equals(logType)) {
			model.put("pay_status", "2");
			model.put("shipping_status", "7");
			model.put("order_status", "7");
		}
		return businessOrderMapper.update(model);
	}

	/**
	 * 查询历史订单列表
	 */
	public List<PageData> selectHistory(Page pd) {
		List<PageData> list = orderMapper.selectAppByPage(pd);
		List<PageData> goodlist = new ArrayList<PageData>();
		for (PageData pageData : list) {
			PageData data = new PageData();
			data.put("order_id", pageData.getAsString("order_id"));
			goodlist = orderMapper.selectOrderGoods(data);

			pageData.put("goodList", goodlist);
		}
		return list;
	}

	/**
	 * 发布圈子时查询完成订单的商品列表
	 */
	public List<PageData> selectbuygoods(Page pg) {
		List<PageData> list = orderMapper.selectAppByPage(pg);
		List<PageData> goodlist = new ArrayList<PageData>();
		for (PageData pageData : list) {
			PageData data = new PageData();
			data.put("order_id", pageData.getAsString("order_id"));
			goodlist.addAll(orderMapper.selectOrderGoods(data));
		}
		return goodlist;
	}

	/**
	 * 查询分销列表订单 分页
	 */
	public List<PageData> selectrebateOrder(Page pd) {
		List<PageData> list = orderMapper.selectRebateOrderByPage(pd);
		List<PageData> goodlist = new ArrayList<PageData>();
		for (PageData pageData : list) {
			PageData data = new PageData();
			data.put("order_id", pageData.getAsString("order_id"));
			goodlist = orderMapper.selectOrderGoods(data);
			pageData.put("goodList", goodlist);
			Integer counts = 0;
			for (PageData pageData2 : goodlist) {
				counts += pageData2.getAsInt("goods_count");
			}
			pageData.put("goods_counts", counts);
		}
		return list;
	}

	/**
	 * 查询分销列表订单 不分页
	 */
	public List<PageData> selectrebateOrder(PageData pd) {
		List<PageData> list = orderMapper.selectRebateOrder(pd);
		List<PageData> goodlist = null;
		PageData data = new PageData();
		for (PageData pageData : list) {
			data.put("order_id", pageData.getAsString("order_id"));
			goodlist = orderMapper.selectOrderGoods(data);
			pageData.put("goodList", goodlist);
			Integer counts = 0;
			for (PageData pageData2 : goodlist) {
				counts += pageData2.getAsInt("goods_count");
			}
			pageData.put("goods_counts", counts);
		}
		return list;
	}

	/**
	 * 查询订单对应的商品
	 */
	public List<PageData> selectOrderGoods(PageData pd) {
		return orderMapper.selectOrderGoods(pd);
	}

	/**
	 * 查询订单详情(订单详情页显示)
	 */
	public PageData selectDetails(PageData pd) {
		Page page = new Page();
		page.setPd(pd);
		PageData data = orderMapper.selectOrderDetails(pd);
		if (EAUtil.isNotEmpty(data)) {
			page = new Page();
			List<PageData> goodslist = orderMapper.selectDetailsGoodslist(pd);
			if (EAUtil.isNotEmpty(pd.getAsString("user_id"))) {
				for (PageData pageData : goodslist) {
					PageData coommentpd = new PageData();
					coommentpd.put("user_id", pd.getAsString("user_id"));
					coommentpd.put("goods_id", pageData.getAsString("goods_id"));
					page.setPd(coommentpd);
					List<PageData> list = goodsCommentMapper.selectByMap(page);
					if (list != null && list.size() > 0) {
						pageData.put("is_Comment", "1");
					} else {
						pageData.put("is_Comment", "0");
					}
				}
			}
			data.put("goodslist", goodslist);
		}
		return data;
	}

	/**
	 * 查询订单详情（其他地方用，不要删除）
	 */
	public List<PageData> selectdetails(PageData pd) {
		return orderMapper.selectDetails(pd);
	}

	/**
	 * 根据订单号查询订单
	 */

	public PageData selectByOrderSn(String orderSn) {
		return orderMapper.selectByOrderSn(orderSn);
	}

	/**
	 * 取消订单（APP接口 作废勿删）
	 */
	public boolean updateCancelOrder(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不传，无法进行取消");
		}
		if (EAUtil.isEmpty(pd.getAsString("orders_id"))) {
			throw new Exception("订单id不传，无法进行取消");
		}
		PageData order = orderMapper.selectById(pd.getAsInt("orders_id"));
		pd.put("order_id", pd.getAsInt("orders_id"));
		pd.put("pay_status", "2");
		pd.put("shipping_status", "7");
		pd.put("order_status", "7");
		if (EAUtil.isNotEmpty(order) && order.getAsInt("order_status") > 0 && order.getAsInt("order_status") < 4) {
			// 释放库存
			PageData skupd = new PageData();
			List<PageData> ordergoods = ordersGoodsMapper.selectByOrderId(pd.getAsInt("orders_id"));
			for (PageData pageData : ordergoods) {
				skupd.clear();
				skupd.put("sku_id", pageData.getAsString("goods_sku_id"));
				Stock stock = goodsMapper.queryStockBySkuId(skupd);
				int stockCount = EAString.stringToInt(stock.getStock(), 0) + pageData.getAsInt("goods_count");
				stock.setStock(String.valueOf(stockCount));
				goodsMapper.updateStockByPid(stock);
				skupd.clear();
				skupd.put("goods_id", pageData.getAsString("goods_id"));
				skupd.put("goods_stock", stockCount);
				goodsMapper.update(skupd);
			}
			// 积分支付 回退
			if (StringUtils.isNotEmpty(order.getAsString("user_pay_points"))
					&& order.getAsBigDecimal("user_pay_points").compareTo(new BigDecimal("0")) == 1) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldPoint.add(order.getAsBigDecimal("user_pay_points")));
				userAccountMapper.updatePoints(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_pay_order", order.getAsString("order_sn"));
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_role", "1");
				payLog.put("log_points", order.getAsString("user_pay_points"));
				payLog.put("log_expenditure", "1"); // 收入
				payLog.put("log_type", "6");
				payLog.put("log_note", "商品取消：" + order.getAsString("order_sn") + "使用回退"
						+ order.getAsString("order_give_points") + " 积分");
				userPointsLogMapper.insert(payLog);
			}
			// 赠送积分 回收
			if (StringUtils.isNotEmpty(order.getAsString("give_points"))
					&& order.getAsBigDecimal("give_points").compareTo(new BigDecimal("0")) == 1
					&& !order.getAsString("order_status").equals("0")) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldPoint.subtract(order.getAsBigDecimal("give_points")));
				userAccountMapper.updatePoints(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_pay_order", order.getAsString("order_sn"));
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_role", "1");
				payLog.put("log_points", order.getAsString("give_points"));
				payLog.put("log_expenditure", "0"); // 支出
				payLog.put("log_type", "5");
				payLog.put("log_note", "商品取消：订单 " + order.getAsString("order_sn") + " 赠送回收"
						+ order.getAsString("give_points") + " 积分");
				userPointsLogMapper.insert(payLog);
			}
			// 更改用户金额账户
			if (StringUtils.isNotEmpty(order.getAsString("pay_by_money"))
					&& order.getAsBigDecimal("pay_by_money").compareTo(new BigDecimal("0")) == 1
					&& !order.getAsString("order_status").equals("0")) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				BigDecimal orderMoney = userAccount.getAsBigDecimal("order_money");
				BigDecimal userMoney = userAccount.getAsBigDecimal("user_money");
				userAccount.put("order_money", orderMoney.subtract(order.getAsBigDecimal("pay_by_money")));
				userAccount.put("user_money", userMoney.add(order.getAsBigDecimal("pay_by_money")));
				userAccountMapper.update(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_money", order.getAsString("pay_by_money"));
				payLog.put("log_type", "2");
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_note", "商品取消");
				payLog.put("log_symbol", 0);
				payLog.put("log_order", order.getAsString("order_sn"));
				userAccountLogMapper.insert(payLog);
			}
			if (orderMapper.update(pd) > 0) {
				return true;
			} else {
				throw new Exception("未知原因，取消失败！");
			}

		} else {
			throw new Exception("订单状态有误，取消失败！");
		}
	}

	/**
	 * 取消订单（APP接口 ）
	 */
	public boolean updateCancelOrders(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不传，无法进行取消");
		}
		if (EAUtil.isEmpty(pd.getAsString("orders_id"))) {
			throw new Exception("订单id不传，无法进行取消");
		}
		BusinessOrder order = businessOrderMapper.selectEntityByID(pd.getAsInt("orders_id"));
		pd.put("id", pd.getAsInt("orders_id"));
		pd.put("pay_status", "2");
		pd.put("shipping_status", "7");
		pd.put("order_status", "7");
		if (EAUtil.isNotEmpty(order) && Integer.valueOf(order.getOrderStatus()) > 0
				&& Integer.valueOf(order.getOrderStatus()) < 4) {
			// 释放库存
			PageData skupd = new PageData();
			for (BusinessOrderGoods pageData : order.getOrderGoods()) {
				skupd.clear();
				skupd.put("sku_id", pageData.getSkuId());
				Stock stock = goodsMapper.queryStockBySkuId(skupd);
				int stockCount = EAString.stringToInt(stock.getStock(), 0) + Integer.valueOf(pageData.getGoodsCount());
				stock.setStock(String.valueOf(stockCount));
				goodsMapper.updateStockByPid(stock);
				skupd.clear();
				skupd.put("goods_id", pageData.getGoodsId());
				skupd.put("goods_stock", stockCount);
				goodsMapper.update(skupd);
			}
			// 积分支付 回退
			if (EAUtil.isNotEmpty(order.getUserPayPoints())
					&& order.getUserPayPoints().compareTo(new BigDecimal("0")) == 1) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldPoint.add(order.getUserPayPoints()));
				userAccountMapper.updatePoints(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_pay_order", order.getOrderSn());
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_role", "1");
				payLog.put("log_points", order.getUserPayPoints());
				payLog.put("log_expenditure", "1"); // 收入
				payLog.put("log_type", "6");
				payLog.put("log_note", "商品取消：" + order.getOrderSn() + "使用回退" + order.getUserPayPoints() + " 积分");
				userPointsLogMapper.insert(payLog);
			}
			// 赠送积分 回收
			if (EAUtil.isNotEmpty(order.getGivePoints()) && order.getGivePoints().compareTo(new BigDecimal("0")) == 1
					&& !order.getGivePoints().equals("0")) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldPoint.subtract(order.getGivePoints()));
				userAccountMapper.updatePoints(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_pay_order", order.getOrderSn());
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_role", "1");
				payLog.put("log_points", order.getGivePoints());
				payLog.put("log_expenditure", "0"); // 支出
				payLog.put("log_type", "5");
				payLog.put("log_note", "商品取消：订单 " + order.getOrderSn() + " 赠送回收" + order.getGivePoints() + " 积分");
				userPointsLogMapper.insert(payLog);
			}
			// 更改用户金额账户
			if (EAUtil.isNotEmpty(order.getPayByMoney()) && order.getPayByMoney().compareTo(new BigDecimal("0")) == 1
					&& !order.getOrderStatus().equals("0")) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				BigDecimal orderMoney = userAccount.getAsBigDecimal("order_money");
				BigDecimal userMoney = userAccount.getAsBigDecimal("user_money");
				userAccount.put("order_money", orderMoney.subtract(order.getPayByMoney()));
				userAccount.put("user_money", userMoney.add(order.getPayByMoney()));
				userAccountMapper.update(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_money", order.getPayByMoney());
				payLog.put("log_type", "2");
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_note", "商品取消");
				payLog.put("log_symbol", 0);
				payLog.put("log_order", order.getOrderSn());
				userAccountLogMapper.insert(payLog);
			}
			if (businessOrderMapper.update(pd) > 0) {
				return true;
			} else {
				throw new Exception("未知原因，取消失败！");
			}

		} else {
			throw new Exception("订单状态有误，取消失败！");
		}
	}

	/**
	 * 取消订单（WX）
	 */
	public boolean updatecancelOrder(PageData pd) throws Exception {
		PageData order = orderMapper.selectById(pd.getAsInt("order_id"));
		pd.put("pay_status", "2");
		pd.put("shipping_status", "7");
		pd.put("order_status", "7");
		if (EAUtil.isNotEmpty(order) && order.getAsInt("order_status") > 0 || order.getAsInt("order_status") < 4) {
			// 释放库存
			PageData skupd = new PageData();
			List<PageData> ordergoods = ordersGoodsMapper.selectByOrderId(pd.getAsInt("order_id"));
			for (PageData pageData : ordergoods) {
				skupd.clear();
				skupd.put("sku_id", pageData.getAsString("goods_sku_id"));
				Stock stock = goodsMapper.queryStockBySkuId(skupd);
				int stockCount = EAString.stringToInt(stock.getStock(), 0) + pageData.getAsInt("goods_count");
				stock.setStock(String.valueOf(stockCount));
				goodsMapper.updateStockByPid(stock);
				skupd.clear();
				skupd.put("goods_id", pageData.getAsString("goods_id"));
				skupd.put("goods_stock", stockCount);
				goodsMapper.update(skupd);
			}
			// 积分支付 回退
			if (StringUtils.isNotEmpty(order.getAsString("user_pay_points"))
					&& order.getAsBigDecimal("user_pay_points").compareTo(new BigDecimal("0")) == 1) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldPoint.add(order.getAsBigDecimal("user_pay_points")));
				userAccountMapper.updatePoints(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_pay_order", order.getAsString("order_sn"));
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_role", "1");
				payLog.put("log_points", order.getAsString("user_pay_points"));
				payLog.put("log_expenditure", "1"); // 收入
				payLog.put("log_type", "6");
				payLog.put("log_note", "商品取消：" + order.getAsString("order_sn") + "使用回退"
						+ order.getAsString("order_give_points") + " 积分");
				userPointsLogMapper.insert(payLog);
			}
			// 赠送积分 回收
			if (StringUtils.isNotEmpty(order.getAsString("give_points"))
					&& order.getAsBigDecimal("give_points").compareTo(new BigDecimal("0")) == 1
					&& !order.getAsString("order_status").equals("0")) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldPoint.subtract(order.getAsBigDecimal("give_points")));
				userAccountMapper.updatePoints(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_pay_order", order.getAsString("order_sn"));
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_role", "1");
				payLog.put("log_points", order.getAsString("give_points"));
				payLog.put("log_expenditure", "0"); // 支出
				payLog.put("log_type", "5");
				payLog.put("log_note", "商品取消：订单 " + order.getAsString("order_sn") + " 赠送回收"
						+ order.getAsString("give_points") + " 积分");
				userPointsLogMapper.insert(payLog);
			}
			// 更改用户金额账户
			if (StringUtils.isNotEmpty(order.getAsString("pay_by_money"))
					&& order.getAsBigDecimal("pay_by_money").compareTo(new BigDecimal("0")) == 1
					&& !order.getAsString("order_status").equals("0")) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				BigDecimal orderMoney = userAccount.getAsBigDecimal("order_money");
				BigDecimal userMoney = userAccount.getAsBigDecimal("user_money");
				userAccount.put("order_money", orderMoney.subtract(order.getAsBigDecimal("pay_by_money")));
				userAccount.put("user_money", userMoney.add(order.getAsBigDecimal("pay_by_money")));
				userAccountMapper.update(userAccount);
				/**
				 * 更改钱包流水
				 */
				PageData payLog = new PageData();
				payLog.put("user_id", pd.getAsString("user_id"));
				payLog.put("log_money", order.getAsString("pay_by_money"));
				payLog.put("log_type", "2");
				payLog.put("log_time", EADate.getCurrentTime());
				payLog.put("log_note", "商品取消");
				payLog.put("log_symbol", 0);
				payLog.put("log_order", order.getAsString("order_sn"));
				userAccountLogMapper.insert(payLog);
			}
			if (orderMapper.update(pd) > 0) {
				return true;
			} else {
				throw new Exception("未知原因，取消失败！");
			}
		} else {
			throw new Exception("订单已发货，取消失败！");
		}
	}

	/**
	 * 签收订单(作废 勿删)
	 */
	public int updateSignOrder(PageData pd) {
		pd.put("pay_status", "1");
		pd.put("shipping_status", "6");
		pd.put("order_status", "6");
		pd.put("sign_time", EADate.getCurrentTime());
		return orderMapper.update(pd);
	}

	/**
	 * 签收订单
	 */
	public int updateSignsOrder(PageData pd) {
		pd.put("pay_status", "1");
		pd.put("shipping_status", "6");
		pd.put("order_status", "6");
		pd.put("sign_time", EADate.getCurrentTime());
		return businessOrderMapper.update(pd);
	}

	public int updateBusOrder(PageData pd) {
		return businessOrderMapper.update(pd);
	}

	/**
	 * 逻辑删除订单（作废 勿删）
	 */
	public int deleteOrder(int order_id) {
		return orderMapper.deleteOrder(order_id);
	}

	/**
	 * 逻辑删除订单
	 */
	public int deleteOrders(int order_id) {
		return businessOrderMapper.deleteOrder(order_id);
	}

	/**
	 * 逻辑修改订单收货地址
	 */
	public int updateOrderAdress(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("order_id"))) {
			throw new Exception("订单id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不能为空");
		}
		pd.put("create_time", EADate.getCurrentTime());
		// 判断是否可以修改订单地址
		List<PageData> orderpd = orderMapper.selectOrderGoods(pd);
		if (EAUtil.isNotEmpty(orderpd) && orderpd.size() > 0) {
			if (orderpd.get(0).getAsInt("order_status") < 4) {
				return orderMapper.updateOrderAdress(pd);
			} else {
				throw new Exception("订单号已发货不能修改地址了");
			}
		} else {
			throw new Exception("订单号不存在");
		}
	}

	/**
	 * 解析数组嵌套JSON
	 */
	/**
	 * 计算运费 parameter goodsLists 待计算的订单商品列表
	 * 
	 * defaultAddress 默认地址id
	 * 
	 * @throws Exception
	 *             return freight 运费 weight 计费商品总重量 send_time 预计送达时间
	 */
	public PageData calculateFreight(List<PageData> goodsLists, Integer defaultAddress) throws Exception {
		PageData param = new PageData();
		BigDecimal freight = BigDecimal.valueOf(0);
		BigDecimal weight = BigDecimal.valueOf(0);
		Map<String, String> goodsIdMapCount = new HashMap<>();
		StringBuffer sb = new StringBuffer();

		if (defaultAddress == null || defaultAddress == 0) {
			throw new Exception("默认地址不能为空");
		}
		PageData address = userAddressMapper.selectById(defaultAddress);
		if (address == null) {
			throw new Exception("数据库没法查出该地址");
		}
		if (goodsLists == null || goodsLists.size() == 0) {
			throw new Exception("商品不存在！");
		}
		for (PageData tpd : goodsLists) {
			sb.append(tpd.getAsString("goods_id") + ",");
			goodsIdMapCount.put(tpd.getAsString("goods_id"),
					StringUtils.isNotEmpty(tpd.getAsString("goods_number")) ? tpd.getAsString("goods_number") : "1");
		}
		String goodsIdStr = sb.substring(0, sb.length() - 1);
		goodsIdStr = "(" + goodsIdStr + ")";
		param.put("goodsIds", goodsIdStr);
		// 查询商品列表
		goodsLists = goodsMapper.selectByIdsStr(param);
		if (goodsLists == null || goodsLists.size() == 0) {
			throw new Exception("商品都是去年的商品了");
		}
		// 计算所有非免运费的商品重量
		Boolean flag = false;

		for (PageData tpd : goodsLists) {
			if (StringUtils.isNotEmpty(tpd.getAsString("is_free_shipping"))
					&& "0".equals(tpd.getAsString("is_free_shipping"))) {
				flag = true;
				// 如果商品不免运费 则查看商品重量 如果重量也为0设为0.0（为了匹配运费首重） 那么商品运费为0

				String goodsWeight = StringUtils.isNotEmpty(tpd.getAsString("goods_weight"))
						? tpd.getAsString("goods_weight") : "0";
				String goodsCount = goodsIdMapCount.get(tpd.getAsString("goods_id"));
				weight = weight.add(new BigDecimal(goodsWeight).multiply(new BigDecimal(goodsCount)));
			}
		}
		// 查询默认地址的省份
		param.put("addressId",
				StringUtils.isNotEmpty(address.getAsString("province_id")) ? address.getAsString("province_id") : "0");
		if (flag) {
			BigDecimal tempKg = new BigDecimal(0);
			List<PageData> logisticsList = logisticsMapper.selectlogsticsByProvince(param);
			if (logisticsList == null || logisticsList.size() == 0) {
				throw new Exception("无该区域的物流信息");
			}
			PageData logistics = logisticsList.get(0);
			tempKg = logistics.getAsBigDecimal("first_kg");
			freight = logistics.getAsBigDecimal("first_price");
			BigDecimal tempPrice = BigDecimal.valueOf(0);

			if (weight.compareTo(logistics.getAsBigDecimal("first_kg")) == 1) {
				// 查询续重
				param.clear();
				param.put("area_id", logistics.getAsString("area_id"));
				param.put("goodsWeight", weight);
				List<PageData> contnus = logisticsMapper.selectContnuByWeightDesc(param);
				for (int k = 0; k < contnus.size() + 1; k++) {
					BigDecimal currentKg = BigDecimal.valueOf(0);
					if (k != contnus.size()) {
						currentKg = contnus.get(k).getAsBigDecimal("contnu_kg");
						freight = freight.add(currentKg.subtract(tempKg).multiply(tempPrice));
						tempPrice = contnus.get(k).getAsBigDecimal("contnu_price");
					} else {
						currentKg = weight;
						freight = freight.add(currentKg.subtract(tempKg).multiply(tempPrice));
					}
					if (k != contnus.size()) {
						tempKg = contnus.get(k).getAsBigDecimal("contnu_kg");
					}
				}
			}
			param.put("send_time", logistics.getAsString("send_time"));
			param.put("freight", freight);
		} else {
			param.put("send_time", "0");
			param.put("freight", "0");
		}
		param.put("weight", weight);
		return param;
	}

	/**
	 * 创建b2c订单  直接购买
	 * sku_id 库存id
	 * 
	 * goods_id   商品id
	 * 
	 * goods_number 商品数量
	 * @throws Exception 
	 */
	public PageData createOrderForB2cBuyNow(PageData newData) throws Exception{
		
		PageData selectPd = new PageData();
		PageData productDetail = goodsCartMapper.selectForgeBySkuId(newData);
		BigDecimal shippingMoney = BigDecimal.valueOf(0);
		BigDecimal goodsPrice = BigDecimal.valueOf(0);
		if ("0".equals(productDetail.getAsString("is_free_shipping"))
				|| StringUtils.isEmpty(productDetail.getAsString("is_free_shipping"))) {
			//如果是到点自取 将不计算运费
			if(StringUtils.isNotEmpty(newData.getAsString("shipping_type")) && "1".equalsIgnoreCase(newData.getAsString("shipping_type"))){
				shippingMoney = BigDecimal.valueOf(0);
			}else{
				if (StringUtils.isNotEmpty(productDetail.getAsString("goods_freight"))) {
					shippingMoney = shippingMoney.add(productDetail.getAsBigDecimal("goods_freight"));
				}
			}
		}
		goodsPrice = goodsPrice
				.add(productDetail.getAsBigDecimal("price").multiply(newData.getAsBigDecimal("goods_number")));
		String stockNumStr = StringUtils.isEmpty(productDetail.getAsString("stock")) ? productDetail.getAsString("goods_stock")
				: productDetail.getAsString("stock");
		int stockNum = EAString.stringToInt(stockNumStr, 0);
		System.out.println("商品库存<><><><><>><><<>:"+productDetail.getAsString("stock"));
		System.out.println("平台计算库存<><><><><>><><<>:"+productDetail.getAsString("stock"));
		System.out.println("购买数量<><><><><>><><<>:"+newData.getAsInt("goods_number"));
		if (stockNum < newData.getAsInt("goods_number")) {
			throw new Exception("该商品库存不足。");
		}
		if (newData.getAsBigDecimal("totalMoney").compareTo(goodsPrice.add(shippingMoney)) != 0) {
			throw new Exception("金额非法");
		}
		newData.put("order_money", goodsPrice.add(shippingMoney));
		newData.put("shipping_money", shippingMoney);
		newData.put("goods_money", goodsPrice);
		newData.put("order_status", "0");
		newData.put("shipping_status", 0);
		newData.put("give_points", 0);
		newData.put("pay_status", 0);
		newData.put("user_pay_points", 0);
		newData.put("pay_by_points", 0);
		newData.put("pay_by_money", goodsPrice.add(shippingMoney));
		newData.put("give_points", 0);
		newData.put("create_time", EADate.getCurrentTime());
		newData.put("is_delete", 0);
		newData.put("rebate_status", "-1");
		newData.put("order_sn", EAString.getFourSn());
		newData.put("received_money", "0");
		newData.put("rebate_money", "0");
		newData.put("rebate_money", "0");
		// 查询地址信息
		PageData addressInfo = new PageData();
		if(StringUtils.isEmpty(newData.getAsString("shipping_type")) || "0".equalsIgnoreCase(newData.getAsString("shipping_type"))){ //上门取货
			addressInfo = userAddressMapper.selectById(EAString.stringToInt(newData.getAsString("addressId"), 0));
			if (addressInfo == null) { //如果选择快递方式
				throw new Exception("地址查不出来");
			}
		}
		newData.put("contact_name", addressInfo.getAsString("contact_name"));
		newData.put("contact_phone", addressInfo.getAsString("contact_phone"));
		newData.put("province_id", addressInfo.getAsString("province_id"));
		newData.put("province", addressInfo.getAsString("province"));
		newData.put("city_id", addressInfo.getAsString("city_id"));
		newData.put("city", addressInfo.getAsString("city"));
		newData.put("area_id", addressInfo.getAsString("area_id"));
		newData.put("area", addressInfo.getAsString("area"));
		newData.put("address", addressInfo.getAsString("address"));
		newData.put("addtype", "0");
		orderMapper.insert(newData);
		selectPd.put("pay_by_money", newData.get("order_money"));
		selectPd.put("order_id", newData.get("order_id"));
		/**
		 * mer_no 插入订单商品a.sku_id,a.price,a.goods_id,a.stock,a.attr_json,b.goods_sn,b.goods_name,b.goods_tags,b.goods_stock,
		b.list_img,b.is_free_shipping,b.goods_freight
		 */
		PageData insertOrderGoods = new PageData();
		insertOrderGoods.put("order_id", newData.getAsString("order_id"));
		insertOrderGoods.put("goods_id", newData.getAsString("goods_id"));
		insertOrderGoods.put("list_img", productDetail.getAsString("list_img"));
		insertOrderGoods.put("goods_name", productDetail.getAsString("goods_name"));
		insertOrderGoods.put("goods_sn", productDetail.getAsString("goods_sn"));
		insertOrderGoods.put("goods_count", newData.getAsString("goods_number"));
		insertOrderGoods.put("goods_price", productDetail.getAsString("price"));
		insertOrderGoods.put("goods_sku_id", productDetail.getAsString("sku_id"));
		insertOrderGoods.put("goods_attr", productDetail.getAsString("attr_json"));
		insertOrderGoods.put("rebate_type", 2);
		insertOrderGoods.put("is_refund", 0);
		insertOrderGoods.put("is_gift", 0);
		ordersGoodsMapper.insert(insertOrderGoods);
		// 插入成功的同时更新库存表，同时更新商品总库存
		Stock stock = new Stock();
		stock.setSku_id(productDetail.getAsString("sku_id"));
		int stockCount = EAString.stringToInt(productDetail.getAsString("stock"), 0)
				- EAString.stringToInt(newData.getAsString("goods_number"), 0);
		stock.setStock(String.valueOf(stockCount));
		goodsMapper.updateStockByPid(stock);
		PageData goodspd = new PageData();
		goodspd.put("goods_id", productDetail.getAsString("goods_id"));
		goodspd.put("goods_stock", EAString.stringToInt(productDetail.getAsString("goods_stock"), 0)
				- EAString.stringToInt(newData.getAsString("goods_number"), 0));
		goodsMapper.update(goodspd);
		return selectPd;
	}
	/**
	 * 
	 * 创建b2c订单
	 * 
	 * 
	 * @param newData
	 * @return
	 * @throws Exception
	 */
	public PageData createOrderForB2C(PageData newData) throws Exception {
		PageData selectPd = new PageData();
		selectPd.put("user_id", newData.getAsString("user_id"));
		// 查询选中的购物车数据
		selectPd.put("cart_ids", newData.getAsString("cartIds").split(","));
		List<PageData> carList = goodsCartMapper.selectByUserCartId(selectPd);
		if(EAUtil.isEmpty(carList)){
			throw new Exception("该购物车商品已形成订单，请勿重复提交支付！");
		}
		BigDecimal goodsPrice = BigDecimal.valueOf(0);
		BigDecimal shippingMoney = BigDecimal.valueOf(0);
		for (PageData tempPd : carList) {
			if ("0".equals(tempPd.getAsString("is_free_shipping"))
					|| StringUtils.isEmpty(tempPd.getAsString("is_free_shipping"))) {
				if (StringUtils.isNotEmpty(tempPd.getAsString("goods_freight"))) {
					shippingMoney = shippingMoney.add(tempPd.getAsBigDecimal("goods_freight"));
				}
			}
			goodsPrice = goodsPrice
					.add(tempPd.getAsBigDecimal("price").multiply(tempPd.getAsBigDecimal("goods_number")));
			// 得到库存值
			String stockNumStr = StringUtils.isEmpty(tempPd.getAsString("stock")) ? tempPd.getAsString("goods_stock")
					: tempPd.getAsString("stock");
			int stockNum = EAString.stringToInt(stockNumStr, 0);

			if (stockNum < tempPd.getAsInt("goods_number")) {
				throw new Exception("商品 " + tempPd.getAsString("goods_name") + "库存不足。");
			}
		}
		if (newData.getAsBigDecimal("totalMoney").compareTo(goodsPrice.add(shippingMoney)) != 0) {
			throw new Exception("金额非法");
		}
		newData.put("order_money", goodsPrice.add(shippingMoney));
		newData.put("shipping_money", shippingMoney);
		newData.put("goods_money", goodsPrice);
		newData.put("order_status", "0");
		newData.put("shipping_status", 0);
		newData.put("shipping_type", 1);
		newData.put("give_points", 0);
		newData.put("pay_status", 0);
		newData.put("user_pay_points", 0);
		newData.put("pay_by_points", 0);
		newData.put("pay_by_money", goodsPrice.add(shippingMoney));
		newData.put("give_points", 0);
		newData.put("create_time", EADate.getCurrentTime());
		newData.put("is_delete", 0);
		newData.put("rebate_status", "-1");
		newData.put("order_sn", EAString.getFourSn());
		newData.put("received_money", "0");
		newData.put("rebate_money", "0");
		newData.put("rebate_money", "0");
		// 查询地址信息
		PageData addressInfo = userAddressMapper.selectById(EAString.stringToInt(newData.getAsString("addressId"), 0));
		if (addressInfo == null) {
			throw new Exception("地址查不出来");
		}

		newData.put("contact_name", addressInfo.getAsString("contact_name"));
		newData.put("contact_phone", addressInfo.getAsString("contact_phone"));
		newData.put("province_id", addressInfo.getAsString("province_id"));
		newData.put("province", addressInfo.getAsString("province"));
		newData.put("city_id", addressInfo.getAsString("city_id"));
		newData.put("city", addressInfo.getAsString("city"));
		newData.put("area_id", addressInfo.getAsString("area_id"));
		newData.put("area", addressInfo.getAsString("area"));
		newData.put("address", addressInfo.getAsString("address"));
		newData.put("addtype", "0");
		orderMapper.insert(newData);
		selectPd.put("pay_by_money", newData.get("order_money"));
		selectPd.put("order_id", newData.get("order_id"));
		/**
		 * mer_no 插入订单商品
		 */

		for (PageData cartgoods : carList) {
			PageData insertOrderGoods = new PageData();
			insertOrderGoods.put("order_id", newData.getAsString("order_id"));
			insertOrderGoods.put("goods_id", cartgoods.getAsString("goods_id"));
			insertOrderGoods.put("list_img", cartgoods.getAsString("list_img"));
			insertOrderGoods.put("goods_name", cartgoods.getAsString("goods_name"));
			insertOrderGoods.put("goods_sn", cartgoods.getAsString("goods_sn"));
			insertOrderGoods.put("goods_count", cartgoods.getAsString("goods_number"));
			insertOrderGoods.put("goods_price", cartgoods.getAsString("price"));
			insertOrderGoods.put("goods_sku_id", cartgoods.getAsString("sku_id"));
			insertOrderGoods.put("goods_attr", cartgoods.getAsString("attr_json"));
			insertOrderGoods.put("rebate_type", 2);
			insertOrderGoods.put("is_refund", 0);
			insertOrderGoods.put("is_gift", 0);
			ordersGoodsMapper.insert(insertOrderGoods);
			// 插入成功的同时更新库存表，同时更新商品总库存
			Stock stock = new Stock();
			stock.setSku_id(cartgoods.getAsString("sku_id"));
			int stockCount = EAString.stringToInt(cartgoods.getAsString("stock"), 0)
					- EAString.stringToInt(cartgoods.getAsString("goods_number"), 0);
			stock.setStock(String.valueOf(stockCount));
			goodsMapper.updateStockByPid(stock);
			PageData goodspd = new PageData();
			goodspd.put("goods_id", cartgoods.getAsString("goods_id"));
			goodspd.put("goods_stock", EAString.stringToInt(cartgoods.getAsString("goods_stock"), 0)
					- EAString.stringToInt(cartgoods.getAsString("goods_number"), 0));
			goodsMapper.update(goodspd);
		}
		/**
		 * 删除购物车数据
		 */
		/**
		 * 批量删除 购物车商品
		 */
		String[] cartIdsStr = newData.getAsString("cartIds").split(",");
		int[] cartIdsInt = new int[cartIdsStr.length];
		for (int i = 0; i < cartIdsStr.length; i++) {
			cartIdsInt[i] = Integer.parseInt(cartIdsStr[i]);
		}
		goodsCartMapper.deleteBatch(cartIdsInt);
		return selectPd;
	}

	/**
	 * 创建b2c 折扣订单  直接购买 
	 * sku_id 库存id
	 * goods_number 商品数量
	 * bs_id 商家ID
	 * pro_id 折扣活动商品ID
	 * @throws Exception 
	 */
	public PageData createOrderForDiscountB2cBuyNow(PageData newData) throws Exception{
		BigDecimal shippingMoney = BigDecimal.valueOf(0);
		BigDecimal goodsPrice = BigDecimal.valueOf(0);
		//获取封装 折扣商品信息
		if (EAUtil.isEmpty(newData.getAsString("pro_id"))) {
			throw new Exception("折扣活动，折扣商品ID不能为空");
		}
		// 校验活动是否结束
		PageData newGood= new PageData();
		newGood.put("id", newData.getAsString("pro_id"));
		newGood.put("now", EADate.getCurrentTime());
		PageData limitdata = goodsActivityTimelimitMapper.selectByProId(newGood);
		if (EAUtil.isEmpty(limitdata)) {
			throw new Exception("折扣商品有误或折扣时间已过");
		}
		if (limitdata.getAsString("bs_id").equals(newData.getAsString("bs_id"))) {
			throw new Exception("不能购买自己的活动商品");
		}
		if (newData.getAsInt("goods_number") < limitdata.getAsInt("min_num")) {
			throw new Exception("折扣商品购买数量有误");
		}else{
			 limitdata.put("min_num", newData.getAsInt("goods_number"));
		}
		newGood.clear();
		newGood.put("sku_id", limitdata.getAsString("stock_id"));
		Stock gstock = goodsMapper.queryStockBySkuId(newGood);
		newGood.put("attr_json", gstock.getAttr_json());
		newGood.put("stock", gstock.getStock()); // 库存
		newGood.put("goods_number", newData.getAsString("goods_number"));
		newGood.put("goods_count", newData.getAsString("goods_number"));
		newGood.put("goods_price", limitdata.getAsString("discount_price"));
		newGood.put("list_img", limitdata.getAsString("list_img"));
		newGood.put("goods_id", gstock.getGoods_id());
		newGood.put("goods_name", limitdata.getAsString("goods_name"));
		newGood.put("bs_id", limitdata.getAsString("bs_id"));
		newGood.put("goods_freight", limitdata.getAsString("goods_freight"));
		newGood.put("goods_sn", limitdata.getAsString("goods_sn"));
		if ("0".equals(limitdata.getAsString("is_free_shipping"))
				|| StringUtils.isEmpty(limitdata.getAsString("is_free_shipping"))) {
			if (StringUtils.isNotEmpty(limitdata.getAsString("goods_freight"))) {
				shippingMoney = shippingMoney.add(limitdata.getAsBigDecimal("goods_freight"));
			}
		}
		goodsPrice = goodsPrice
				.add((limitdata.getAsBigDecimal("discount_price")).multiply(limitdata.getAsBigDecimal("min_num")));
		String stockNumStr = StringUtils.isEmpty(gstock.getStock()) ?"0": gstock.getStock();
		int stockNum = EAString.stringToInt(stockNumStr, 0);
		System.out.println("商品库存<><><><><>><><<>:"+gstock.getStock());
		System.out.println("平台计算库存<><><><><>><><<>:"+gstock.getStock());
		System.out.println("购买数量<><><><><>><><<>:"+newData.getAsInt("goods_number"));
		if (Integer.valueOf(stockNumStr) <= 0) {
			throw new Exception("折扣商品已售罄！");
		}
		if (stockNum < newData.getAsInt("goods_number")) {
			throw new Exception("折扣商品库存不足。");
		}
		if (newData.getAsBigDecimal("totalMoney").compareTo(goodsPrice.add(shippingMoney)) != 0) {
			throw new Exception("折扣商品金额非法");
		}
		newData.put("order_money", goodsPrice.add(shippingMoney));
		newData.put("shipping_money", shippingMoney);
		newData.put("goods_money", goodsPrice);
		newData.put("order_status", "0");
		newData.put("shipping_status", 0);
		newData.put("shipping_type", 1);
		newData.put("give_points", 0);
		newData.put("pay_status", 0);
		newData.put("user_pay_points", 0);
		newData.put("pay_by_points", 0);
		newData.put("pay_by_money", goodsPrice.add(shippingMoney));
		newData.put("give_points", 0);
		newData.put("create_time", EADate.getCurrentTime());
		newData.put("is_delete", 0);
		newData.put("rebate_status", "-1");
		newData.put("order_sn", EAString.getFourSn());
		newData.put("received_money", "0");
		newData.put("rebate_money", "0");
		// 查询地址信息
		PageData addressInfo = userAddressMapper.selectById(EAString.stringToInt(newData.getAsString("addressId"), 0));
		if (addressInfo == null) {
			throw new Exception("地址查不出来");
		}
		newData.put("contact_name", addressInfo.getAsString("contact_name"));
		newData.put("contact_phone", addressInfo.getAsString("contact_phone"));
		newData.put("province_id", addressInfo.getAsString("province_id"));
		newData.put("province", addressInfo.getAsString("province"));
		newData.put("city_id", addressInfo.getAsString("city_id"));
		newData.put("city", addressInfo.getAsString("city"));
		newData.put("area_id", addressInfo.getAsString("area_id"));
		newData.put("area", addressInfo.getAsString("area"));
		newData.put("address", addressInfo.getAsString("address"));
		newData.put("addtype", "1");
		orderMapper.insert(newData);
		PageData selectPd= new PageData();
		selectPd.put("pay_by_money", newData.get("order_money"));
		selectPd.put("order_id", newData.get("order_id"));
		/**
		 * mer_no 插入订单商品a.sku_id,a.price,a.goods_id,a.stock,a.attr_json,b.goods_sn,b.goods_name,b.goods_tags,b.goods_stock,
		b.list_img,b.is_free_shipping,b.goods_freight
		 */
		PageData insertOrderGoods = new PageData();
		insertOrderGoods.put("order_id", newData.getAsString("order_id"));
		insertOrderGoods.put("goods_id", newData.getAsString("goods_id"));
		insertOrderGoods.put("list_img", newGood.getAsString("list_img"));
		insertOrderGoods.put("goods_name", newGood.getAsString("goods_name"));
		insertOrderGoods.put("goods_sn", newGood.getAsString("goods_sn"));
		insertOrderGoods.put("goods_count", newData.getAsString("goods_number"));
		insertOrderGoods.put("goods_price", newGood.getAsString("goods_price"));
		insertOrderGoods.put("goods_sku_id", newGood.getAsString("sku_id"));
		insertOrderGoods.put("goods_attr", newGood.getAsString("attr_json"));
		insertOrderGoods.put("rebate_type", 2);
		insertOrderGoods.put("is_refund", 0);
		insertOrderGoods.put("is_gift", 0);
		ordersGoodsMapper.insert(insertOrderGoods);
		// 插入成功的同时更新库存表，同时更新商品总库存
		Stock stock = new Stock();
		stock.setSku_id(newGood.getAsString("sku_id"));
		int stockCount = EAString.stringToInt(newGood.getAsString("stock"), 0)
				- EAString.stringToInt(newData.getAsString("goods_number"), 0);
		stock.setStock(String.valueOf(stockCount));
		goodsMapper.updateStockByPid(stock);
		PageData goodspd = new PageData();
		goodspd.put("goods_id", newGood.getAsString("goods_id"));
		goodspd.put("goods_stock", EAString.stringToInt(newGood.getAsString("stock"), 0)
				- EAString.stringToInt(newData.getAsString("goods_number"), 0));
		goodsMapper.update(goodspd);
		return selectPd;
	}
	
	
	/**
	 * 创建订单（拆单）
	 * 
	 * @param newData
	 * @throws Exception
	 */
	public PageData createOrder(PageData newData) throws Exception {
		Page page = new Page();
		/**
		 * 检查参数
		 */
		PageData upd = new PageData();
		upd.put("user_id", newData.getAsString("user_id"));
		if (StringUtils.isEmpty(newData.getAsString("user_id"))) {
			throw new Exception("用户信息丢失，请退出公众号重新进入");
		}
		page.setPd(upd);
		List<PageData> user = userMapper.selectByMap(page);
		if (EAUtil.isNotEmpty(user)) {
			newData.put("user_name", user.get(0).getAsString("nick_name"));
		}
		upd.clear();
		// 判断参数是否购物车id是空
		if (StringUtils.isEmpty(newData.getAsString("cart_id"))
				|| newData.getAsString("cart_id").split(",").length == 0) {
			throw new Exception("未选择任何商品");
		}
		// 用in关键字查询传过来的商品
		upd.put("carts_id", "(" + newData.getAsString("cart_id") + ")");
		List<PageData> cartList = goodsCartMapper.selectByMap(page);
		List<PageData> goodsList = new ArrayList<PageData>();
		// 系统计算价格
		BigDecimal systemPrice = BigDecimal.valueOf(0);
		// 计算商品赠送积分
		BigDecimal givePoint = BigDecimal.valueOf(0);
		// 检测订单是否使用积分支付
		boolean isPayPoints = false;
		BigDecimal payPoints = newData.getAsBigDecimal("pay_by_points");
		if (payPoints.compareTo(new BigDecimal(0)) == 1) {
			isPayPoints = true;
		}
		// 运费
		BigDecimal SMoney = BigDecimal.valueOf(0);
		// 重新封装商品 查看是否分单操作
		boolean splitType = false;
		for (PageData tempCart : cartList) {
			PageData newGood = goodsMapper.selectById(tempCart.getAsInt("goods_id"));
			// SMoney = SMoney.add(newGood.getAsBigDecimal("goods_freight"));
			newGood.put("sku_id", tempCart.getAsString("sku_id"));
			Stock gstock = goodsMapper.queryStockBySkuId(newGood);
			if (EAUtil.isNotEmpty(gstock)) {
				if (Integer.valueOf(gstock.getStock()) < tempCart.getAsInt("goods_number")) {
					throw new Exception("商品库存不足");
				}
				newGood.put("attr_json", gstock.getAttr_json());
				newGood.put("stock", gstock.getStock()); // 库存
			}
			if (EAUtil.isNotEmpty(newGood.getAsString("bs_id"))) {
				splitType = true;
			}
			systemPrice = systemPrice.add(new BigDecimal(gstock.getPrice())
					.multiply(BigDecimal.valueOf(tempCart.getAsInteger("goods_number"))));// 计算价格
			newGood.put("goods_number", tempCart.getAsString("goods_number"));
			newGood.put("goods_count", tempCart.getAsString("goods_number"));
			newGood.put("goods_price", gstock.getPrice());
			goodsList.add(newGood);

			if (!isPayPoints) {
				newData.put("user_pay_points", "0");
				givePoint = givePoint.add((BigDecimal) (newGood.getAsBigDecimal("give_points")
						.multiply(tempCart.getAsBigDecimal("goods_number")) == null ? 0
								: newGood.getAsBigDecimal("give_points")
										.multiply(tempCart.getAsBigDecimal("goods_number"))));
			} else {
				givePoint = BigDecimal.valueOf(0);
			}
		}
		/**
		 * 查询是否使用优惠劵
		 */
		BigDecimal cashMoney = BigDecimal.valueOf(0);
		if (EAUtil.isNotEmpty(newData.getAsInt("cash_id")) && newData.getAsInt("cash_id") != -1) {
			Integer cash_id = newData.getAsInt("cash_id");
			page = new Page();
			PageData cashpd = new PageData();
			cashpd.put("cash_id", cash_id);
			cashpd.put("user_id", newData.getAsString("user_id"));
			page.setPd(cashpd);
			List<PageData> cashlist = couponMapper.selectAppByPage(page);
			if (EAUtil.isNotEmpty(cashlist) && cashlist.size() > 0) {
				PageData cashdata = cashlist.get(0);
				// 判断时间是否过期（）
				long dataNow = EADate.getMillis(new Date());
				long datacash = EADate.getMillis(EADate.stringToDate(cashdata.getAsString("cash_end_time")));
				if (cashdata.getAsString("user_status").equals("0")
						&& systemPrice.compareTo(cashdata.getAsBigDecimal("cash_consumption_money")) >= 0
						&& datacash > dataNow) {
					cashMoney = cashdata.getAsBigDecimal("cash_money");
					newData.put("cash_money", cashMoney);
					cashpd.put("id", cashdata.getAsString("record_id"));
					cashpd.put("user_status", "1");
					couponMapper.updaterecord(cashpd);
				} else {
					throw new Exception("优惠劵已使用或未达使用要求！");
				}
			} else {
				throw new Exception("优惠劵无效");
			}
		} else {
			newData.put("cash_id", "0");
			newData.put("cash_money", "0");
		}

		/**
		 * 是否动态计算快递费用 查询快递费
		 */
		BigDecimal zreo_monety = new BigDecimal(0);
		newData.put("goods_money", systemPrice);// 存入商品价格

		/**
		 * 计算价格结果
		 */
		PageData addressPd = new PageData();
		addressPd.put("address_id", newData.getAsString("address_id"));
		List<PageData> bsInfoPdList = new ArrayList<PageData>();
		List<String> bsIdList = new ArrayList<String>();
		for (PageData pageData : goodsList) {

			List<PageData> pinfoPdList = new ArrayList<PageData>();
			PageData bsInfo = new PageData();
			bsInfo.put("bs_id", pageData.getAsString("bs_id"));
			PageData pinfo = new PageData();
			pinfo.put("p_id", pageData.getAsString("goods_id"));
			pinfo.put("number", pageData.getAsString("goods_number"));
			pinfoPdList.add(pinfo);
			bsInfo.put("pinfo", pinfoPdList);

			if (EAUtil.isNotEmpty(bsInfoPdList)) {
				for (int i = (bsInfoPdList.size() - 1); i >= 0; i--) {
					PageData pd2 = bsInfoPdList.get(i);
					List<PageData> pinfolist = pd2.getAsList("pinfo");
					if (bsIdList.contains(pageData.getAsString("bs_id"))
							&& pd2.getAsString("bs_id").equals(pageData.getAsString("bs_id"))
							&& !pinfolist.contains(pinfo)) {
						pinfolist.addAll(pinfoPdList);
						pd2.put("pinfo", pinfolist);
					} else if (!bsIdList.contains(pageData.getAsString("bs_id"))
							&& !pd2.getAsString("bs_id").equals(pageData.getAsString("bs_id"))
							&& !pinfolist.contains(pinfo)) {
						bsInfoPdList.add(bsInfo);
						bsIdList.add(pageData.getAsString("bs_id"));
					}
				}
			} else {
				bsInfoPdList.add(bsInfo);
				bsIdList.add(pageData.getAsString("bs_id"));
			}
		}
		addressPd.put("bsInfo", bsInfoPdList);
		String jsonStr = JSONObject.fromObject(addressPd).toString();
		List<PageData> freight = ordersShippingService.calculatedFreight(jsonStr);
		for (PageData pageData : freight) {
			SMoney = SMoney.add(pageData.getAsBigDecimal("freight"));
		}
		if (SMoney.compareTo(newData.getAsBigDecimal("shipping_money")) != 0) {
			throw new Exception("运费计算有误");
		}
		newData.put("shipping_money", SMoney);
		systemPrice = systemPrice.add(newData.getAsBigDecimal("shipping_money")).subtract(cashMoney);
		if (systemPrice.compareTo(zreo_monety) == -1) {
			systemPrice = zreo_monety;
		}
		if (systemPrice.compareTo(
				newData.getAsBigDecimal("pay_by_money").add(newData.getAsBigDecimal("pay_by_points"))) != 0) {
			throw new Exception("总金额计算有误");
		}

		newData.put("give_points", givePoint);
		newData.put("order_money", systemPrice);
		if (systemPrice.compareTo(zreo_monety) == 0) {
			newData.put("order_status", "1");
			newData.put("pay_status", "1");
		} else {
			newData.put("order_status", "0");
			newData.put("pay_status", "0");
		}
		newData.put("addtype", "0");
		addOrder(newData);
		/**
		 * 订单商品插入记录
		 */
		for (PageData pd : goodsList) {
			PageData newGood = new PageData();
			newGood.put("order_id", newData.getAsString("order_id"));
			newGood.put("goods_id", pd.getAsString("goods_id"));
			newGood.put("goods_name", pd.getAsString("goods_name"));
			newGood.put("goods_sn", pd.getAsString("goods_sn"));
			newGood.put("goods_count", pd.getAsString("goods_count"));
			newGood.put("goods_price", pd.getAsString("goods_price"));
			newGood.put("goods_sku_id", pd.getAsString("sku_id"));
			newGood.put("is_gift", (pd.getAsString("is_gift").equals("") ? "0" : pd.getAsString("is_gift")));
			newGood.put("goods_attr", pd.getAsString("attr_json")); // 销售属性
			// 分销部分
			newGood.put("rebate_1", pd.get("rebate_1"));
			newGood.put("rebate_2", pd.get("rebate_2"));
			newGood.put("rebate_3", pd.get("rebate_3"));
			// newGood.put("points_1", pd.get("points_1"));
			// newGood.put("points_2", pd.get("points_2"));
			// newGood.put("points_3", pd.get("points_3"));
			// newGood.put("rebate_points", newData.getAsString("give_points"));
			newGood.put("rebate_type", pd.getAsInt("rebate_type"));
			ordersGoodsMapper.insert(newGood);
			pd.put("order_goods_id", newGood.getAsString("order_goods_id"));
			// 插入成功的同时更新库存表，同时更新商品总库存
			Stock stock = new Stock();
			stock.setSku_id(pd.getAsString("sku_id"));
			int stockCount = pd.getAsInt("stock") - pd.getAsInt("goods_count");
			stock.setStock(String.valueOf(stockCount));
			goodsMapper.updateStockByPid(stock);
			PageData goodspd = new PageData();
			goodspd.put("goods_id", pd.getAsString("goods_id"));
			goodspd.put("goods_stock", stockCount);
			goodsMapper.update(goodspd);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", newData.getAsString("order_id"));
		/**
		 * 不同商家分单操作
		 */
		if (splitType) {
			PageData bsOrderMapPd = new PageData();
			bsOrderMapPd.put("platform_order_id", newData.getAsString("order_id"));
			// 发票信息
			JSONArray jsonArray = JSONArray.fromObject(newData.getAsString("merdata"));
			// Java集合
			List<PageData> merData = (List<PageData>) jsonArray.toCollection(jsonArray, PageData.class);
			for (PageData goods : goodsList) {
				if (EAUtil.isNotEmpty(goods.getAsString("bs_id"))) {
					// 查询店铺信息
					PageData business = businessMapper.selectById(EAString.stringToInt(goods.getAsString("bs_id"), 0));
					if (EAUtil.isEmpty(business)) {
						throw new Exception("商品商家发生错误！");
					}
					bsOrderMapPd.put("business_id", goods.getAsString("bs_id"));
					List<PageData> borderlist = businessOrderMapper.selectOrderMap(bsOrderMapPd);

					BigDecimal orderMoney = (goods.getAsBigDecimal("goods_price")
							.multiply(goods.getAsBigDecimal("goods_count")));

					BigDecimal goodsMoney = (goods.getAsBigDecimal("goods_price")
							.multiply(goods.getAsBigDecimal("goods_count")));
					BigDecimal shippingMoney = BigDecimal.valueOf(0);
					for (PageData frepd : freight) {
						if(frepd.getAsString("bs_id").equals(goods.getAsBigDecimal("bs_id"))){
							shippingMoney=frepd.getAsBigDecimal("freight");
						}
					}
					if (EAUtil.isEmpty(borderlist)) {
						// 插入数据
						bsOrderMapPd.put("order_sn", EAString.getFourSn());
						bsOrderMapPd.put("user_id", newData.getAsString("user_id"));
						bsOrderMapPd.put("user_name", user.get(0).getAsString("nick_name"));
						bsOrderMapPd.put("order_money", orderMoney.add(shippingMoney));
						bsOrderMapPd.put("goods_money", goodsMoney);
						bsOrderMapPd.put("shipping_money", shippingMoney);
						bsOrderMapPd.put("shipping_fee", "0");
						bsOrderMapPd.put("order_status", "0");
						bsOrderMapPd.put("shipping_status", "0");
						bsOrderMapPd.put("shipping_type", "1");
						bsOrderMapPd.put("pay_status", "0");
						bsOrderMapPd.put("pay_type", "0");
						bsOrderMapPd.put("is_delete", "0");
						bsOrderMapPd.put("pay_by_money", orderMoney.add(shippingMoney));
						if ("1".equals(business.getAsString("is_own_shop"))) {
							bsOrderMapPd.put("give_points", givePoint);
							bsOrderMapPd.put("pay_by_points", payPoints);
							bsOrderMapPd.put("cash_id", newData.getAsString("cash_id"));
							bsOrderMapPd.put("cash_money", newData.getAsString("cash_money"));
							isPayPoints = true;
						}
						bsOrderMapPd.put("create_time", EADate.getCurrentTime());
						bsOrderMapPd.put("pay_time", EADate.getCurrentTime());
						bsOrderMapPd.put("contact_name", newData.getAsString("contact_name"));
						bsOrderMapPd.put("contact_phone", newData.getAsString("contact_phone"));
						bsOrderMapPd.put("province_id", newData.getAsString("province_id"));
						bsOrderMapPd.put("province", newData.getAsString("province"));
						bsOrderMapPd.put("city_id", newData.getAsString("city_id"));
						bsOrderMapPd.put("city", newData.getAsString("city"));
						bsOrderMapPd.put("area_id", newData.getAsString("area_id"));
						bsOrderMapPd.put("area", newData.getAsString("area"));
						bsOrderMapPd.put("address", newData.getAsString("address"));
						bsOrderMapPd.put("addtype", "0");
						bsOrderMapPd.put("is_mer", "0");
						if (EAUtil.isNotEmpty(merData)) {
							for (PageData merpd : merData) {
								if (goods.getAsString("bs_id").equals(merpd.getAsString("bs_id"))) {
									bsOrderMapPd.put("is_mer", "1");
									bsOrderMapPd.put("mer_title", merpd.getAsString("mer_title"));
									bsOrderMapPd.put("client_remark", merpd.getAsString("client_remark"));
									bsOrderMapPd.put("mer_no", merpd.getAsString("mer_no"));
								}
							}
						}
						businessOrderMapper.insert(bsOrderMapPd);
					} else if (EAUtil.isNotEmpty(borderlist) && borderlist.size() == 1) {
						PageData data = borderlist.get(0);
						// 修改数据 修改运费，总价，商品总价格
						bsOrderMapPd.put("id", data.getAsString("id"));
						bsOrderMapPd.put("order_money", data.getAsBigDecimal("order_money").add(orderMoney));
						bsOrderMapPd.put("pay_by_money", data.getAsBigDecimal("pay_by_money").add(orderMoney));
						bsOrderMapPd.put("goods_money", data.getAsBigDecimal("goods_money").add(goodsMoney));
						businessOrderMapper.update(bsOrderMapPd);
					} else {
						throw new Exception("商家商品订单发生错误！");
					}
					// 插入商家订单商品数据
					bsOrderMapPd.put("order_goods_id", goods.getAsString("order_goods_id"));
					bsOrderMapPd.put("border_id", bsOrderMapPd.getAsString("id"));
					businessOrderMapper.insertOrderGoods(bsOrderMapPd);
				}
			}
		}
		/**
		 * 批量删除 购物车商品
		 */
		String[] cartIdsStr = newData.getAsString("cart_id").split(",");
		int[] cartIdsInt = new int[cartIdsStr.length];
		for (int i = 0; i < cartIdsStr.length; i++) {
			cartIdsInt[i] = Integer.parseInt(cartIdsStr[i]);
		}
		goodsCartMapper.deleteBatch(cartIdsInt);
		// 更改用户积分账户
		if (StringUtils.isNotEmpty(newData.getAsString("user_pay_points")) && newData.getAsDouble("user_pay_points") > 0
				&& isPayPoints) {
			PageData userAccount = userAccountMapper.selectByUserId(newData.getAsInt("user_id"));
			// 判断积分数量是否合法
			BigDecimal oldPoint = (BigDecimal) (userAccount.getAsBigDecimal("user_points") == null ? 0
					: userAccount.getAsBigDecimal("user_points"));
			if (oldPoint.compareTo(newData.getAsBigDecimal("user_pay_points")) < 0) {
				throw new Exception("账户积分数量不足");
			}
			userAccount.put("user_points", oldPoint.subtract(newData.getAsBigDecimal("user_pay_points")));
			userAccountMapper.updatePoints(userAccount);
		}
		// throw new Exception("ceshi");
		return newData;
	}

	/**
	 * 商家分单支付时 创建支付总订单
	 * 
	 * @param newData
	 * @throws Exception
	 */
	public PageData createPayOrder(PageData newData) throws Exception {
		Page page = new Page();
		/**
		 * 检查参数
		 */
		PageData upd = new PageData();
		upd.put("user_id", newData.getAsString("user_id"));
		if (StringUtils.isEmpty(newData.getAsString("user_id"))) {
			throw new Exception("用户信息丢失，请退出公众号重新进入");
		}
		page.setPd(upd);
		List<PageData> user = userMapper.selectByMap(page);
		if (EAUtil.isNotEmpty(user)) {
			newData.put("user_name", user.get(0).getAsString("nick_name"));
		}
		/**
		 * 获取所有支付订单详请，创建合并支付订单
		 */
		if (StringUtils.isEmpty(newData.getAsString("order_ids"))) {
			throw new Exception("订单ID有误！");
		}
		List<PageData> bsorder = businessOrderMapper.selectByIds(newData);
		// 商品价格
		BigDecimal goodsMoney = BigDecimal.valueOf(0);
		// 总价
		BigDecimal orderMoney = BigDecimal.valueOf(0);
		// 支付价格
		BigDecimal payByMoney = BigDecimal.valueOf(0);
		// 运费
		BigDecimal shippingMoney = BigDecimal.valueOf(0);
		// 赠送积分
		BigDecimal givePoints = BigDecimal.valueOf(0);
		// 用户使用积分
		BigDecimal userPayPoints = BigDecimal.valueOf(0);
		// 用户使用积分抵扣
		BigDecimal payByPoints = BigDecimal.valueOf(0);
		// 优惠劵金额
		BigDecimal cashMoney = BigDecimal.valueOf(0);
		//
		Integer p_order_id = 0;
		for (PageData pageData : bsorder) {
			goodsMoney = goodsMoney.add(pageData.getAsBigDecimal("goods_money"));
			orderMoney = orderMoney.add(pageData.getAsBigDecimal("order_money"));
			payByMoney = payByMoney.add(pageData.getAsBigDecimal("pay_by_money"));
			shippingMoney = shippingMoney.add(pageData.getAsBigDecimal("shipping_money"));
			givePoints = givePoints.add(pageData.getAsBigDecimal("give_points"));
			userPayPoints = userPayPoints.add(pageData.getAsBigDecimal("user_pay_points"));
			payByPoints = payByPoints.add(pageData.getAsBigDecimal("pay_by_points"));
			cashMoney = cashMoney.add(pageData.getAsBigDecimal("cash_money"));
			p_order_id = pageData.getAsInt("platform_order_id");
		}
		newData.put("order_money", orderMoney);
		newData.put("goods_money", goodsMoney);
		newData.put("shipping_money", shippingMoney);
		newData.put("pay_by_money", payByMoney);
		newData.put("order_status", "0");
		newData.put("pay_status", "0");
		newData.put("pay_by_points", payByPoints);
		newData.put("user_pay_points", userPayPoints);
		newData.put("give_points", givePoints);
		newData.put("cash_money", cashMoney);
		newData.put("addtype", "0");
		newData.put("aType", "1");
		addOrder(newData);
		// 更新订单商品列表
		List<PageData> ogoodslist = ordersGoodsMapper.selectByOrderId(p_order_id);
		for (PageData pageDatas : ogoodslist) {
			pageDatas.put("order_id", newData.getAsString("order_id"));
			ordersGoodsMapper.update(pageDatas);
		}
		// 更改分单总订单号
		for (PageData pageData : bsorder) {
			pageData.put("platform_order_id", newData.getAsString("order_id"));
			businessOrderMapper.update(pageData);
		}
		return newData;
	}

	/**
	 * 创建秒杀，折扣，套餐订单
	 * 
	 * @param newData
	 * @throws Exception
	 */
	public PageData createOrderActivity(PageData newData) throws Exception {
		Page page = new Page();
		/**
		 * 检查参数
		 */
		PageData upd = new PageData();
		upd.put("user_id", newData.getAsString("user_id"));
		if (StringUtils.isEmpty(newData.getAsString("user_id"))) {
			throw new Exception("用户信息丢失，请退出公众号重新进入");
		}
		page.setPd(upd);
		List<PageData> user = userMapper.selectByMap(page);
		if (EAUtil.isNotEmpty(user)) {
			newData.put("user_name", user.get(0).getAsString("nick_name"));
		}
		// 查询用户是否为商家
		PageData business = businessMapper.selectByUserId(EAString.stringToInt(newData.getAsString("user_id"), 0));
		upd.clear();
		List<PageData> goodsList = new ArrayList<PageData>();
		// 系统计算价格
		BigDecimal systemPrice = BigDecimal.valueOf(0);
		// 系统计算运费价格
		BigDecimal systemShippingPrice = BigDecimal.valueOf(0);
		PageData newGood = new PageData();
		// 秒杀活动下单
		if ("seckill".equals(newData.getAsString("addtype"))) {
			newData.put("addtype", "1");
			if (EAUtil.isEmpty(newData.getAsString("pro_id"))) {
				throw new Exception("秒杀活动，秒杀商品ID不能为空");
			}
			// 校验活动是否结束
			newGood.put("id", newData.getAsString("pro_id"));
			newGood.put("now", EADate.getCurrentTime());
			PageData seckilldata = goodsActivitySeckillMapper.selectByProId(newGood);
			if (EAUtil.isEmpty(seckilldata)) {
				throw new Exception("秒杀商品有误或秒杀时间已过");
			}
			if (seckilldata.getAsString("bs_id").equals(business.getAsString("bs_id"))) {
				throw new Exception("不能购买自己的活动商品");
			}
			if (newData.getAsInt("goods_number") <= seckilldata.getAsInt("timelimit_buy_min")) {
				throw new Exception("秒杀商品购买数量有误");
			}
			if (seckilldata.getAsInt("stock") - seckilldata.getAsInt("buy_num") > 0) {
				newGood.clear();
				newGood.put("sku_id", seckilldata.getAsString("kp_sku_id"));
				Stock gstock = goodsMapper.queryStockBySkuId(newGood);
				newGood.put("attr_json", gstock.getAttr_json());
				newGood.put("stock", gstock.getStock()); // 库存
				systemPrice = systemPrice.add(seckilldata.getAsBigDecimal("discount_price"))
						.multiply(BigDecimal.valueOf(1));// 计算价格
				newGood.put("goods_number", newData.getAsString("goods_number"));
				newGood.put("goods_count", newData.getAsString("goods_number"));
				newGood.put("goods_price", seckilldata.getAsString("discount_price"));
				newGood.put("goods_id", gstock.getGoods_id());
				newGood.put("goods_name", seckilldata.getAsString("goods_name"));
				newGood.put("bs_id", seckilldata.getAsString("bs_id"));
				newGood.put("goods_freight", seckilldata.getAsString("goods_freight"));
				newGood.put("goods_sn", seckilldata.getAsString("goods_sn"));
				systemShippingPrice = systemShippingPrice
						.add(seckilldata.getAsBigDecimal("goods_freight").multiply(BigDecimal.valueOf(1)));
				goodsList.add(newGood);
				// 更新秒杀商品限额
				seckilldata.put("buy_num", (seckilldata.getAsInt("buy_num") + newData.getAsInt("goods_number")));
				goodsActivitySeckillMapper.update(seckilldata);
			} else {
				throw new Exception("秒杀商品已售罄！");
			}
		}
		// 折扣 活动下单
		if ("timelimit".equals(newData.getAsString("addtype"))) {
			newData.put("addtype", "2");
			if (EAUtil.isEmpty(newData.getAsString("pro_id"))) {
				throw new Exception("折扣活动，折扣商品ID不能为空");
			}
			// 校验活动是否结束
			newGood.put("id", newData.getAsString("pro_id"));
			newGood.put("now", EADate.getCurrentTime());
			PageData limitdata = goodsActivityTimelimitMapper.selectByProId(newGood);
			if (EAUtil.isEmpty(limitdata)) {
				throw new Exception("折扣商品有误或折扣时间已过");
			}
			if (limitdata.getAsString("bs_id").equals(business.getAsString("bs_id"))) {
				throw new Exception("不能购买自己的活动商品");
			}
			if (newData.getAsInt("goods_number") < limitdata.getAsInt("min_num")) {
				throw new Exception("折扣商品购买数量有误");
			}else{
				 limitdata.put("min_num", newData.getAsInt("goods_number"));
			}
			newGood.clear();
			newGood.put("sku_id", limitdata.getAsString("stock_id"));
			Stock gstock = goodsMapper.queryStockBySkuId(newGood);
			if (Integer.valueOf(gstock.getStock()) <= 0) {
				throw new Exception("折扣商品已售罄！");
			}
			newGood.put("attr_json", gstock.getAttr_json());
			newGood.put("stock", gstock.getStock()); // 库存
			systemPrice = systemPrice
					.add((limitdata.getAsBigDecimal("discount_price")).multiply(limitdata.getAsBigDecimal("min_num")));// 计算总价价格
			newGood.put("goods_number", newData.getAsString("goods_number"));
			newGood.put("goods_count", newData.getAsString("goods_number"));
			newGood.put("goods_price", limitdata.getAsString("discount_price"));
			newGood.put("goods_id", gstock.getGoods_id());
			newGood.put("goods_name", limitdata.getAsString("goods_name"));
			newGood.put("bs_id", limitdata.getAsString("bs_id"));
			newGood.put("goods_freight", limitdata.getAsString("goods_freight"));
			newGood.put("goods_sn", limitdata.getAsString("goods_sn"));
			systemShippingPrice = systemShippingPrice
					.add(limitdata.getAsBigDecimal("goods_freight").multiply(limitdata.getAsBigDecimal("min_num")));
			goodsList.add(newGood);
		}
		// 套餐活动下单
		if ("compro".equals(newData.getAsString("addtype"))) {
			newData.put("addtype", "3");
			if (EAUtil.isEmpty(newData.getAsString("pro_id"))) {
				throw new Exception("套餐活动，套餐商品ID不能为空");
			}
			// 校验活动是否结束
			newGood.put("compo_id", newData.getAsString("pro_id"));
			newGood.put("type", "1");
			newGood.put("currentTime", EADate.getCurrentTime());
			Compo compdata = goodsActivityCompoMapper.selectCompoInfo(newGood);
			if (EAUtil.isEmpty(compdata)) {
				throw new Exception("折扣时间已过");
			}
			if (EAUtil.isEmpty(compdata.getPros())) {
				throw new Exception("折扣商品有误");
			}
			if (compdata.getBsId().equals(business.getAsString("bs_id"))) {
				throw new Exception("不能购买自己的活动商品");
			}
			if (Integer.valueOf(compdata.getCompoLimitNum()) != 0) {
				if (newData.getAsInt("goods_number") > Integer.valueOf(compdata.getCompoLimitNum())) {
					throw new Exception("套餐商品购买数量有误");
				}
			}
			systemShippingPrice = new BigDecimal(compdata.getCompoPostage());
			newGood.clear();
			for (CompoPro pageData : compdata.getPros()) {
				newGood.put("attr_json", pageData.getAttrJson());
				newGood.put("stock", pageData.getStock()); // 库存
				if (Integer.valueOf(pageData.getStock()) <= 0) {
					throw new Exception("套餐商品已售罄！");
				}
				systemPrice = (new BigDecimal(compdata.getCompoPrice()));// 计算价格
				newGood.put("goods_number", "1");
				newGood.put("goods_count", "1");
				newGood.put("goods_price", pageData.getGoodsPrice());
				newGood.put("goods_id", pageData.getGoodsId());
				newGood.put("goods_name", pageData.getGoodsName());
				newGood.put("sku_id", pageData.getSkuId());
				newGood.put("bs_id", pageData.getGoodsId());
				newGood.put("goods_freight", pageData.getGoodsFreight());
				goodsList.add(newGood);
			}
		}
		newData.put("pay_by_points", "0");
		newData.put("cash_id", "0");
		newData.put("cash_money", "0");
		/**
		 * 是否动态计算快递费用 查询快递费
		 */
		BigDecimal zreo_monety = new BigDecimal(0);
		newData.put("goods_money", systemPrice);// 存入商品价格
		newData.put("calFreight", "0");// 存入商品价格
		if (!"0".equalsIgnoreCase(newData.getAsString("calFreight"))) {
			if (StringUtils.isEmpty(newData.getAsString("address_id"))) {
				throw new Exception("地址不能为空");
			}
			PageData freightPd = calculateFreight(goodsList,
					EAString.stringToInt(newData.getAsString("address_id"), 0));
			BigDecimal freightP = freightPd.getAsBigDecimal("freight");
			systemPrice = systemPrice.add(freightP).subtract(new BigDecimal("0"));
			if (systemPrice.compareTo(zreo_monety) == -1) {
				systemPrice = zreo_monety;
			}
			if (systemPrice.compareTo(
					newData.getAsBigDecimal("pay_by_money").add(newData.getAsBigDecimal("pay_by_points"))) != 0) {
				throw new Exception("金额非法");
			}
			newData.put("shipping_money", freightP);
		} else {
			// 如果设置运费直接加入
			if (systemShippingPrice.compareTo(newData.getAsBigDecimal("shipping_money")) != 0) {
				throw new Exception("运费金额非法");
			}
			if ((systemShippingPrice.add(systemPrice)).compareTo(newData.getAsBigDecimal("pay_by_money")) != 0) {
				throw new Exception("总金额非法");
			}
			newData.put("shipping_money", newData.get("shipping_money"));
			systemPrice = systemPrice.add(newData.getAsBigDecimal("shipping_money")).subtract(new BigDecimal("0"));
		}
		newData.put("order_money", systemPrice);
		if (systemPrice.compareTo(zreo_monety) == 0) {
			newData.put("order_status", "1");
			newData.put("pay_status", "1");
		} else {
			newData.put("order_status", "0");
			newData.put("pay_status", "0");
		}
		addOrder(newData);

		/**
		 * 订单商品插入记录
		 */
		for (PageData pd : goodsList) {
			newGood = new PageData();
			newGood.put("order_id", newData.getAsString("order_id"));
			newGood.put("goods_id", pd.getAsString("goods_id"));
			newGood.put("goods_name", pd.getAsString("goods_name"));
			newGood.put("goods_sn", pd.getAsString("goods_sn"));
			newGood.put("goods_count", pd.getAsString("goods_count"));
			newGood.put("goods_price", pd.getAsString("goods_price"));
			newGood.put("goods_sku_id", pd.getAsString("sku_id"));
			newGood.put("is_gift", (pd.getAsString("is_gift").equals("") ? "0" : pd.getAsString("is_gift")));
			newGood.put("goods_attr", pd.getAsString("attr_json")); // 销售属性
			// 分销部分
			newGood.put("rebate_1", pd.get("rebate_1"));
			newGood.put("rebate_2", pd.get("rebate_2"));
			newGood.put("rebate_3", pd.get("rebate_3"));
			// newGood.put("points_1", pd.get("points_1"));
			// newGood.put("points_2", pd.get("points_2"));
			// newGood.put("points_3", pd.get("points_3"));
			// newGood.put("rebate_points", newData.getAsString("give_points"));
			newGood.put("rebate_type", pd.getAsInt("rebate_type"));
			ordersGoodsMapper.insert(newGood);
			pd.put("order_goods_id", newGood.getAsString("order_goods_id"));
			// 插入成功的同时更新库存表，同时更新商品总库存
			Stock stock = new Stock();
			stock.setSku_id(pd.getAsString("sku_id"));
			int stockCount = pd.getAsInt("stock") - pd.getAsInt("goods_count");
			stock.setStock(String.valueOf(stockCount));
			goodsMapper.updateStockByPid(stock);
			PageData goodspd = new PageData();
			goodspd.put("goods_id", pd.getAsString("goods_id"));
			goodspd.put("goods_stock", stockCount);
			goodsMapper.update(goodspd);
		}
		/* rebateOrderService.updateOrderRebate(newData); */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", newData.getAsString("order_id"));
		/**
		 * 不同商家分单操作
		 */
		PageData bsOrderMapPd = new PageData();
		bsOrderMapPd.put("platform_order_id", newData.getAsString("order_id"));

		for (PageData goods : goodsList) {
			bsOrderMapPd.put("business_id", goods.getAsString("bs_id"));
			List<PageData> borderlist = businessOrderMapper.selectOrderMap(bsOrderMapPd);

			BigDecimal orderMoney = systemPrice;

			BigDecimal goodsMoney = (goods.getAsBigDecimal("goods_price")
					.multiply(goods.getAsBigDecimal("goods_count")));

			BigDecimal shippingMoney = (goods.getAsBigDecimal("goods_freight")
					.multiply(goods.getAsBigDecimal("goods_count")));

			if (EAUtil.isEmpty(borderlist)) {
				// 插入数据
				bsOrderMapPd.put("order_sn", EAString.getFourSn());
				bsOrderMapPd.put("user_id", newData.getAsString("user_id"));
				bsOrderMapPd.put("user_name", user.get(0).getAsString("nick_name"));
				bsOrderMapPd.put("order_money", orderMoney);
				bsOrderMapPd.put("goods_money", goodsMoney);
				bsOrderMapPd.put("shipping_money", shippingMoney);
				bsOrderMapPd.put("shipping_fee", "0");
				bsOrderMapPd.put("order_status", "0");
				bsOrderMapPd.put("shipping_status", "0");
				bsOrderMapPd.put("shipping_type", "1");
				bsOrderMapPd.put("pay_status", "0");
				bsOrderMapPd.put("pay_type", "0");
				bsOrderMapPd.put("is_delete", "0");
				bsOrderMapPd.put("pay_by_money", orderMoney);
				bsOrderMapPd.put("create_time", EADate.getCurrentTime());
				bsOrderMapPd.put("pay_time", EADate.getCurrentTime());
				bsOrderMapPd.put("contact_name", newData.getAsString("contact_name"));
				bsOrderMapPd.put("contact_phone", newData.getAsString("contact_phone"));
				bsOrderMapPd.put("province_id", newData.getAsString("province_id"));
				bsOrderMapPd.put("province", newData.getAsString("province"));
				bsOrderMapPd.put("city_id", newData.getAsString("city_id"));
				bsOrderMapPd.put("city", newData.getAsString("city"));
				bsOrderMapPd.put("area_id", newData.getAsString("area_id"));
				bsOrderMapPd.put("area", newData.getAsString("area"));
				bsOrderMapPd.put("address", newData.getAsString("address"));
				bsOrderMapPd.put("is_mer", newData.getAsString("is_mer"));
				bsOrderMapPd.put("mer_title", newData.getAsString("mer_title"));
				bsOrderMapPd.put("client_remark", newData.getAsString("client_remark"));
				bsOrderMapPd.put("mer_no", newData.getAsString("mer_no"));
				businessOrderMapper.insert(bsOrderMapPd);
				// 插入商家订单商品数据
				bsOrderMapPd.put("order_goods_id", goods.getAsString("order_goods_id"));
				bsOrderMapPd.put("border_id", bsOrderMapPd.getAsString("id"));
				businessOrderMapper.insertOrderGoods(bsOrderMapPd);
			} else if (EAUtil.isNotEmpty(borderlist) && borderlist.size() == 1) {
				PageData data = borderlist.get(0);
				// 修改数据 修改运费，总价，商品总价格
				bsOrderMapPd.put("id", data.getAsString("id"));
				if (!"compro".equals(newData.getAsString("addtype"))) {
					bsOrderMapPd.put("order_money", data.getAsBigDecimal("order_money").add(orderMoney));
				}
				bsOrderMapPd.put("goods_money", data.getAsBigDecimal("goods_money").add(goodsMoney));
				bsOrderMapPd.put("shipping_money", data.getAsBigDecimal("shipping_money").add(shippingMoney));
				businessOrderMapper.update(bsOrderMapPd);
			} else {
				throw new Exception("商家商品订单发生错误！");
			}
		}
		// throw new Exception("ceshi");
		return newData;
	}

	/**
	 * 商品订单退货(作废 勿删)
	 */
	public boolean updateOrderReturn(PageData pd) throws Exception {
		Page pg = new Page();
		PageData odpds = new PageData();
		PageData returnPd = new PageData();
		Map<String, String> goodspd = new HashMap<String, String>();
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("goods_id"))) {
			throw new Exception("商品id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("order_id"))) {
			throw new Exception("订单id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("goods_sku_id"))) {
			throw new Exception("库存id不能为空");
		}

		/*
		 * //查询商品积分抵扣比例 String ds = ""; Map<String,PageData> result = new
		 * HashMap<String,PageData>(); List<PageData> resetting =
		 * goodsMapper.selectGoodsSetting(); for(PageData setdata :resetting){
		 * result.put(setdata.getAsString("setting_code"), setdata); } ds =
		 * StringUtils.isEmpty(result.get("00103").getAsString("setting_value"))
		 * ? "1:10" : result.get("00103").getAsString("setting_value");
		 */
		// 订单返回积分，回收积分，返回金额
		BigDecimal returnTempPoints = BigDecimal.valueOf(0);// 退回商品积分
		BigDecimal tempAllPoints = BigDecimal.valueOf(0);// 商品总积分
		BigDecimal totalPayPoint = BigDecimal.valueOf(0);// 支付总积分
		BigDecimal returnPoint = BigDecimal.valueOf(0);// 退回积分
		BigDecimal recovery = BigDecimal.valueOf(0);// 回收赠送积分
		BigDecimal orderPayPoint = BigDecimal.valueOf(0);// 积分支付金额
		BigDecimal returnMoney = BigDecimal.valueOf(0);// 退回金额
		BigDecimal returnGoodsMoney = BigDecimal.valueOf(0);// 退回商品总价
		// 查询订单是否存在
		PageData order = orderMapper.selectById(pd.getAsInt("order_id"));
		if ("business".equals(pd.getAsString("tyep"))) {
			order = businessOrderMapper.selectById(pd.getAsInt("order_id"));
		}
		String[] goodsids = pd.getAsString("goods_id").split(",");
		String[] goodsskuids = pd.getAsString("goods_sku_id").split(",");
		if (goodsids.length == goodsskuids.length) {
			for (int i = 0; i < goodsids.length; i++) {
				goodspd.put(goodsids[i], goodsskuids[i]);
			}
			for (String goods_id : goodsids) {
				if (EAUtil.isNotEmpty(order) && order.getAsInt("order_status") >= 1
						&& order.getAsInt("order_status") <= 3) {
					// 积分支付金额
					orderPayPoint = order.getAsBigDecimal("pay_by_points");
					// 订单积分支付总额
					totalPayPoint = order.getAsBigDecimal("user_pay_points");
					// 要退的商品
					System.out.println(pd.getAsString("goods_id"));
					pd.put("goods_sku_id", goodspd.get(pd.getAsString("goods_id")));
					PageData ogpd = ordersGoodsMapper.selectGRById(pd);
					if (EAUtil.isEmpty(ogpd)) {
						throw new Exception("该商品正在退款或已退款！");
					}
					// 查询商品师傅已退货
					PageData refund = ordersRefundMapper.selectRefund(ogpd.getAsInt("order_goods_id"));
					if (EAUtil.isNotEmpty(refund)) {
						throw new Exception("该商品正在退款或已退款！");
					}
					PageData spd = new PageData();
					spd.put("sku_id", goodspd.get(pd.getAsString("goods_id")));
					Stock goodsStock = goodsMapper.queryStockBySkuId(spd);
					// 回滚库存
					int stockCount = EAString.stringToInt(goodsStock.getStock(), 0) + ogpd.getAsInt("goods_count");
					goodsStock.setStock(String.valueOf(stockCount));
					goodsMapper.updateStockByPid(goodsStock);
					spd.clear();
					spd.put("goods_id", pd.getAsString("goods_id"));
					spd.put("goods_stock", stockCount);
					goodsMapper.update(spd);

					returnGoodsMoney = new BigDecimal(goodsStock.getPrice());
					odpds.put("order_id", pd.getAsString("order_id"));
					pg.setPd(odpds);
					List<PageData> oglist = ordersGoodsMapper.selectByMap(pg);
					List<PageData> goodslist = new ArrayList<PageData>();
					if (EAUtil.isNotEmpty(oglist)) {
						for (PageData pageData : oglist) {
							PageData gpd = goodsMapper.selectById(pageData.getAsInt("goods_id"));
							goodslist.add(gpd);
							if (pageData.getAsString("goods_id").equals(pd.getAsString("goods_id"))) {
								returnGoodsMoney = returnGoodsMoney.multiply(pageData.getAsBigDecimal("goods_count"));
								if (gpd.getAsString("goods_id").equals(goods_id)) {
									// 积分支付 返回积分为零 回收赠送商品积分
									if (orderPayPoint.compareTo(BigDecimal.valueOf(0)) < 1) {
										returnTempPoints = BigDecimal.valueOf(0);
										recovery = recovery.add(gpd.getAsBigDecimal("give_points")
												.multiply(pageData.getAsBigDecimal("goods_count")));
									} else {
										returnTempPoints = returnPoint.add(gpd.getAsBigDecimal("pay_points")
												.multiply(pageData.getAsBigDecimal("goods_count")));
										// 非积分支付 返回商品积分支付金额
									}
								}
							}
							if (orderPayPoint.compareTo(BigDecimal.valueOf(0)) < 1) {
								tempAllPoints = BigDecimal.valueOf(0);
							} else {
								tempAllPoints = tempAllPoints.add(gpd.getAsBigDecimal("pay_points")
										.multiply(pageData.getAsBigDecimal("goods_count")));
							}
						}
					}
					// 积分支付 返回积分
					if (returnTempPoints.compareTo(BigDecimal.valueOf(0)) > 0) {
						returnPoint = returnTempPoints.multiply(totalPayPoint).divide(tempAllPoints, 2,
								RoundingMode.FLOOR);
					} else {
						// 非积分支付返回退回商品总价
						returnPoint = returnTempPoints;
					}
					BigDecimal goodsPayPointMoney = BigDecimal.valueOf(0);
					// 返回积分抵扣金额 goodsPayPointMoney
					if (returnTempPoints.compareTo(BigDecimal.valueOf(0)) > 0) {
						goodsPayPointMoney = returnTempPoints.multiply(orderPayPoint).divide(tempAllPoints, 2,
								RoundingMode.FLOOR);
					}
					// 积分支付 返回金额
					if (returnPoint.compareTo(BigDecimal.valueOf(0)) > 0) {
						returnMoney = returnGoodsMoney.subtract(goodsPayPointMoney);
					} else {
						// 非积分支付返回退回商品总价
						returnMoney = returnGoodsMoney;
					}
					returnPd.put("user_id", pd.getAsString("user_id"));
					returnPd.put("order_id", pd.getAsString("order_id"));
					returnPd.put("refund_points", returnPoint);
					returnPd.put("refund_money", returnMoney);
					returnPd.put("resive_points", recovery);
					returnPd.put("order_goods_id", ogpd.getAsString("order_goods_id"));
					returnPd.put("refund_no", EAString.getFourSn());
					returnPd.put("refund_num", ogpd.getAsString("goods_count"));
					returnPd.put("refund_status", "1");
					if ("0".equals(order.getAsString("pay_type")) || "3".equals(order.getAsString("pay_type"))) {
						returnPd.put("refund_type", "2");
					}
					if ("2".equals(order.getAsString("pay_type"))) {
						returnPd.put("refund_type", "1");
					}
					returnPd.put("creator", "administrator");
					returnPd.put("refund_reason", pd.getAsString("refund_reason"));
					returnPd.put("refund_images", pd.getAsString("refund_images"));
					returnPd.put("refund_remark", pd.getAsString("refund_remark"));
					returnPd.put("create_time", EADate.getCurrentTime());
					ordersRefundMapper.insert(returnPd);
					// 退款单日志
					PageData refundLog = new PageData();
					refundLog.put("refund_id", returnPd.get("refund_id"));
					refundLog.put("log_note", "退款状态：审核中。退款金额为:" + returnMoney + ",退回积分为：" + returnPoint + ",系统回收积分为："
							+ recovery + "。返还的金额和积分未加入该用户的钱包");
					refundLog.put("creator", "administrator");
					refundLog.put("create_time", EADate.getCurrentTime());
					ordersRefundMapper.insertRefundLog(refundLog);
					// 更改订单商品数据
					PageData orderGoods = new PageData();
					orderGoods.put("order_goods_id", ogpd.getAsString("order_goods_id"));
					orderGoods.put("is_refund", "1");
					ordersGoodsMapper.update(orderGoods);
				} else {
					throw new Exception("已发货的商品无法退款");
				}
			}
			// throw new Exception("订单商品有误！");
			return true;
		} else {
			throw new Exception("订单商品有误！");
		}
	}

	/**
	 * 统计各个订单状态订单数
	 */
	public PageData statisticsOrderStatus(PageData pd){
		
		pd.put("orderType", "noPay");
		pd.put("noPay", businessOrderMapper.statisticsOrderStatus(pd));
		pd.put("orderType", "noSign");
		pd.put("noSign", businessOrderMapper.statisticsOrderStatus(pd));
		pd.put("orderType", "noSend");
		pd.put("noSend", businessOrderMapper.statisticsOrderStatus(pd));
		pd.put("orderType", "noComment");
		pd.put("noComment", businessOrderMapper.statisticsOrderStatus(pd));
		return pd;
	}
	
	/**
	 * 统计商家月份销售金额
	 * 
	 * 
	 */
	public List<PageData> statisticsOrderByMonth(PageData pd){
		return businessOrderMapper.statisticsOrderByMonth(pd);
	}
	
	/**
	 * 统计访客量
	 * @param pd
	 * @return
	 */
	public Integer selectVisitCount(PageData pd) {
		return businessOrderMapper.selectVisitCount(pd);
	}
	/**
	 * 统计商铺销售总额
	 * @param pd
	 * @return
	 */
	public String statisticsOrderMoeny(PageData pd) {
		return businessOrderMapper.statisticsOrderMoeny(pd);
	}
	/**
	 * 查询最近五天的访客量
	 * @param pd
	 * @return
	 */
	public List<PageData> selectVisitRecord(PageData pd){
		return businessOrderMapper.selectVisitRecord(pd);
	}
	/**
	 * 获取商家订单
	 * 
	 * @param Id
	 * @return
	 */
	public BusinessOrder getBusinessOrderByID(Integer Id) {
		return businessOrderMapper.selectEntityByID(Id);
	}

	/**
	 * 修改商家订单
	 * 
	 * @param bspd
	 */
	public void editBusinessOrder(PageData bspd) {
		businessOrderMapper.update(bspd);
	}
	

}