package com.easaa.rebate.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.code.dao.UserPayLogMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.Sender;
import com.easaa.course.dao.DeviceMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsMapper;
import com.easaa.order.dao.BusinessOrderMapper;
import com.easaa.order.dao.OrderMapper;
import com.easaa.order.dao.OrdersGoodsMapper;
import com.easaa.order.dao.OrdersLogMapper;
import com.easaa.order.entity.BusinessOrder;
import com.easaa.order.service.OrderService;
import com.easaa.order.service.OrdersGoodsService;
import com.easaa.rebate.dao.RebateOrderMapper;
import com.easaa.rebate.dao.RebateStatsMapper;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserMapper;
import com.easaa.user.dao.UserPointsLogMapper;
import com.easaa.user.service.UserService;

@Service
public class RebateOrderService extends EABaseService {
	@Autowired
	private RebateOrderMapper rebateOrderMapper;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrdersLogMapper ordersLogMapper;

	@Autowired
	private UserPayLogMapper userPayLogMapper;

	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private UserAccountLogMapper userAccountLogMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OrdersGoodsService ordersGoodsService;

	@Autowired
	private RebateConfigService rebateConfigService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserPointsLogMapper userPointsLogMapper;

	@Autowired
	private RebateStatsMapper rebateStatsMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private OrdersGoodsMapper ordersGoodsMapper;

	@Autowired
	private DeviceMapper deviceMapper;

	@Autowired
	private BusinessOrderMapper businessOrderMapper;

	@Override
	public EADao getDao() {
		return rebateOrderMapper;
	}

	/**
	 * 获取用户返利订单
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getUserOrder(PageData pd) {
		Page page = new Page();
		page.setPd(pd);
		return rebateOrderMapper.selectByPage(page);
	}

	/**
	 * 查询一级会员下面的所有订单数量
	 */
	public Integer selectOrderCount(PageData pd) {
		return rebateOrderMapper.selectOrderCount(pd);
	}

	/**
	 * 获取以及用户返利订单总数
	 * 
	 * @param pd
	 * @return
	 */
	public int getUserAllOrder(PageData pd) {
		List<PageData> userlist = new ArrayList<PageData>();
		Page page = new Page();
		int orderNumber = 0;
		userlist.add(pd);
		pd.put("rec_user_id", pd.getAsString("user_id"));
		userlist.addAll(rebateStatsMapper.selectification(pd));
		pd.remove("rec_user_id");
		for (PageData pageData : userlist) {
			pd.put("rebate_status", "5");
			pd.put("user_1", pageData.getAsString("user_id"));
			page.setPd(pd);
			orderNumber += rebateOrderMapper.selectByPage(page).size();
		}
		return orderNumber;
	}

	/**
	 * 获取用户返利订单(分页)
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getUserOrderPage(Page page) {
		return rebateOrderMapper.selectByMapPage(page);
	}

	/**
	 * 插入一条购物返佣订单
	 * 
	 * @param order
	 *            订单对象, 必须包含商品列表(商品完整信息)
	 */
	public void updateOrderRebate(PageData order) {
		PageData cOrder = new PageData();
		cOrder.put("order_id", order.get("order_id"));
		cOrder.put("order_sn", order.get("order_sn"));
		Integer userId = order.getAsInt("user_id");
		// 获取用户的推荐会员
		PageData users = userService.getAllParentByUserID(userId);
		if (EAUtil.isEmpty(users)) {// 下单用户有没有上级
			cOrder.put("rebate_status", -1);// 本单无返佣
			cOrder.put("rebate_money", "0");// 本单无返佣设置返佣金额为0
			orderService.edit(cOrder);
			return;
		}
		// 查询商品
		List<PageData> goodsList = ordersGoodsService.getByOrderID(order.getAsInt("order_id"));
		BigDecimal rebate1 = BigDecimal.valueOf(0), rebate2 = BigDecimal.valueOf(0), rebate3 = BigDecimal.valueOf(0),
				totalRebate = BigDecimal.valueOf(0), points1 = BigDecimal.valueOf(0), points2 = BigDecimal.valueOf(0),
				points3 = BigDecimal.valueOf(0);
		PageData rConfig = rebateConfigService.getConfig();
		if (EAUtil.isEmpty(rConfig) || rConfig.getAsString("is_open").equals("0")) {// 系统关闭分销
			cOrder.put("rebate_status", -1);// 本单无返佣
			cOrder.put("rebate_money", "0");// 本单无返佣设置返佣金额为0
			orderService.edit(cOrder);
			return;
		}
		for (int i = 0; i < goodsList.size(); i++) {
			PageData goods = goodsList.get(i);
			if (goods.getAsInt("rebate_type") == 2) {// 不参与分销
				continue;
			} else if (goods.getAsInt("rebate_type") == 1) {// 自定义分销(直接返回金额，积分)
				rebate1 = rebate1.add(goods.getAsBigDecimal("rebate_1")
						.multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count"))));
				rebate2 = rebate2.add(goods.getAsBigDecimal("rebate_2")
						.multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count"))));
				rebate3 = rebate3.add(goods.getAsBigDecimal("rebate_3")
						.multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count"))));
				/*
				 * points1 = points1.add(goods.getAsBigDecimal("points_1")
				 * .multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count"
				 * )))); points2 = points2.add(goods.getAsBigDecimal("points_2")
				 * .multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count"
				 * )))); points3 = points3.add(goods.getAsBigDecimal("points_3")
				 * .multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count"
				 * ))));
				 */
			} else {// 继承系统的分销（计算系统比例）
				rebate1 = rebate1.add(rConfig.getAsBigDecimal("rebate_1").multiply(goods.getAsBigDecimal("goods_price"))
						.multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count")).divide(BigDecimal.valueOf(100),
								2, RoundingMode.FLOOR)));
				rebate2 = rebate2.add(rConfig.getAsBigDecimal("rebate_2").multiply(goods.getAsBigDecimal("goods_price"))
						.multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count")).divide(BigDecimal.valueOf(100),
								2, RoundingMode.FLOOR)));
				rebate3 = rebate3.add(rConfig.getAsBigDecimal("rebate_3").multiply(goods.getAsBigDecimal("goods_price"))
						.multiply(BigDecimal.valueOf(goods.getAsInteger("goods_count")).divide(BigDecimal.valueOf(100),
								2, RoundingMode.FLOOR)));
				/*
				 * points1 = points1.add(
				 * rConfig.getAsBigDecimal("points_1").multiply(goods.
				 * getAsBigDecimal("rebate_points")).multiply(
				 * BigDecimal.valueOf(goods.getAsInteger("goods_count")).divide(
				 * BigDecimal.valueOf(100)))); points2 = points1.add(
				 * rConfig.getAsBigDecimal("points_2").multiply(goods.
				 * getAsBigDecimal("rebate_points")).multiply(
				 * BigDecimal.valueOf(goods.getAsInteger("goods_count")).divide(
				 * BigDecimal.valueOf(100)))); points3 = points1.add(
				 * rConfig.getAsBigDecimal("points_3").multiply(goods.
				 * getAsBigDecimal("rebate_points")).multiply(
				 * BigDecimal.valueOf(goods.getAsInteger("goods_count")).divide(
				 * BigDecimal.valueOf(100))));
				 */
			}
		}
		System.out.println(rebate1 + "----" + rebate2 + "----" + rebate3);
		PageData user1 = users.getAsPageData("p1");
		PageData user2 = users.getAsPageData("p2");
		PageData user3 = users.getAsPageData("p3");
		if (EAUtil.isNotEmpty(user1)) {// 如果有一级
			cOrder.put("rebate_1", rebate1);
			// cOrder.put("points_1", points1);
			cOrder.put("user_1", user1.get("user_id"));
			totalRebate = totalRebate.add(rebate1);
		}
		if (EAUtil.isNotEmpty(user2)) {// 如果有二级
			cOrder.put("rebate_2", rebate2);
			// cOrder.put("points_2", points2);
			cOrder.put("user_2", user2.get("user_id"));
			totalRebate = totalRebate.add(rebate2);
		}
		if (EAUtil.isNotEmpty(user3)) {// 如果有三级
			cOrder.put("rebate_3", rebate3);
			// cOrder.put("points_3", points3);
			cOrder.put("user_3", user3.get("user_id"));
			totalRebate = totalRebate.add(rebate3);
		}
		cOrder.put("total_rebate", totalRebate);// 订单总共返佣
		cOrder.put("order_type", "1");
		cOrder.put("rebate_status", "0");// 等待付款
		cOrder.put("rebate_time", EADate.getCurrentTime());// 等待付款
		rebateOrderMapper.insert(cOrder);
		PageData buyOrder = new PageData();
		buyOrder.put("order_id", order.get("order_id"));
		buyOrder.put("rebate_money", totalRebate);
		orderService.edit(buyOrder);
	}

	/**
	 * 商品支付微信回调
	 * 
	 * @param param
	 * @return
	 */
	public boolean doWechartCall(PageData param) {
		/**
		 * 更新订单状态
		 */
		String orderNo = param.getAsString("out_trade_no"); // 商品订单号
		PageData order = orderMapper.selectByOrderSn(orderNo);
		if ("1".equalsIgnoreCase(order.getAsString("pay_status"))) { // 避免微信多次回调
			return true;
		}
		/**
		 * 维护用户关系
		 */
		if (StringUtils.isNotEmpty(order.getAsString("recommend_user"))) {
			// 公众号购买推荐人不等于空 有可能是app分享 如果是app分享此字段存储的是userid
			// 如果能直接转成整数类型说明是
			System.out.println("回调1此处获取的分享着的id是<><><>><><><><<><>><<><><><><><><><><><>><><><><><<>><"
					+ order.getAsString("recommend_user"));
			PageData recommendUser = null;
			try {
				recommendUser = userService.getById(Integer.parseInt(order.getAsString("recommend_user")));
			} catch (Exception e) {
				// 说明是公众号分享
				recommendUser = userService.selectByOpenId(StringUtils.trim(order.getAsString("recommend_user")));
			}
			System.out.println("回调2查出的分享者为<><><>><><><><<><>><<><><><><><><><><><>><><><><><<>><"
					+ recommendUser.getAsString("user_id"));
			if (recommendUser != null) { // 说明推荐人存在
				if (StringUtils.isEmpty(recommendUser.getAsString("extension_juri"))
						|| recommendUser.getAsString("extension_juri").equals("1")) { // 检测用户的推广权限是否开启
					/**
					 * 查询被推荐人
					 */
					PageData passiveUser = userService.getById(order.getAsInteger("user_id"));
					// 维护用户关系
					// 防止多种事物嵌套
					try {
						relationship(recommendUser.getAsInt("user_id"), passiveUser.getAsInt("user_id"), 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		order.put("pay_status", "1");
		order.put("pay_type", param.getAsString("pay_type"));
		order.put("order_status", "2");
		order.put("shipping_status", "2");
		orderMapper.update(order);
		// 更改拆分订单状态
		Page buspage = new Page();
		PageData bspd = new PageData();
		bspd.put("platform_order_id", order.getAsString("order_id"));
		buspage.setPd(bspd);
		List<PageData> busorder = businessOrderMapper.selectAllByMap(buspage);
		if (EAUtil.isNotEmpty(busorder)) {
			for (PageData pageData : busorder) {
				pageData.put("pay_status", "1");
				pageData.put("pay_type", param.getAsString("pay_type"));
				pageData.put("order_status", "2");
				pageData.put("shipping_status", "2");
				businessOrderMapper.update(pageData);
			}
		}
		// 购物返佣
		updateOrderRebate(order);
		PageData account = userAccountMapper.selectByUserId(EAString.stringToInt(order.getAsString("user_id"), 0));
		if (account == null) { // 用户没有钱包 创建钱包
			account = new PageData();
			account.put("user_id", order.getAsString("user_id"));
			account.put("user_money", "0");
			account.put("frozen_money", "0");
			account.put("charge_money", "0");
			account.put("withdraw_money", "0");
			account.put("order_money", "0");
			account.put("rebate_money", "0");
			account.put("frozen_points", "0");
			account.put("user_points", "0");
			account.put("charge_points", "0");
			account.put("use_points", "0");
			account.put("order_points", "0");
			account.put("rebate_points", "0");
			account.put("share_points", "0");
			account.put("change_time", EADate.getCurrentTime());
			account.put("tea_hoursubsidy", "0");
			userAccountMapper.insert(account);
			account = userAccountMapper.selectByUserId(EAString.stringToInt(order.getAsString("user_id"), 0));
		}
		// 用户余额
		Integer userPoints = account.getAsInteger("user_points") == null ? 0 : account.getAsInteger("user_points");
		Integer orderPoints = account.getAsInteger("order_points") == null ? 0 : account.getAsInteger("order_points");

		// 用户订单金额
		BigDecimal accountOrderMoney = account.getAsBigDecimal("order_money") == null ? BigDecimal.valueOf(0)
				: account.getAsBigDecimal("order_money");
		account.put("order_money", accountOrderMoney.add(order.getAsBigDecimal("pay_by_money")));
		userAccountMapper.update(account);
		/**
		 * 更改 账户积分
		 */
		if (StringUtils.isEmpty(order.getAsString("user_pay_points")) || order.getAsInteger("user_pay_points") == 0) {
			Integer givePoints = order.getAsInteger("give_points");
			if (givePoints != null && givePoints != 0) {
				account.put("user_points", userPoints + givePoints);
				account.put("order_points", orderPoints + givePoints);
				userAccountMapper.update(account);
			}
		}
		/**
		 * 插入支付日志
		 */
		PageData payLog = new PageData();
		payLog.put("pay_money", order.getAsString("pay_by_money"));
		payLog.put("pay_type", param.getAsString("pay_type"));
		payLog.put("pay_time", EADate.getCurrentTime());
		payLog.put("user_id", order.getAsString("user_id"));
		payLog.put("pay_note", "购买商品支付");
		payLog.put("pay_source", "2");
		payLog.put("pay_order", order.getAsString("order_sn"));
		userPayLogMapper.insert(payLog);
		/**
		 * 插入订单日志
		 */
		payLog.clear();
		payLog.put("order_id", order.getAsString("order_id"));
		payLog.put("order_status", order.getAsString("order_status"));
		payLog.put("shipping_status", order.getAsString("shipping_status"));
		payLog.put("pay_status", order.getAsString("pay_status"));
		payLog.put("creator", "administrator");// 日志操作人（admin）
		payLog.put("create_time", EADate.getCurrentTime());
		payLog.put("log_note", "用户支付");
		ordersLogMapper.insert(payLog);
		/**
		 * 查询返点订单 更改订单状态
		 */
		Page page = new Page();
		PageData rebateParam = new PageData();
		rebateParam.put("order_id", order.getAsString("order_id"));
		page.setPd(rebateParam);
		List<PageData> rebatePds = rebateOrderMapper.selectByMap(page);
		if (rebatePds != null && rebatePds.size() > 0) {
			rebateParam = rebatePds.get(0);
			rebateParam.put("rebate_status", 1);
			rebateOrderMapper.update(rebateParam);
		}
		/**
		 * 积分变动明细
		 */
		rebateParam.clear();
		rebateParam.put("user_id", order.getAsString("user_id"));
		rebateParam.put("log_pay_order", order.getAsString("order_sn"));
		rebateParam.put("log_time", EADate.getCurrentTime());
		rebateParam.put("log_role", "1");
		if (StringUtils.isNotEmpty(order.getAsString("user_pay_points"))
				&& !"0".equalsIgnoreCase(order.getAsString("user_pay_points"))) {
			rebateParam.put("log_points", order.getAsString("user_pay_points"));
			rebateParam.put("log_expenditure", "0"); // 支出
			rebateParam.put("log_type", "3");
			rebateParam.put("log_note", "商品支付：订单  " + order.getAsString("order_sn") + " 支付："
					+ order.getAsString("user_pay_points") + " 积分");
			userPointsLogMapper.insert(rebateParam);
		} else {
			if (StringUtils.isNotEmpty(order.getAsString("give_points"))
					&& !"0".equalsIgnoreCase(order.getAsString("give_points"))) {
				rebateParam.put("log_points", order.getAsString("give_points"));
				rebateParam.put("log_expenditure", "1"); // 支出
				rebateParam.put("log_type", "2");
				rebateParam.put("log_note", "商品购买：订单  " + order.getAsString("order_sn") + " 赠送："
						+ order.getAsString("give_points") + " 积分");
				userPointsLogMapper.insert(rebateParam);
			}
		}
		List<PageData> ordergoods = ordersGoodsMapper.selectByOrderId(order.getAsInt("order_id"));
		for (PageData pageData : ordergoods) {
			// 变更商品购买数量
			PageData goods = goodsMapper.selectById(pageData.getAsInt("goods_id"));
			goods.put("virtual_sales",
					EAString.stringToInt(goods.getAsString("virtual_sales"), 0) + pageData.getAsInt("goods_count"));
			goodsMapper.update(goods);
			// 查询是否为设备 修改设备状态为出售1
			PageData devpd = new PageData();
			Page devpage = new Page();
			devpd.put("dv_device_id", pageData.getAsString("goods_id"));
			devpage.setPd(devpd);
			List<PageData> dvdata = deviceMapper.selectByMap(devpage);
			if (EAUtil.isNotEmpty(dvdata)) {
				PageData devdata = dvdata.get(0);
				devdata.put("dv_status", "1");
				deviceMapper.update(devdata);
				goods.put("is_on_sale", "0");
				goodsMapper.update(goods);
			}
		}
		PageData user = userMapper.getUserInfoById(order.getAsInt("user_id"));
		if (EAUtil.isNotEmpty(user)) {
			String content = "";
			if (param.getAsString("pay_type").equals("1")) {
				String phone = user.getAsString("phone");
				String[] userid = new String[] { order.getAsString("user_id") };
				content = "会员：" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用支付宝支付了 "
						+ order.getAsBigDecimal("pay_by_money") + " 元,购买了国士风商品！" + " 赠送："
						+ order.getAsString("give_points") + " 积分 ,我们将在24小时内配送,谢谢光临！";
				if (EAUtil.isEmpty(phone)) {
					phone = order.getAsString("contact_phone");
				}
				Sender.sendUserMessage(userid, content, 3, phone);
			} else if (param.getAsString("pay_type").equals("0") || param.getAsString("pay_type").equals("3")) {
				String phone = user.getAsString("phone");
				String[] userid = new String[] { order.getAsString("user_id") };
				content = "会员：" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 "
						+ order.getAsBigDecimal("pay_by_money") + " 元,购买了国士风商品！ " + " 赠送："
						+ order.getAsString("give_points") + " 积分 ,我们将在24小时内配送,谢谢光临！";
				if (EAUtil.isEmpty(phone)) {
					phone = order.getAsString("contact_phone");
				}
				Sender.sendUserMessage(userid, content, 3, phone);
			}
		}
		return true;
	}

	/**
	 * 商家订单商品支付微信回调
	 * 
	 * @param param
	 * @return
	 */
	public boolean doWechartBsCall(PageData param) {
		/**
		 * 更新订单状态
		 */
		String orderNo = param.getAsString("out_trade_no"); // 商品订单号
		PageData order = businessOrderMapper.selectByOrderSn(orderNo);
		if ("1".equalsIgnoreCase(order.getAsString("pay_status"))) { // 避免微信多次回调
			return true;
		}
		order.put("pay_status", "1");
		order.put("pay_type", param.getAsString("pay_type"));
		order.put("order_status", "2");
		order.put("shipping_status", "2");
		businessOrderMapper.update(order);
		PageData account = userAccountMapper.selectByUserId(EAString.stringToInt(order.getAsString("user_id"), 0));
		if (account == null) { // 用户没有钱包 创建钱包
			account = new PageData();
			account.put("user_id", order.getAsString("user_id"));
			account.put("user_money", "0");
			account.put("frozen_money", "0");
			account.put("charge_money", "0");
			account.put("withdraw_money", "0");
			account.put("order_money", "0");
			account.put("rebate_money", "0");
			account.put("frozen_points", "0");
			account.put("user_points", "0");
			account.put("charge_points", "0");
			account.put("use_points", "0");
			account.put("order_points", "0");
			account.put("rebate_points", "0");
			account.put("share_points", "0");
			account.put("change_time", EADate.getCurrentTime());
			account.put("tea_hoursubsidy", "0");
			userAccountMapper.insert(account);
			account = userAccountMapper.selectByUserId(EAString.stringToInt(order.getAsString("user_id"), 0));
		}
		// 用户余额
		Integer userPoints = account.getAsInteger("user_points") == null ? 0 : account.getAsInteger("user_points");
		Integer orderPoints = account.getAsInteger("order_points") == null ? 0 : account.getAsInteger("order_points");

		// 用户订单金额
		BigDecimal accountOrderMoney = account.getAsBigDecimal("order_money") == null ? BigDecimal.valueOf(0)
				: account.getAsBigDecimal("order_money");
		account.put("order_money", accountOrderMoney.add(order.getAsBigDecimal("pay_by_money")));
		userAccountMapper.update(account);
		/**
		 * 更改 账户积分
		 */
		if (StringUtils.isEmpty(order.getAsString("user_pay_points")) || order.getAsInteger("user_pay_points") == 0) {
			Integer givePoints = order.getAsInteger("give_points");
			if (givePoints != null && givePoints != 0) {
				account.put("user_points", userPoints + givePoints);
				account.put("order_points", orderPoints + givePoints);
				userAccountMapper.update(account);
			}
		}
		/**
		 * 插入支付日志
		 */
		PageData payLog = new PageData();
		payLog.put("pay_money", order.getAsString("pay_by_money"));
		payLog.put("pay_type", param.getAsString("pay_type"));
		payLog.put("pay_time", EADate.getCurrentTime());
		payLog.put("user_id", order.getAsString("user_id"));
		payLog.put("pay_note", "购买商品支付");
		payLog.put("pay_source", "2");
		payLog.put("pay_order", order.getAsString("order_sn"));
		userPayLogMapper.insert(payLog);
		/**
		 * 插入订单日志
		 */
		payLog.clear();
		payLog.put("order_id", order.getAsString("id"));
		payLog.put("order_status", order.getAsString("order_status"));
		payLog.put("shipping_status", order.getAsString("shipping_status"));
		payLog.put("pay_status", order.getAsString("pay_status"));
		payLog.put("creator", "administrator");// 日志操作人（admin）
		payLog.put("create_time", EADate.getCurrentTime());
		payLog.put("log_note", "用户支付");
		ordersLogMapper.insert(payLog);
		// 更新商品购买数量
		List<PageData> ordergoods = ordersGoodsMapper.selectByOrderId(order.getAsInt("platform_order_id"));
		for (PageData pageData : ordergoods) {
			if (pageData.getAsString("order_goods_id").equals(order.getAsString("order_goods_id"))) {
				// 变更商品购买数量
				PageData goods = goodsMapper.selectById(pageData.getAsInt("goods_id"));
				goods.put("virtual_sales",
						EAString.stringToInt(goods.getAsString("virtual_sales"), 0) + pageData.getAsInt("goods_count"));
				goodsMapper.update(goods);
			}
		}
		// 更新活动购买量

		PageData user = userMapper.getUserInfoById(order.getAsInt("user_id"));
		if (EAUtil.isNotEmpty(user)) {
			String content = "";
			if (param.getAsString("pay_type").equals("1")) {
				String phone = user.getAsString("phone");
				String[] userid = new String[] { order.getAsString("user_id") };
				content = "会员：" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用支付宝支付了 "
						+ order.getAsBigDecimal("pay_by_money") + " 元,购买了国士风商品！" + " 赠送："
						+ order.getAsString("give_points") + " 积分 ,我们将在24小时内配送,谢谢光临！";
				if (EAUtil.isEmpty(phone)) {
					phone = order.getAsString("contact_phone");
				}
				Sender.sendUserMessage(userid, content, 3, phone);
			} else if (param.getAsString("pay_type").equals("0") || param.getAsString("pay_type").equals("3")) {
				String phone = user.getAsString("phone");
				String[] userid = new String[] { order.getAsString("user_id") };
				content = "会员：" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 "
						+ order.getAsBigDecimal("pay_by_money") + " 元,购买了国士风商品！ " + " 赠送："
						+ order.getAsString("give_points") + " 积分 ,我们将在24小时内配送,谢谢光临！";
				if (EAUtil.isEmpty(phone)) {
					phone = order.getAsString("contact_phone");
				}
				Sender.sendUserMessage(userid, content, 3, phone);
			}
		}
		return true;
	}

	/**
	 * 商品支付微信回调
	 * 
	 * @param param
	 * @return
	 */
	public boolean doDuyunWechartCall(PageData param) {
		/**
		 * 更新订单状态
		 */
		String orderNo = param.getAsString("out_trade_no"); // 商品订单号
		PageData order = orderMapper.selectOneByOrderSn(orderNo);
		if ("1".equalsIgnoreCase(order.getAsString("pay_status"))) { // 避免微信多次回调
			return true;
		}
		order.put("pay_status", "1");
		order.put("pay_type", param.getAsString("pay_type"));
		order.put("order_status", "2");
		order.put("shipping_status", "2");
		orderMapper.update(order);
		// 购物返佣
	//	updateOrderRebate(order);
		PageData account = userAccountMapper.selectByUserId(EAString.stringToInt(order.getAsString("user_id"), 0));
		if (account == null) { // 用户没有钱包 创建钱包
			account = new PageData();
			account.put("user_id", order.getAsString("user_id"));
			account.put("user_money", "0");
			account.put("frozen_money", "0");
			account.put("charge_money", "0");
			account.put("withdraw_money", "0");
			account.put("order_money", "0");
			account.put("rebate_money", "0");
			account.put("frozen_points", "0");
			account.put("user_points", "0");
			account.put("charge_points", "0");
			account.put("use_points", "0");
			account.put("order_points", "0");
			account.put("rebate_points", "0");
			account.put("share_points", "0");
			account.put("change_time", EADate.getCurrentTime());
			account.put("tea_hoursubsidy", "0");
			userAccountMapper.insert(account);
			account = userAccountMapper.selectByUserId(EAString.stringToInt(order.getAsString("user_id"), 0));
		}
		/**
		 * 插入支付日志
		 */
		PageData payLog = new PageData();
		payLog.put("pay_money", order.getAsString("pay_by_money"));
		payLog.put("pay_type", param.getAsString("pay_type"));
		payLog.put("pay_time", EADate.getCurrentTime());
		payLog.put("user_id", order.getAsString("user_id"));
		payLog.put("pay_note", "购买商品支付");
		payLog.put("pay_source", "2");
		payLog.put("pay_order", order.getAsString("order_sn"));
		userPayLogMapper.insert(payLog);
		/**
		 * 插入订单日志
		 */
		payLog.clear();
		payLog.put("order_id", order.getAsString("order_id"));
		payLog.put("order_status", order.getAsString("order_status"));
		payLog.put("shipping_status", order.getAsString("shipping_status"));
		payLog.put("pay_status", order.getAsString("pay_status"));
		payLog.put("creator", "administrator");// 日志操作人（admin）
		payLog.put("create_time", EADate.getCurrentTime());
		payLog.put("log_note", "用户支付");
		ordersLogMapper.insert(payLog);
		/**
		 * 查询返点订单 更改订单状态
		 */
		Page page = new Page();
		PageData rebateParam = new PageData();
		rebateParam.put("order_id", order.getAsString("user_id"));
		page.setPd(rebateParam);
		List<PageData> rebatePds = rebateOrderMapper.selectByMap(page);
		if (rebatePds != null && rebatePds.size() > 0) {
			rebateParam = rebatePds.get(0);
			rebateParam.put("rebate_status", 1);
			rebateOrderMapper.update(rebateParam);
		}
		List<PageData> ordergoods = ordersGoodsMapper.selectByOrderId(order.getAsInt("order_id"));
		for (PageData pageData : ordergoods) {
			// 变更商品购买数量
			PageData goods = goodsMapper.selectById(pageData.getAsInt("goods_id"));
			goods.put("virtual_sales",
					EAString.stringToInt(goods.getAsString("virtual_sales"), 0) + pageData.getAsInt("goods_count"));
			goodsMapper.update(goods);
			/*
			 * // 查询是否为设备 修改设备状态为出售1 PageData devpd = new PageData(); Page
			 * devpage = new Page(); devpd.put("dv_device_id",
			 * pageData.getAsString("goods_id")); devpage.setPd(devpd);
			 * List<PageData> dvdata = deviceMapper.selectByMap(devpage); if
			 * (EAUtil.isNotEmpty(dvdata)) { PageData devdata = dvdata.get(0);
			 * devdata.put("dv_status", "1"); deviceMapper.update(devdata);
			 * goods.put("is_on_sale", "0"); goodsMapper.update(goods); }
			 */
		}
		return true;
	}

	/**
	 * 维护用户关系
	 * 
	 * @param recId
	 *            推荐人id
	 * @param childId
	 *            被推荐人id
	 * @param recType
	 *            关系级别
	 * @return
	 */
	private PageData relationship(int recId, int childId, int recType) {
		PageData reuslt = new PageData();
		// 根据父id和子id查询关系表里面是否存在关系
		List<PageData> relations = null;
		PageData targetUser = userMapper.selectById(recId);
		PageData childUser = userMapper.selectById(childId);
		Integer child2Count = targetUser.getAsInteger("child_2_count");
		Integer child3Count = targetUser.getAsInteger("child_3_count");
		child2Count = child2Count == null ? 1 : child2Count + 1;
		targetUser.put("child_2_count", child2Count);
		userMapper.update(targetUser); // 跟新 推荐人的二级会员数
		childUser.put("user_1", childId);
		childUser.put("user_2", recId);
		childUser.put("parent_id", recId);
		userMapper.update(childUser); // 更新 一级返点人 二级返点人 和父亲
		PageData pd = new PageData();
		pd.put("rec_user_id", recId);
		pd.put("child_user_id", childId);
		relations = userMapper.selectRelationByCondition(pd);
		if (relations == null || relations.size() == 0) {
			pd.put("rec_type", 2);
			pd.put("create_time", EADate.getCurrentTime());
			userMapper.insertRelation(pd);
		}
		/**
		 * 查看 被推荐人下面是否有直接下级 如果有 统计到推荐人三级
		 */
		Page page = new Page();
		PageData nextPd = new PageData();
		nextPd.put("parent_id", childId);
		page.setPd(nextPd);
		List<PageData> nextChilds = userMapper.selectByMap(page);
		if (nextChilds != null && nextChilds.size() > 0) {
			targetUser.put("child_3_count", child3Count + nextChilds.size());
			// 建立 推荐人 和被推荐人下级的关系
			for (PageData childNextPd : nextChilds) {
				nextPd.put("rec_type", 3);
				nextPd.put("rec_user_id", recId);
				nextPd.put("child_user_id", childNextPd.getAsString("user_id"));
				relations = userMapper.selectRelationByCondition(nextPd);
				if (relations == null || relations.size() == 0) {
					nextPd.put("create_time", EADate.getCurrentTime());
					userMapper.insertRelation(nextPd);
					childNextPd.put("user1", childNextPd.getAsString("user_id"));
					childNextPd.put("user2", childId);
					childNextPd.put("user3", recId);
					userMapper.update(childNextPd);
				}
			}
		}
		// 如果有上级会员
		if (StringUtils.isNotEmpty(targetUser.getAsString("parent_id"))
				&& !targetUser.getAsString("parent_id").equals("0")) {
			PageData parentPd = userMapper.selectById(EAString.stringToInt(targetUser.getAsString("parent_id"), 0));
			if (parentPd != null) {
				childUser.put("user_3", parentPd.getAsString("user_id"));
				userMapper.update(childUser); // 更新三级返点人
				child3Count = parentPd.getAsInteger("child_3_count");
				child3Count = child3Count == null ? 1 : child3Count + 1;
				parentPd.put("child_3_count", child3Count);
				userMapper.update(parentPd);
				pd.clear();
				pd.put("rec_user_id", parentPd.getAsString("user_id"));
				pd.put("child_user_id", childId);
				relations = userMapper.selectRelationByCondition(pd);
				if (relations == null || relations.size() == 0) {
					pd.put("rec_type", 3);
					pd.put("create_time", EADate.getCurrentTime());
					userMapper.insertRelation(pd);
				}
			}
		}
		return reuslt;
	}

	/**
	 * 商城支付余额支付
	 * 
	 * @param pd
	 * 
	 *            order_sn 订单号 user_id 用户id
	 * @throws Exception
	 */
	public void insertBalancePay(PageData pd) throws Exception {
		String orderId = pd.getAsString("order_id"); // 商品订单号
		PageData order = orderMapper.selectById(EAString.stringToInt(orderId, 0));
		if ("1".equalsIgnoreCase(order.getAsString("pay_status"))) { // 避免用户多次点击
			return;
		}
		/**
		 * 更改订单状态
		 */
		order.put("pay_status", "1");
		order.put("pay_type", "2");
		order.put("order_status", "2");
		order.put("shipping_status", "2");
		orderMapper.update(order);
		/**
		 * 维护用户关系
		 */
		if (StringUtils.isNotEmpty(order.getAsString("recommend_user"))) {
			// 公众号购买推荐人不等于空 有可能是app分享 如果是app分享此字段存储的是userid
			// 如果能直接转成整数类型说明是
			System.out.println("余额回调1此处获取的分享着的id是<><><>><><><><<><>><<><><><><><><><><><>><><><><><<>><"
					+ order.getAsString("recommend_user"));
			PageData recommendUser = null;
			try {
				recommendUser = userService.getById(Integer.parseInt(order.getAsString("recommend_user")));
			} catch (Exception e) {
				// 说明是公众号分享
				recommendUser = userService.selectByOpenId(StringUtils.trim(order.getAsString("recommend_user")));
			}
			System.out.println("余额回调2查出的分享者为<><><>><><><><<><>><<><><><><><><><><><>><><><><><<>><"
					+ recommendUser.getAsString("user_id"));
			if (recommendUser != null) { // 说明推荐人存在
				if (StringUtils.isEmpty(recommendUser.getAsString("extension_juri"))
						|| recommendUser.getAsString("extension_juri").equals("1")) { // 检测用户的推广权限是否开启
					/**
					 * 查询被推荐人
					 */
					PageData passiveUser = userService.getById(order.getAsInteger("user_id"));
					// 维护用户关系
					// 防止多种事物嵌套
					try {
						relationship(recommendUser.getAsInt("user_id"), passiveUser.getAsInt("user_id"), 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		// 更改拆分订单状态
		Page buspage = new Page();
		PageData bspd = new PageData();
		bspd.put("platform_order_id", order.getAsString("order_id"));
		buspage.setPd(bspd);
		List<PageData> busorder = businessOrderMapper.selectAllByMap(buspage);
		if (EAUtil.isNotEmpty(busorder)) {
			for (PageData pageData : busorder) {
				pageData.put("pay_status", "1");
				pageData.put("pay_type",pd.getAsString("pay_type"));
				pageData.put("order_status", "2");
				pageData.put("shipping_status", "2");
				businessOrderMapper.update(pageData);
			}
		}
		// 购物返佣
		updateOrderRebate(order);
		// 订单总金额
		BigDecimal orderMoney = order.getAsBigDecimal("pay_by_money");
		String user_id = order.getAsString("user_id");// 下单人
		PageData account = userAccountMapper.selectByUserId(EAString.stringToInt(user_id, 0));
		// 用户余额
		BigDecimal accountMoney = account.getAsBigDecimal("user_money"),
				accountOrderMoney = account.getAsBigDecimal("order_money") == null ? BigDecimal.valueOf(0)
						: account.getAsBigDecimal("order_money");
		Integer userPoints = account.getAsInteger("user_points") == null ? 0 : account.getAsInteger("user_points");
		Integer orderPoints = account.getAsInteger("order_points") == null ? 0 : account.getAsInteger("order_points");
		if (account == null || accountMoney.doubleValue() < 0 || accountMoney.compareTo(orderMoney) == -1) {
			throw new Exception("无法用余额支付，余额不足");
		}
		/**
		 * 更改用户余额 账户积分
		 */
		if (StringUtils.isEmpty(order.getAsString("user_pay_points")) || order.getAsInteger("user_pay_points") == 0) {
			Integer givePoints = order.getAsInteger("give_points");
			if (givePoints != null && givePoints != 0) {
				account.put("user_points", userPoints + givePoints);
				account.put("order_points", orderPoints + givePoints);
			}
		}
		account.put("user_money", accountMoney.subtract(orderMoney));
		account.put("order_money", accountOrderMoney.add(orderMoney));
		userAccountMapper.update(account);
		/**
		 * 更改钱包流水
		 */
		PageData paramEntity = new PageData();
		paramEntity.put("user_id", user_id);
		paramEntity.put("log_money", orderMoney);
		paramEntity.put("log_type", "1");
		paramEntity.put("log_time", EADate.getCurrentTime());
		paramEntity.put("log_symbol", 1); // 支出
		paramEntity.put("log_note", "国士风商品支付");
		paramEntity.put("log_order", order.getAsString("order_sn"));
		userAccountLogMapper.insert(paramEntity);
		/**
		 * 插入支付日志
		 */
		paramEntity.clear();
		paramEntity.put("pay_money", orderMoney);
		paramEntity.put("pay_type", "2");
		paramEntity.put("pay_time", EADate.getCurrentTime());
		paramEntity.put("user_id", user_id);
		paramEntity.put("pay_note", "商品订单支付");
		paramEntity.put("pay_source", "2");
		paramEntity.put("pay_order", order.getAsString("order_sn"));
		userPayLogMapper.insert(paramEntity);
		/**
		 * 更改返点表状态
		 */
		Page page = new Page();
		PageData rebateParam = new PageData();
		rebateParam.put("order_id", order.getAsString("order_id"));
		page.setPd(rebateParam);
		List<PageData> rebatePds = rebateOrderMapper.selectByMap(page);
		if (rebatePds != null && rebatePds.size() > 0) {
			rebateParam = rebatePds.get(0);
			rebateParam.put("rebate_status", 1);
			rebateOrderMapper.update(rebateParam);
		}
		/**
		 * 插入积分明细日志
		 */
		rebateParam.clear();
		rebateParam.put("user_id", order.getAsString("user_id"));
		rebateParam.put("log_pay_order", order.getAsString("order_sn"));
		rebateParam.put("log_time", EADate.getCurrentTime());
		rebateParam.put("log_role", "1");
		if (StringUtils.isNotEmpty(order.getAsString("user_pay_points"))
				&& !"0".equalsIgnoreCase(order.getAsString("user_pay_points"))) {
			rebateParam.put("log_points", order.getAsString("user_pay_points"));
			rebateParam.put("log_expenditure", "0"); // 支出
			rebateParam.put("log_type", "3");
			rebateParam.put("log_note", "商品支付：订单  " + order.getAsString("order_sn") + " 支付："
					+ order.getAsString("user_pay_points") + " 积分");
			userPointsLogMapper.insert(rebateParam);
		} else {
			if (StringUtils.isNotEmpty(order.getAsString("give_points"))
					&& !"0".equalsIgnoreCase(order.getAsString("give_points"))) {
				rebateParam.put("log_points", order.getAsString("give_points"));
				rebateParam.put("log_expenditure", "1"); // 收入
				rebateParam.put("log_type", "2");
				rebateParam.put("log_note", "商品购买：订单  " + order.getAsString("order_sn") + " 赠送："
						+ order.getAsString("give_points") + " 积分");
				userPointsLogMapper.insert(rebateParam);
			}
		}
		// 变更商品购买数量
		List<PageData> ordergoods = ordersGoodsMapper.selectByOrderId(order.getAsInt("order_id"));
		for (PageData pageData : ordergoods) {
			PageData goods = goodsMapper.selectById(pageData.getAsInt("goods_id"));
			goods.put("virtual_sales",
					EAString.stringToInt(goods.getAsString("virtual_sales"), 0) + pageData.getAsInt("goods_count"));
			goodsMapper.update(goods);
			// 查询是否为设备 修改设备状态为出售1 商品下架
			PageData devpd = new PageData();
			Page devpage = new Page();
			devpd.put("dv_device_id", pageData.getAsString("goods_id"));
			devpage.setPd(devpd);
			List<PageData> dvdata = deviceMapper.selectByMap(devpage);
			if (EAUtil.isNotEmpty(dvdata)) {
				PageData devdata = dvdata.get(0);
				devdata.put("dv_status", "1");
				deviceMapper.update(devdata);
				goods.put("is_on_sale", "0");
				goodsMapper.update(goods);
			}
		}
		PageData user = userMapper.getUserInfoById(order.getAsInt("user_id"));
		if (EAUtil.isNotEmpty(user)) {
			String phone = user.getAsString("phone");
			String[] userid = new String[] { order.getAsString("user_id") };
			String content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用余额支付了 "
					+ order.getAsBigDecimal("pay_by_money") + " 元,购买了国士风商品！" + " 赠送：" + order.getAsString("give_points")
					+ " 积分 ,我们将在24小时内配送,谢谢光临！";
			if (EAUtil.isEmpty(phone)) {
				phone = order.getAsString("contact_phone");
			}
			Sender.sendUserMessage(userid, content, 3, phone);
		}
	}
}