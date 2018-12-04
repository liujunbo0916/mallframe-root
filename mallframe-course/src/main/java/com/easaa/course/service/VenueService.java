package com.easaa.course.service;

import java.math.BigDecimal;
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
import com.easaa.course.dao.VenueLimitMapper;
import com.easaa.course.dao.VenueListingsMapper;
import com.easaa.course.dao.VenueMapper;
import com.easaa.course.dao.VenueOrderMapper;
import com.easaa.course.dao.VenuePlaceMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserMapper;

@Service
public class VenueService extends EABaseService {

	@Autowired
	private VenueMapper venueMapper;

	@Autowired
	private VenuePlaceMapper venuePlaceMapper;

	@Autowired
	private VenueListingsMapper venueListingsMapper;

	@Autowired
	private VenueLimitMapper venueLimitMapper;

	@Autowired
	private VenueOrderMapper venueOrderMapper;

	@Autowired
	private UserPayLogMapper userPayLogMapper;

	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private UserAccountLogMapper userAccountLogMapper;
	@Autowired
	private UserMapper userMapper;

	@Override
	public EADao getDao() {
		return venueMapper;
	}

	/**
	 * 场馆列表
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getvenueLists(PageData pd) throws Exception {
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("当前页不能为空");
		}
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(Integer.parseInt(pd.getAsString("currentPage")));
		page.setShowCount(15);
		page.setPd(pd);
		return venueMapper.selectByPage(page);
	}

	/**
	 * 场馆详情
	 * 
	 * @param pd
	 * @return
	 */
	public PageData venueDetails(PageData pd) throws Exception {
		PageData data = new PageData();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("venue_id"))) {
			throw new Exception("venue_id不能为空");
		}
		// 场馆详情
		data.put("venue_details", venueMapper.selectById(pd.getAsInt("venue_id")));
		// 场馆时间
		Page page = new Page();
		page.setPd(pd);
		data.put("venuePlace", venuePlaceMapper.selectByMap(page));
		return data;
	}

	/**
	 * 场馆预约
	 * 
	 * @param pd
	 * @return
	 */
	public boolean insertVenueOrder(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("venue_place_id"))) {
			throw new Exception("venue_place_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("venue_place_name"))) {
			throw new Exception("venue_place_name不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("venue_id"))) {
			throw new Exception("venue_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("venue_name"))) {
			throw new Exception("venue_name不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("venue_address"))) {
			throw new Exception("venue_adress不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("vorder_date"))) {
			throw new Exception("vorder_date不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("vorder_time"))) {
			throw new Exception("vorder_time不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("contact_phone"))) {
			throw new Exception("contact_phone不能为空");
		}
		PageData vlpd = new PageData();
		vlpd.put("limit_target_id", pd.getAsString("venue_place_id"));
		vlpd.put("limit_date", pd.getAsString("vorder_date"));
		vlpd.put("limit_type", "2");
		// 锁定场馆时间
		PageData vldata = venueLimitMapper.selectByPd(vlpd);
		if (EAUtil.isEmpty(vldata)) {
			vlpd.put("limit_listings", pd.getAsString("vorder_time"));
			venueLimitMapper.insert(vlpd);
		} else {
			String[] strs = pd.getAsString("vorder_time").split(",");
			for (String str : strs) {
				if (vldata.getAsString("limit_listings").contains(str)) {
					throw new Exception("场馆场次时间有误！");
				}
			}
			vldata.put("limit_listings", vldata.getAsString("limit_listings") + "," + pd.getAsString("vorder_time"));
			venueLimitMapper.update(vldata);
		}
		String[] timeNum = pd.getAsString("vorder_time").split(",");
		// 插入预约单
		PageData venuedata = venueMapper.selectById(pd.getAsInt("venue_id"));
		if (EAUtil.isNotEmpty(venuedata)) {
			BigDecimal sysmoney = venuedata.getAsBigDecimal("venue_price").multiply(new BigDecimal(timeNum.length));
			pd.put("vorder_money", sysmoney);
		} else {
			throw new Exception("场馆不存在！");
		}
		pd.put("vorder_pay_status", "0");
		pd.put("vorder_status", "0");
		pd.put("vorder_remarks", pd.getAsString("vorder_remarks"));
		pd.put("vorder_create_time", EADate.getCurrentTime());
		pd.put("vorder_sn", EADate.getOrderSnString() + "VR");
		if (venueOrderMapper.insert(pd) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 场馆订单
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectVenueOrder(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("当前页不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("当前页不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("order_status"))) {
			throw new Exception("order_status不能为空");
		}
		if (pd.getAsInt("order_status") == 0) {
			pd.put("vorder_pay_status", "0");
		}
		pd.remove("order_status");
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(Integer.parseInt(pd.getAsString("currentPage")));
		page.setShowCount(15);
		page.setPd(pd);
		List<PageData> data = venueOrderMapper.selectByPage(page);
		for (PageData pageData : data) {
			String time = "";
			String[] str = pageData.getAsString("vorder_time").split(",");
			for (String string : str) {
				PageData vlsdata = venueListingsMapper.selectById(Integer.valueOf(string));
				time += vlsdata.getAsString("listings_start_time") + "--" + vlsdata.getAsString("listings_end_time")
						+ ",";
			}
			pageData.put("venue_pic", venueMapper.selectById(pageData.getAsInt("venue_id")).getAsString("venue_pic"));
			pageData.put("vorder_time", time.substring(0, (time.length() - 1)));
		}
		return data;
	}

	/**
	 * 取消场馆订单
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrder(PageData model) throws Exception {
		if (EAUtil.isEmpty(model.getAsString("vorder_id"))) {
			throw new Exception("vorder_id不能为空！");
		}
		PageData vodata = venueOrderMapper.selectById(model.getAsInt("vorder_id"));
		if (EAUtil.isEmpty(vodata) || !vodata.getAsString("vorder_pay_status").equals("1")) {
			throw new Exception("订单有误或未支付！");
		}
		// 取消锁定场次时间
		updateVenueLimit(vodata);
		// 修改订单装态
		vodata.put("vorder_status", 3);
		venueOrderMapper.update(vodata);
		// 金钱支付，退回钱包
		if (StringUtils.isNotEmpty(vodata.getAsString("vorder_money")) && vodata.getAsInt("vorder_money") > 0) {
			PageData userAccount = userAccountMapper.selectByUserId(model.getAsInt("user_id"));
			BigDecimal oldMoney = userAccount.getAsBigDecimal("user_money");
			BigDecimal orderMoney = userAccount.getAsBigDecimal("order_money");
			userAccount.put("order_money", orderMoney.subtract(vodata.getAsBigDecimal("vorder_money")));
			userAccount.put("user_money", oldMoney.add(vodata.getAsBigDecimal("vorder_money")));
			userAccountMapper.update(userAccount);
			/**
			 * 更改钱包流水
			 */
			PageData payLog = new PageData();
			payLog.put("user_id", model.getAsInt("user_id"));
			payLog.put("log_money", vodata.getAsBigDecimal("vorder_money"));
			payLog.put("log_type", "2");
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_note", "场馆预约取消");
			payLog.put("log_symbol", "0");
			payLog.put("log_order", vodata.getAsString("vorder_sn"));
			userAccountLogMapper.insert(payLog);
		}
		return true;
	}

	/**
	 * 删除场馆订单
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrder(PageData model) throws Exception {
		if (EAUtil.isEmpty(model.getAsString("vorder_id"))) {
			throw new Exception("vorder_id不能为空！");
		}
		PageData vodata = venueOrderMapper.selectById(model.getAsInt("vorder_id"));
		if (EAUtil.isEmpty(vodata) || !vodata.getAsString("vorder_pay_status").equals("0")) {
			throw new Exception("订单有误或已支付！");
		}
		// 取消锁定场次时间
		updateVenueLimit(vodata);
		Page page = new Page();
		page.setPd(vodata);
		venueOrderMapper.delete(page);
		return true;
	}

	/**
	 * 释放场馆锁定场次
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public void updateVenueLimit(PageData vodata) throws Exception {
		// 取消锁定场次时间
		PageData vlpd = new PageData();
		vlpd.put("limit_target_id", vodata.getAsString("venue_place_id"));
		vlpd.put("limit_date", vodata.getAsString("vorder_date"));
		vlpd.put("limit_type", "2");
		// 删除 场馆锁定时间
		PageData vldata = venueLimitMapper.selectByPd(vlpd);
		if (EAUtil.isEmpty(vldata)) {
			throw new Exception("场次有误！");
		}
		String[] strs = vodata.getAsString("vorder_time").split(",");
		String limits = vldata.getAsString("limit_listings");
		for (String str : strs) {
			if (limits.contains((str + ","))) {
				limits = limits.replace((str + ","), "");
			}
			if (limits.contains(("," + str + ","))) {
				limits = limits.replace((str + ","), "");
			}
			if (limits.contains(("," + str))) {
				limits = limits.replace(("," + str), "");
			}
			if (limits.contains(str)) {
				limits = limits.replace(str, "");
			}
		}
		vldata.put("limit_listings", limits);
		if (EAUtil.isEmpty(limits)) {
			Page page = new Page();
			page.setPd(vldata);
			venueLimitMapper.delete(page);
		} else {
			venueLimitMapper.update(vldata);
		}
	}

	/**
	 * 场馆订单（后台）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectVenueOrderAll(Page page) throws Exception {
		List<PageData> data = venueOrderMapper.selectByPage(page);
		for (PageData pageData : data) {
			String time = "";
			String[] str = pageData.getAsString("vorder_time").split(",");
			for (String string : str) {
				PageData vlsdata = venueListingsMapper.selectById(Integer.valueOf(string));
				time += vlsdata.getAsString("listings_start_time") + "--" + vlsdata.getAsString("listings_end_time")
						+ ",";
			}
			pageData.put("vorder_time", time.substring(0, (time.length() - 1)));
		}
		return data;
	}

	/**
	 * 场馆订单byID（后台）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData selectVenueOrderById(Integer id) {
		return venueOrderMapper.selectById(id);
	}

	/**
	 * 场馆订单列表（定时器）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectAllVenueOrder(PageData pd) {
		Page page = new Page();
		page.setPd(pd);
		return venueOrderMapper.selectByMap(page);
	}

	/**
	 * 修改场馆订单（后台）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int updateVenueOrder(PageData model) throws Exception{
		if(model.getAsString("vorder_status").equals("2")){
			PageData vodata = venueOrderMapper.selectById(model.getAsInt("vorder_id"));
			updateVenueLimit(vodata);
		}
		return venueOrderMapper.update(model);
	}

	/**
	 * 删除场馆订单（后台）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int deleteVenueOrder(PageData model) {
		Page page = new Page();
		page.setPd(model);
		return venueOrderMapper.delete(page);
	}

	/**
	 * 场馆支付回调逻辑
	 */
	public boolean doVenueWxCall(PageData pd) {
		// 更改场馆订单支付状态已付款 更改场馆状态已完成
		Page page = new Page();
		pd.put("vorder_sn", pd.getAsString("out_trade_no"));
		page.setPd(pd);
		List<PageData> venueOrders = venueOrderMapper.selectByMap(page);
		if (venueOrders == null || venueOrders.size() != 1) {
			return false;
		}
		PageData order = venueOrders.get(0);

		if ("1".equalsIgnoreCase(order.getAsString("vorder_pay_status"))
				&& "4".equalsIgnoreCase(order.getAsString("vorder_status"))) {
			return true;
		}
		order.put("vorder_status", "4");
		order.put("vorder_pay_status", "1");
		venueOrderMapper.update(order);
		try {
			// 插入支付日志
			PageData payLog = new PageData();
			payLog.put("pay_money", order.getAsString("vorder_money"));
			payLog.put("pay_type", pd.getAsString(pd.getAsString("pay_type")));
			payLog.put("pay_time", EADate.getCurrentTime());
			payLog.put("user_id", order.getAsString("user_id"));
			payLog.put("pay_note", "场馆预定");
			payLog.put("pay_source", "1");
			payLog.put("pay_order", order.getAsString("vorder_sn"));
			userPayLogMapper.insert(payLog);
			/**
			 * 发短信提醒用户预订成功
			 */
			PageData user = userMapper.getUserInfoById(order.getAsInt("user_id"));
			if (EAUtil.isNotEmpty(user)) {
				String content = "";
				String[] userid = null;
				String phone = user.getAsString("phone");
				if (EAUtil.isEmpty(phone)) {
					phone = order.getAsString("contact_phone");
				}
				if (pd.getAsString("pay_type").equals("1")) {
					userid = new String[] { order.getAsString("user_id") };
					content = "会员:" + user.getAsString("nick_name") + "，您好！感谢您在国士风" + "使用支付宝支付了 "
							+ order.getAsBigDecimal("vorder_money") + " 元,预约了国士风场馆: " + order.getAsString("venue_name")
							+ ",请留意订单情况！谢谢光临！";
				} else if (pd.getAsString("pay_type").equals("0") || pd.getAsString("pay_type").equals("3")) {
					content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 "
							+ order.getAsBigDecimal("vorder_money") + " 元,预约了国士风场馆: " + order.getAsString("venue_name")
							+ ",请留意订单情况！谢谢光临！";
				}
				Sender.sendUserMessage(userid, content, 3, phone);

				/*
				 * String str = ""; if(pd.getAsString("pay_type").equals("1")){
				 * JPushYthdUtil.userBuyPush("会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风使用支付宝支付了 "
				 * +order.getAsBigDecimal("vorder_money")+" 元,预约了国士风场馆: "
				 * +order.getAsString("venue_name")+",请留意订单情况！谢谢光临！",
				 * order.getAsString("user_id")); String content = "会员:" +
				 * user.getAsString("nick_name") + "，您好！感谢您在国士风" + "使用支付宝支付了 "
				 * +order.getAsBigDecimal("vorder_money")+" 元,预约了国士风场馆: "
				 * +order.getAsString("venue_name")+",请留意订单情况！谢谢光临！"; if
				 * (EAUtil.isNotEmpty(user.getAsString("phone"))) { str =
				 * EASMS.sendSms2(user.getAsString("phone"), content); }else {
				 * str = EASMS.sendSms2(order.getAsString("contact_phone"),
				 * content); } }else if(pd.getAsString("pay_type").equals("0")
				 * || pd.getAsString("pay_type").equals("3")){
				 * JPushYthdUtil.userBuyPush("会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风使用微信支付了 "
				 * +order.getAsBigDecimal("vorder_money")+" 元,预约了国士风场馆: "
				 * +order.getAsString("venue_name")+",请留意订单情况！谢谢光临！",
				 * order.getAsString("user_id")); String content = "会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 "
				 * +order.getAsBigDecimal("vorder_money")+" 元,预约了国士风场馆: "
				 * +order.getAsString("venue_name")+",请留意订单情况！谢谢光临！"; if
				 * (EAUtil.isNotEmpty(user.getAsString("phone"))) { str =
				 * EASMS.sendSms2(user.getAsString("phone"), content); }else {
				 * str = EASMS.sendSms2(order.getAsString("contact_phone"),
				 * content); } } if (str.equals("2")) {
				 * System.out.println("预约场馆发送短信成功！：" + str); } else {
				 * System.out.println("预约场馆发送短信失败！：" + str); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 场馆余额支付
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void insertVenueYuePay(PageData pd) throws Exception {
		/**
		 * 更改订单状态
		 */
		String orderId = StringUtils.isNotEmpty(pd.getAsString("orderId")) ? pd.getAsString("orderId")
				: pd.getAsString("order_id");
		PageData venueOrder = venueOrderMapper.selectById(EAString.stringToInt(orderId, 0));
		if (venueOrder == null) {
			throw new Exception("订单不存在");
		}
		if ("1".equalsIgnoreCase(venueOrder.getAsString("vorder_pay_status"))
				&& "4".equalsIgnoreCase(venueOrder.getAsString("vorder_status"))) {
			throw new Exception("订单已支付，请勿重复提交");
		}
		BigDecimal payMoney = new BigDecimal(venueOrder.getAsString("vorder_money"));
		PageData paramEntity = new PageData();
		PageData account = userAccountMapper.selectByUserId(EAString.stringToInt(venueOrder.getAsString("user_id"), 0));
		// 用户余额
		BigDecimal accountMoney = account.getAsBigDecimal("user_money");
		if (account == null || accountMoney.doubleValue() < 0 || accountMoney.compareTo(payMoney) == -1) {
			throw new Exception("无法用余额支付，余额不足");
		}
		venueOrder.put("vorder_status", "4");
		venueOrder.put("vorder_pay_status", "1");
		venueOrderMapper.update(venueOrder);
		/**
		 * 插入支付日志
		 */
		paramEntity.put("pay_money", payMoney);
		paramEntity.put("pay_type", "2");
		paramEntity.put("pay_time", EADate.getCurrentTime());
		paramEntity.put("user_id", venueOrder.getAsString("user_id"));
		paramEntity.put("pay_note", "场馆预定");
		paramEntity.put("pay_source", "1");
		paramEntity.put("pay_order", venueOrder.getAsString("vorder_sn"));
		userPayLogMapper.insert(paramEntity);
		/**
		 * 更改用户钱包
		 */
		Page paramPage = new Page();
		paramEntity.clear();
		paramEntity.put("user_id", venueOrder.getAsString("user_id"));
		paramPage.setPd(paramEntity);
		List<PageData> userAccounts = userAccountMapper.selectByMap(paramPage);
		if (userAccounts != null && userAccounts.size() > 0) {
			paramEntity = userAccounts.get(0);
			BigDecimal userMoney = new BigDecimal(paramEntity.getAsString("user_money"));
			BigDecimal orderMoney = new BigDecimal(paramEntity.getAsString("order_money"));
			paramEntity.put("user_money", userMoney.subtract(payMoney));
			paramEntity.put("order_money", orderMoney.add(payMoney));
			userAccountMapper.update(paramEntity);
		}
		/**
		 * 更改钱包流水
		 */
		paramEntity.clear();
		paramEntity.put("user_id", venueOrder.getAsString("user_id"));
		paramEntity.put("log_money", payMoney);
		paramEntity.put("log_type", "1");
		paramEntity.put("log_symbol", 1); // 支出
		paramEntity.put("log_time", EADate.getCurrentTime());
		paramEntity.put("log_note", "场馆预定");
		paramEntity.put("log_order", venueOrder.getAsString("vorder_sn"));
		userAccountLogMapper.insert(paramEntity);

		PageData user = userMapper.getUserInfoById(venueOrder.getAsInt("user_id"));
		if (EAUtil.isNotEmpty(user)) {
			String content = "";
			String[] userid = null;
			String phone = user.getAsString("phone");
			if (EAUtil.isEmpty(phone)) {
				phone = venueOrder.getAsString("contact_phone");
			}
			userid = new String[] { venueOrder.getAsString("user_id") };
			content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 "
					+ venueOrder.getAsBigDecimal("vorder_money") + " 元,预约了国士风场馆: "
					+ venueOrder.getAsString("venue_name") + ",请留意订单情况！谢谢光临！";
			Sender.sendUserMessage(userid, content, 3, phone);
			/*
			 * JPushYthdUtil.userBuyPush("会员:" + user.getAsString("nick_name") +
			 * "，您好！感谢您在国士风使用余额支付了 "+payMoney+" 元,预约了国士风场馆: "
			 * +venueOrder.getAsString("venue_name")+",请留意订单情况！谢谢光临！",
			 * venueOrder.getAsString("user_id")); String str = ""; String
			 * content = "会员:" + user.getAsString("nick_name") + "，您好！感谢您在国士风" +
			 * "使用余额支付了 "+payMoney+" 元,预约了国士风场馆: "
			 * +venueOrder.getAsString("venue_name")+",请留意订单情况,谢谢光临！"; if
			 * (EAUtil.isNotEmpty(user.getAsString("phone"))) { str =
			 * EASMS.sendSms2(user.getAsString("phone"), content); }else { str =
			 * EASMS.sendSms2(venueOrder.getAsString("contact_phone"), content);
			 * } if (str.equals("2")) { System.out.println("预约场馆发送短信成功！：" +
			 * str); } else { System.out.println("预约场馆发送短信失败！：" + str); }
			 */
		}
	}

}
