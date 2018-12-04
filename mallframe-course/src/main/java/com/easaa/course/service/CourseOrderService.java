package com.easaa.course.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

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
import com.easaa.course.dao.CourseMapper;
import com.easaa.course.dao.CourseOrderMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserMapper;
import com.easaa.user.dao.UserPointsLogMapper;

@Service
public class CourseOrderService extends EABaseService {

	@Autowired
	private CourseOrderMapper courseOrderMapper;
	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private UserAccountMapper userAccountMapper;
	@Autowired
	private UserPayLogMapper userPayLogMapper;
	@Autowired
	private UserAccountLogMapper userAccountLogMapper;
	@Autowired
	private UserPointsLogMapper userPointsLogMapper;
	@Autowired
	private UserMapper userMapper;

	@Override
	public EADao getDao() {
		return courseOrderMapper;
	}

	/**
	 * 课程添加订单
	 * 
	 * @param
	 * @return
	 */
	public PageData insertCourseOrder(PageData pd, Map<String, PageData> gdset) throws Exception {
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("order_course_name"))) {
			throw new Exception("order_course_name为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("order_pay_money"))) {
			throw new Exception("order_money为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("order_conphone"))) {
			throw new Exception("order_conphone为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id为空");
		}
		/**
		 * 判断课程是否已经预约过了
		 */
		Page page = new Page();
		PageData cpd = new PageData();
		cpd.put("order_course_id", pd.getAsString("order_course_id"));
		cpd.put("user_id", pd.getAsString("user_id"));
		cpd.put("order_status", "0");
		page.setPd(cpd);
		List<PageData> clist = courseOrderMapper.selectByMap(page);
		if (clist != null && clist.size() > 0) {
			throw new Exception("该课程已预约过了！");
		}
		BigDecimal zroe_Money = new BigDecimal(0);
		BigDecimal course_Money = new BigDecimal(0);
		// 判断金额是否一致
		PageData course = courseMapper.selectById(pd.getAsInt("order_course_id"));
		if (EAUtil.isEmpty(course)) {
			throw new Exception("order_course_id有误");
		}
		if (!course.getAsString("course_status").equals("0")) {
			throw new Exception("该课程已停止报报名或已开始或已结束！");
		}
		boolean isPayPoints = StringUtils.isNotEmpty(pd.getAsString("order_pay_points"))
				&& (pd.getAsBigDecimal("order_pay_points").compareTo(zroe_Money)==0);
		// 判断积分支付是否一致
		PageData pointdata = goodsdiscount(gdset, pd.getAsString("user_id"), pd.getAsString("order_course_id"));
		if (isPayPoints) {
			if (pd.getAsBigDecimal("order_use_points").compareTo(pointdata.getAsBigDecimal("user_points"))!=0
					&& pd.getAsBigDecimal("course_give_points").compareTo(pointdata.getAsBigDecimal("give_points"))!=0
					&& pd.getAsBigDecimal("order_pay_points").compareTo(pointdata.getAsBigDecimal("pay_points"))!=0) {
				throw new Exception("积分非法！");
			}
		}
		BigDecimal pay_money = pd.getAsBigDecimal("order_pay_money").add(pd.getAsBigDecimal("order_pay_points"));
		// 查询是否是抢购时间
		if (course.getAsInt("course_open_panic") == 1) {
			course_Money = course.getAsBigDecimal("course_panic_price");
		} else {
			course_Money = course.getAsBigDecimal("course_price");
		}
		pd.put("order_course_money", course_Money); // 课程金额
		if (course_Money.subtract(pd.getAsBigDecimal("cash_money")).compareTo(zroe_Money) == -1) {
			course_Money = zroe_Money;
		}
		if (course_Money.compareTo(pay_money) != 0) {
			throw new Exception("金额非法,有误！");
		}
		// 判断是否赠送积分
		if (!isPayPoints) {
			pd.put("order_use_points", "0");
			pd.put("order_give_points", course.getAsBigDecimal("course_give_points") == null ? 0
					: course.getAsBigDecimal("course_give_points"));
		} else {
			pd.put("order_give_points", "0");
		}
		pd.put("order_sn", EAString.getSn());
		if (course_Money.compareTo(zroe_Money) == 0) {
			pd.put("order_pay_status", "2");
		} else {
			pd.put("order_pay_status", "1");
		}
		pd.put("order_status", "0");
		pd.put("order_time", EADate.getCurrentTime());
		pd.put("order_money", pay_money); // 课程金额
		if (courseOrderMapper.insert(pd) > 0) {
			pd.put("id", pd.getAsString("id"));
			// 更改用户积分账户
			if (StringUtils.isNotEmpty(pd.getAsString("order_use_points")) && pd.getAsFloat("order_use_points") > 0) {
				PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
				// 判断积分数量是否合法
				BigDecimal oldPoint = (BigDecimal) (userAccount.getAsBigDecimal("user_points") == null ? 0
						: userAccount.getAsBigDecimal("user_points"));
				if (oldPoint.compareTo(pd.getAsBigDecimal("order_use_points")) < 0) {
					throw new Exception("账户积分数量不足");
				}
				userAccount.put("user_points", oldPoint.subtract(pd.getAsBigDecimal("order_use_points")));
				userAccountMapper.updatePoints(userAccount);
			}
			return pd;
		} else {
			throw new Exception("生成课程订单失败！");
		}
	}

	/**
	 * 课程订单列表
	 * 
	 * @param
	 * @return
	 */
	public List<PageData> selectCourseOrderList(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id为空");
		}
		pd.put("currentPage", EAString.stringToInt(pd.getAsString("currentPage"), 1));
		page.setApp(true);
		page.setCurrentPage(pd.getAsInt("currentPage"));
		page.setShowCount(15);
		page.setPd(pd);
		return courseOrderMapper.selectByPage(page);
	}

	/**
	 * 课程点评列表
	 * 
	 * @param
	 * @return
	 */
	public List<PageData> selectMyCourseorders(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id为空");
		}
		pd.put("currentPage", EAString.stringToInt(pd.getAsString("currentPage"), 1));
		page.setApp(true);
		page.setCurrentPage(pd.getAsInt("currentPage"));
		page.setShowCount(15);
		page.setPd(pd);
		return courseOrderMapper.selecMyCourseOrdersByPage(page);
	}

	/**
	 * 课程订单列表(所有)
	 * 
	 * @param
	 * @return
	 */
	public List<PageData> selectCourseAllOrder(PageData pd) throws Exception {
		Page page = new Page();

		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id为空");
		}
		page.setPd(pd);
		return courseOrderMapper.selectByMap(page);
	}

	/**
	 * 取消课程订单（取消订单后直接删除）
	 * 
	 * @param
	 * @return
	 */
	public boolean updateCourseOrder(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不传，无法进行取消");
		}
		if (EAUtil.isEmpty(pd.getAsString("id"))) {
			throw new Exception("id不传，无法进行取消");
		}
		page.setPd(pd);
		List<PageData> courseorder = courseOrderMapper.selectByPage(page);
		if (EAUtil.isEmpty(courseorder) && courseorder.size() == 0) {
			throw new Exception("课程订单有误！");
		}
		PageData course = courseorder.get(0);
		// 积分支付，退回积分
		if (StringUtils.isNotEmpty(course.getAsString("order_pay_points"))
				&& course.getAsBigDecimal("order_pay_points").compareTo(new BigDecimal("0")) == 1) {
			// 判断积分数量是否合法
			PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
			BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
			userAccount.put("user_points", oldPoint.add(course.getAsBigDecimal("order_pay_points")));
			userAccountMapper.updatePoints(userAccount);
			/**
			 * 更改钱包流水
			 */
			PageData payLog = new PageData();
			payLog.put("user_id", pd.getAsString("user_id"));
			payLog.put("log_pay_order", course.getAsString("order_sn"));
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_role", "1");
			payLog.put("log_points", course.getAsBigDecimal("order_pay_points"));
			payLog.put("log_expenditure", "1"); // 收入
			payLog.put("log_type", "8");
			payLog.put("log_note", "课程预约取消：" + course.getAsString("order_sn") + "使用积分回退"
					+ course.getAsBigDecimal("order_pay_points") + " 积分");
			userPointsLogMapper.insert(payLog);
		}
		// 赠送积分 回收积分
		if (StringUtils.isNotEmpty(course.getAsString("order_give_points"))
				&& course.getAsBigDecimal("order_give_points").compareTo(new BigDecimal("0")) == 1) {
			PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
			// 判断积分数量是否合法
			BigDecimal oldPoint = userAccount.getAsBigDecimal("user_points");
			userAccount.put("user_points", oldPoint.subtract(course.getAsBigDecimal("order_give_points")));
			userAccountMapper.updatePoints(userAccount);
			/**
			 * 更改钱包流水
			 */
			PageData payLog = new PageData();
			payLog.put("user_id", pd.getAsString("user_id"));
			payLog.put("log_pay_order", course.getAsString("order_sn"));
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_role", "1");
			payLog.put("log_points", course.getAsBigDecimal("order_give_points"));
			payLog.put("log_expenditure", "0"); // 支出
			payLog.put("log_type", "7");
			payLog.put("log_note", "课程预约取消：订单 " + course.getAsString("order_sn") + " 赠送回收"
					+ course.getAsBigDecimal("order_give_points") + " 积分");
			userPointsLogMapper.insert(payLog);
		}
		// 金钱支付，退回钱包
		if (StringUtils.isNotEmpty(course.getAsString("order_pay_money"))
				&& course.getAsString("order_pay_status").equals("2")
				&& course.getAsBigDecimal("order_pay_money").doubleValue() > 0) {
			PageData userAccount = userAccountMapper.selectByUserId(pd.getAsInt("user_id"));
			BigDecimal oldMoney = userAccount.getAsBigDecimal("user_money");
			BigDecimal orderMoney = userAccount.getAsBigDecimal("order_money");
			userAccount.put("order_money", orderMoney.subtract(course.getAsBigDecimal("order_pay_money")));
			userAccount.put("user_money", oldMoney.add(course.getAsBigDecimal("order_pay_money")));
			userAccountMapper.update(userAccount);
			/**
			 * 更改钱包流水
			 */
			PageData payLog = new PageData();
			payLog.put("user_id", pd.getAsInt("user_id"));
			payLog.put("log_money", course.getAsBigDecimal("order_pay_money"));
			payLog.put("log_type", "1");
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_note", "课程预约取消");
			payLog.put("log_symbol", "0");
			payLog.put("log_order", course.getAsString("order_sn"));
			userAccountLogMapper.insert(payLog);
		}
		pd.put("order_status", "2");
		if (courseOrderMapper.update(pd) > 0 && course.getAsString("order_pay_status").equals("2")) {
			return true;
		} else {
			throw new Exception("课程取消有误！");
		}
	}

	/**
	 * 删除课程订单
	 * 
	 * @param
	 * @return
	 */
	public boolean deleteCourseOrder(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("用户id不传，无法进行删除");
		}
		if (EAUtil.isEmpty(pd.getAsString("id"))) {
			throw new Exception("id不传，无法进行删除");
		}
		page.setPd(pd);
		List<PageData> courseorder = courseOrderMapper.selectByPage(page);
		if (EAUtil.isEmpty(courseorder) && courseorder.size() == 0) {
			throw new Exception("课程订单有误！");
		}
		if (courseorder.get(0).getAsInt("order_pay_status") == 2 && courseorder.get(0).getAsInt("order_status") == 0) {
			throw new Exception("订单已支付，不能删除！");
		}
		pd.put("order_status", "3");
		if (courseOrderMapper.update(pd) > 0) {
			// 有积分付款，退回积分
			if (EAUtil.isNotEmpty(pd.getAsBigDecimal("order_use_points"))
					&& pd.getAsBigDecimal("order_use_points").compareTo(new BigDecimal(0)) == 1) {
				PageData userAccount = userAccountLogMapper.selectById(pd.getAsInt("user_id"));
				BigDecimal oldMoney = userAccount.getAsBigDecimal("user_points");
				userAccount.put("user_points", oldMoney.add(pd.getAsBigDecimal("order_use_points")));
				userAccountLogMapper.update(userAccount);
			}
			return true;
		} else {
			throw new Exception("课程删除有误！");
		}
	}

	/**
	 * 课程支付回调逻辑
	 */
	public boolean doCourseCall(PageData pd) {
		// 更改场馆订单支付状态已付款 更改场馆状态已完成
		Page page = new Page();
		pd.put("order_sn", pd.getAsString("out_trade_no"));
		page.setPd(pd);
		List<PageData> courseOrders = courseOrderMapper.selectByMap(page);
		if (courseOrders == null || courseOrders.size() != 1) {
			return false;
		}
		PageData order = courseOrders.get(0);

		if ("2".equalsIgnoreCase(order.getAsString("order_pay_status")) && order.getAsInt("order_status") > 0) {
			return true;
		}
		order.put("order_status", "0");
		order.put("order_pay_status", "2");
		courseOrderMapper.update(order);
		try {
			// 插入支付日志
			PageData payLog = new PageData();
			payLog.put("pay_money", order.getAsString("order_pay_money"));
			payLog.put("pay_type", pd.getAsString("pay_type"));
			payLog.put("pay_time", EADate.getCurrentTime());
			payLog.put("user_id", order.getAsString("user_id"));
			payLog.put("pay_note", "课程预约");
			payLog.put("pay_source", "4");
			payLog.put("pay_order", order.getAsString("order_sn"));
			userPayLogMapper.insert(payLog);
			/**
			 * 更改用户钱包
			 */
			Page paramPage = new Page();
			payLog.clear();
			payLog.put("user_id", order.getAsString("user_id"));
			paramPage.setPd(payLog);
			List<PageData> userAccounts = userAccountMapper.selectByMap(paramPage);
			if (userAccounts != null && userAccounts.size() > 0) {
				payLog = userAccounts.get(0);
				BigDecimal orderMoney = new BigDecimal(payLog.getAsString("order_money"));
				payLog.put("order_money", orderMoney.add(order.getAsBigDecimal("order_pay_money")));
				userAccountMapper.update(payLog);
			}
			/**
			 * 更改钱包流水
			 */
			payLog.clear();
			payLog.put("user_id", order.getAsString("user_id"));
			payLog.put("log_money", order.getAsString("order_money"));
			payLog.put("log_type", "1");
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_note", "课程预约");
			payLog.put("log_symbol", 1);
			payLog.put("log_order", order.getAsString("order_sn"));
			userAccountLogMapper.insert(payLog);
			/**
			 * 插入积分明细日志
			 */
			payLog.clear();
			payLog.put("user_id", order.getAsString("user_id"));
			payLog.put("log_pay_order", order.getAsString("order_sn"));
			payLog.put("log_time", EADate.getCurrentTime());
			payLog.put("log_role", "1");
			if (StringUtils.isNotEmpty(order.getAsString("order_use_points"))
					&& !"0".equalsIgnoreCase(order.getAsString("order_use_points"))) {
				payLog.put("log_points", order.getAsString("order_use_points"));
				payLog.put("log_expenditure", "0"); // 支出
				payLog.put("log_type", "3");
				payLog.put("log_note", "预约课程支出：订单 " + order.getAsString("order_sn") + " 支付"
						+ order.getAsString("order_use_points") + " 积分");
				userPointsLogMapper.insert(payLog);
			} else {
				if (StringUtils.isNotEmpty(order.getAsString("order_give_points"))
						&& !"0".equalsIgnoreCase(order.getAsString("order_give_points"))) {
					payLog.put("log_points", order.getAsString("order_give_points"));
					payLog.put("log_expenditure", "1"); // 收入
					payLog.put("log_type", "2");
					payLog.put("log_note", "预约课程赠送：" + order.getAsString("order_sn") + " 赠送"
							+ order.getAsString("order_give_points") + " 积分");
					userPointsLogMapper.insert(payLog);
				}
			}
			// 变更课程购买数量
			PageData course = courseMapper.selectById(order.getAsInt("order_course_id"));
			course.put("course_buyer_total", EAString.stringToInt(course.getAsString("course_buyer_total"), 0) + 1);
			courseMapper.update(course);
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
					content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用支付宝支付了 "
							+ order.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: "
							+ order.getAsString("order_course_name") + " 赠送" + order.getAsString("order_give_points")
							+ " 积分 ,请留意订单情况！谢谢光临！";
				} else if (pd.getAsString("pay_type").equals("0") || pd.getAsString("pay_type").equals("3")) {
					content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 "
							+ order.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: "
							+ order.getAsString("order_course_name") + " 赠送" + order.getAsString("order_give_points")
							+ " 积分 ,请留意订单情况！谢谢光临！";
				}
				Sender.sendUserMessage(userid, content, 3, phone);
				/*
				 * String str = ""; String content = ""; if
				 * (pd.getAsString("pay_type").equals("1")) {
				 * JPushYthdUtil.userBuyPush( "会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风使用支付宝支付了 " +
				 * order.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: " +
				 * order.getAsString("order_course_name") + " 赠送" +
				 * order.getAsString("order_give_points") + " 积分 ,请留意订单情况！谢谢光临！"
				 * , order.getAsString("user_id")); content = "会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用支付宝支付了 " +
				 * order.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: " +
				 * order.getAsString("order_course_name") + " 赠送" +
				 * order.getAsString("order_give_points") + " 积分 ,请留意订单情况！谢谢光临！"
				 * ; } else if (pd.getAsString("pay_type").equals("0") ||
				 * pd.getAsString("pay_type").equals("3")) {
				 * JPushYthdUtil.userBuyPush( "会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风使用微信支付了 " +
				 * order.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: " +
				 * order.getAsString("order_course_name") + " 赠送" +
				 * order.getAsString("order_give_points") + " 积分 ,请留意订单情况！谢谢光临！"
				 * , order.getAsString("user_id")); content = "会员:" +
				 * user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用微信支付了 " +
				 * order.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: " +
				 * order.getAsString("order_course_name") + " 赠送" +
				 * order.getAsString("order_give_points") + " 积分 ,请留意订单情况！谢谢光临！"
				 * ; } if (EAUtil.isNotEmpty(user.getAsString("phone"))) { str =
				 * EASMS.sendSms2(user.getAsString("phone"), content); }else {
				 * str = EASMS.sendSms2(order.getAsString("order_conphone"),
				 * content); } if (str.equals("2")) {
				 * System.out.println("预约课程发送短信成功！：" + str); } else {
				 * System.out.println("预约课程发送短信失败！：" + str); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 课程余额支付
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void insertCourseYuePay(PageData pd) throws Exception {
		/**
		 * 更改订单状态
		 */
		String orderId = StringUtils.isNotEmpty(pd.getAsString("orderId")) ? pd.getAsString("orderId")
				: pd.getAsString("order_id");
		PageData courseOrder = courseOrderMapper.selectById(EAString.stringToInt(orderId, 0));
		if (courseOrder == null) {
			throw new Exception("订单不存在");
		}
		if ("2".equalsIgnoreCase(courseOrder.getAsString("order_pay_status"))
				&& courseOrder.getAsInt("order_status") > 0) {
			throw new Exception("订单已支付，请勿重复提交");
		}
		PageData payLog = new PageData();
		BigDecimal payMoney = new BigDecimal(courseOrder.getAsString("order_pay_money"));
		PageData account = userAccountMapper
				.selectByUserId(EAString.stringToInt(courseOrder.getAsString("user_id"), 0));
		// 用户余额
		BigDecimal accountMoney = account.getAsBigDecimal("user_money");
		if (account == null || accountMoney.doubleValue() < 0 || accountMoney.compareTo(payMoney) == -1) {
			throw new Exception("无法用余额支付，余额不足");
		}
		courseOrder.put("order_status", "0");
		courseOrder.put("order_pay_status", "2");
		courseOrderMapper.update(courseOrder);
		// 插入支付日志
		payLog.put("pay_money", payMoney);
		payLog.put("pay_type", "2");
		payLog.put("pay_time", EADate.getCurrentTime());
		payLog.put("user_id", courseOrder.getAsString("user_id"));
		payLog.put("pay_note", "课程预约");
		payLog.put("pay_source", "4");
		payLog.put("pay_order", courseOrder.getAsString("order_sn"));
		userPayLogMapper.insert(payLog);
		/**
		 * 更改用户钱包
		 */
		Page paramPage = new Page();
		payLog.clear();
		payLog.put("user_id", courseOrder.getAsString("user_id"));
		paramPage.setPd(payLog);
		List<PageData> userAccounts = userAccountMapper.selectByMap(paramPage);
		if (userAccounts != null && userAccounts.size() > 0) {
			payLog = userAccounts.get(0);
			BigDecimal userMoney = new BigDecimal(payLog.getAsString("user_money"));
			BigDecimal orderMoney = new BigDecimal(payLog.getAsString("order_money"));
			payLog.put("user_money", userMoney.subtract(payMoney));
			payLog.put("order_money", orderMoney.add(payMoney));
			userAccountMapper.update(payLog);
		}
		/**
		 * 更改钱包流水
		 */
		payLog.clear();
		payLog.put("user_id", courseOrder.getAsString("user_id"));
		payLog.put("log_money", payMoney);
		payLog.put("log_type", "1");
		payLog.put("log_time", EADate.getCurrentTime());
		payLog.put("log_note", "课程预约");
		payLog.put("log_symbol", 1);
		payLog.put("log_order", courseOrder.getAsString("order_sn"));
		userAccountLogMapper.insert(payLog);
		/**
		 * 插入积分明细日志
		 */
		payLog.clear();
		payLog.put("user_id", courseOrder.getAsString("user_id"));
		payLog.put("log_pay_order", courseOrder.getAsString("order_sn"));
		payLog.put("log_time", EADate.getCurrentTime());
		payLog.put("log_role", "1");
		if (StringUtils.isNotEmpty(courseOrder.getAsString("order_use_points"))
				&& !"0".equalsIgnoreCase(courseOrder.getAsString("order_use_points"))) {
			payLog.put("log_points", courseOrder.getAsString("order_use_points"));
			payLog.put("log_expenditure", 0); // 支出
			payLog.put("log_type", "3");
			payLog.put("log_note", "预约课程支出：" + courseOrder.getAsString("order_sn") + " 支付"
					+ courseOrder.getAsString("order_use_points") + " 积分");
			userPointsLogMapper.insert(payLog);
		} else {
			if (StringUtils.isNotEmpty(courseOrder.getAsString("order_give_points"))
					&& !"0".equalsIgnoreCase(courseOrder.getAsString("order_give_points"))) {
				payLog.put("log_points", courseOrder.getAsString("order_give_points"));
				payLog.put("log_expenditure", 1); // 收入
				payLog.put("log_type", "2");
				payLog.put("log_note", "预约课程订单：" + courseOrder.getAsString("order_sn") + " 赠送"
						+ courseOrder.getAsString("order_give_points") + " 积分");
				userPointsLogMapper.insert(payLog);
			}
		}
		// 变更课程购买数量
		PageData course = courseMapper.selectById(courseOrder.getAsInt("order_course_id"));
		course.put("course_buyer_total", EAString.stringToInt(course.getAsString("course_buyer_total"), 0) + 1);
		courseMapper.update(course);
		PageData user = userMapper.getUserInfoById(courseOrder.getAsInt("user_id"));
		if (EAUtil.isNotEmpty(user)) {
			String content = "";
			String[] userid = null;
			String phone = user.getAsString("phone");
			if (EAUtil.isEmpty(phone)) {
				phone = courseOrder.getAsString("contact_phone");
			}
			userid = new String[] { courseOrder.getAsString("user_id") };
			content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风" + "使用余额支付了"
					+ courseOrder.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: "
					+ courseOrder.getAsString("order_course_name") + " 赠送"
					+ courseOrder.getAsString("order_give_points") + " 积分 ,请留意订单情况！谢谢光临！";
			Sender.sendUserMessage(userid, content, 3, phone);
			/*
			 * JPushYthdUtil.userBuyPush( "会员:" + user.getAsString("nick_name")
			 * + "，您好！感谢您在国士风使用余额支付了 " +
			 * courseOrder.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: "
			 * + courseOrder.getAsString("order_course_name") + " 赠送" +
			 * courseOrder.getAsString("order_give_points") +
			 * " 积分 ,请留意订单情况！谢谢光临！", courseOrder.getAsString("user_id")); String
			 * str = ""; String content = "会员:" + user.getAsString("nick_name")
			 * + "，您好！感谢您在国士风" + "使用余额支付了 " +
			 * courseOrder.getAsBigDecimal("order_pay_money") + " 元,预约了国士风课程: "
			 * + courseOrder.getAsString("order_course_name") + " 赠送" +
			 * courseOrder.getAsString("order_give_points") +
			 * " 积分 ,请留意订单情况！谢谢光临！"; if
			 * (EAUtil.isNotEmpty(user.getAsString("phone"))) { str =
			 * EASMS.sendSms2(user.getAsString("phone"), content); } else { str
			 * = EASMS.sendSms2(courseOrder.getAsString("order_conphone"),
			 * content); } if (str.equals("2")) {
			 * System.out.println("预约课程发送短信成功！：" + str); } else {
			 * System.out.println("预约课程发送短信失败！：" + str); }
			 */
		}
	}

	/**
	 * 查询课程使用订单
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> selectByCourseId(Integer pd) {
		return courseOrderMapper.selectByCourseId(pd);
	}

	/**
	 * 课程积分抵扣
	 * 
	 * @param response
	 */
	public PageData goodsdiscount(Map<String, PageData> gdset, String user_id, String course_id) {
		PageData data = new PageData();
		// 按照积分抵扣比列返回参数
		boolean isTrue = false;
		String ds = "";
		if ((StringUtils.isEmpty(gdset.get("00101").getAsString("setting_value")) ? "1"
				: gdset.get("00101").getAsString("setting_value")).equals("1")) {
			isTrue = true;
		}
		ds = StringUtils.isEmpty(gdset.get("00103").getAsString("setting_value")) ? "1:10"
				: gdset.get("00103").getAsString("setting_value");

		// 查询所有商品是否能抵扣(所有商品可使用的积分)
		BigDecimal zero = new BigDecimal(0);
		BigDecimal goodspoints = zero;
		BigDecimal givepoints = zero;
		PageData course = courseMapper.selectById(Integer.valueOf(course_id));
		goodspoints = course.getAsBigDecimal("course_pay_points");
		givepoints = course.getAsBigDecimal("course_give_points");
		if (goodspoints.compareTo(zero) < 0) {
			goodspoints = zero;
		}
		if (givepoints.compareTo(zero) < 0) {
			givepoints = zero;
		}
		// 积分支付开启
		if (isTrue) {
			// 查询用户可用积分
			BigDecimal userpoints = zero;
			data.put("user_id", user_id);
			data.put("course_id", course_id);
			Page page = new Page();
			page.setPd(data);
			List<PageData> ucpd = userAccountMapper.selectByMap(page);
			if (EAUtil.isNotEmpty(ucpd)) {
				if (EAUtil.isEmpty(ucpd.get(0).getAsString("user_points")) || userpoints.compareTo(zero) < 0) {
					userpoints = zero;
				} else {
					/*
					 * if
					 * (EAUtil.isEmpty(ucpd.get(0).getAsString("frozen_points"))
					 * ) { userpoints = ucpd.get(0).getAsInt("user_points"); }
					 * userpoints = ucpd.get(0).getAsInt("user_points") -
					 * ucpd.get(0).getAsInt("frozen_points");
					 */
					userpoints = ucpd.get(0).getAsBigDecimal("user_points");
				}

			}
			if (userpoints.compareTo(zero) < 0) {
				userpoints = zero;
			}
			data.clear();
			// 用户积分为0或者商品积分为0，不参与积分支付
			if (userpoints.compareTo(zero) == 0 || goodspoints.compareTo(zero) == 0) {
				data.put("pay_points", "0");
				data.put("user_points", "0");
				data.put("give_points", givepoints);
			}
			String[] dsStr;
			if (EAUtil.isNotEmpty(ds)) {
				dsStr = ds.split(":");
				if (userpoints.compareTo(goodspoints) >= 0) {
					BigDecimal ds_point = goodspoints.divide((new BigDecimal(dsStr[1]))
							.multiply(new BigDecimal(dsStr[0])), 2,
									RoundingMode.FLOOR);
					data.put("pay_points", ds_point);
					data.put("user_points", goodspoints);
				} else {
					BigDecimal ds_point = userpoints.divide((new BigDecimal(dsStr[1]))
							.multiply(new BigDecimal(dsStr[0])), 2,
									RoundingMode.FLOOR);
					data.put("pay_points", ds_point);
					data.put("user_points", userpoints);
				}
			}
			data.put("give_points", givepoints);
		} else {
			data.clear();
			data.put("pay_points", "0");
			data.put("user_points", "0");
			data.put("give_points", givepoints);
		}
		return data;
	}
}
