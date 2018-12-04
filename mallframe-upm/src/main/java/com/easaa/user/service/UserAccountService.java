package com.easaa.user.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.Sender;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserMapper;

/**
 * 会员钱包操作类, 会员钱包日志均有钱包操作自动产生,请不要再其余地方直接插入日志,也不要在其他地方直接操作钱包,功能不足请先沟通
 * @author 约
 */
@Service
public class UserAccountService extends EABaseService {
	@Autowired
	private UserAccountMapper userAccountMapper;
	@Autowired
	private UserAccountLogMapper userAccountLogMapper;
	@Autowired
	private UserMapper userMapper;
	/**
	 * =========================================================================
	 * ===================================== 会员钱包操作-------------------------
	 * =========================================================================
	 * =====================================
	 */
	/**
	 * 订单支付;订单支付后更改用户账户
	 * 
	 * @param userId
	 *            会员ID
	 * @param orderSn
	 *            订单流水号
	 * @param cash
	 *            现金(支付时使用了多少现金)
	 * @param balance
	 *            余额(支付时使用了多少余额)
	 * @param givePoints
	 *            赠送积分(赠送了多少积分) 赠与积分不可以和使用积分同事存在;
	 * @param usePoints
	 *            使用积分(支付时使用了多少积分抵扣现金) 赠与积分不可以和使用积分同事存在;
	 * @return
	 */
	public int ordersPay(int userId, String orderSn, float cash, float balance, int givePoints, int usePoints) {
		if (givePoints > 0 && usePoints > 0) {
			System.out.println("赠与积分不可以和使用积分同时存在");
			return 0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		if (balance > 0) {
			// 会员钱包余额
			float user_money = userAccount.getAsFloat("user_money");
			user_money = user_money - balance;
			userAccount.put("user_money", user_money);
		}
		// 累计购物金额
		float order_money = userAccount.getAsFloat("order_money");// 订单金额(累计购物花了多少钱);
		order_money = order_money + cash + balance;
		userAccount.put("order_money", order_money);
		// 累计购物积分
		int order_points = userAccount.getAsInt("order_points");
		order_points = order_points + givePoints;
		userAccount.put("order_points", order_points);
		// 累计使用积分
		int use_points = userAccount.getAsInt("use_points");
		use_points = use_points + usePoints;
		userAccount.put("use_points", use_points);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points - usePoints + givePoints;
		userAccount.put("user_points", user_points);
		// 最后一次修改时间
		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "订单:" + orderSn;
		if (balance > 0) {// 余额大于0
			logNote = logNote + "使用余额(" + balance + ");";
		}
		int points = givePoints;
		if (givePoints > 0) {
			logNote = logNote + "赠与积分(" + givePoints + ");";
		} else if (usePoints > 0) {
			logNote = logNote + "使用积分(" + usePoints + ");";
			points = -usePoints;
		}
		insertAccountLog(userId, balance, 1, points, "", orderSn, logNote);
		return 1;
	}

	/**
	 * 订单退款到会员钱包(如果订单尚未支付,请不要调用此方法);
	 * 
	 * @param userId
	 *            会员ID
	 * @param orderSn
	 *            订单编号(非自增ID)
	 * @param money
	 *            退回金额
	 * @param givePoints
	 *            退回赠送的积分(订单支付时赠送的积分)
	 * @param usePoints
	 *            退回使用的积分(订单支付的时候使用积分抵现部分)
	 * @return 大于一说明操作成功
	 */
	public int ordersRefund(int userId, String orderSn, float money, int givePoints, int usePoints) {
		if (money <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 会员钱包余额
		float user_money = userAccount.getAsFloat("user_money");
		user_money = user_money + money;
		userAccount.put("user_money", user_money);
		// 累计购物金额
		float order_money = userAccount.getAsFloat("order_money");
		order_money = order_money - money;
		userAccount.put("order_money", order_money);
		// 累计购物积分
		int order_points = userAccount.getAsInt("order_points");
		order_points = order_points - givePoints;
		userAccount.put("order_points", order_points);
		// 累计使用积分
		int use_points = userAccount.getAsInt("use_points");
		use_points = use_points - usePoints;
		userAccount.put("use_points", use_points);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points + usePoints - givePoints;
		userAccount.put("user_points", user_points);
		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "退款订单:" + orderSn;
		if (money > 0) {// 余额大于0
			logNote = logNote + "退回余额(" + money + ");";
		}
		int points = usePoints;
		if (givePoints > 0) {
			logNote = logNote + "扣除赠与的积分(" + givePoints + ");";
			points = -givePoints;
		} else if (usePoints > 0) {
			logNote = logNote + "退回使用的积分(" + usePoints + ");";
		}
		insertAccountLog(userId, money, 2, points, "", orderSn, logNote);
		return 1;
	}

	/**
	 * 订单返佣(当订单完成后才可以调用此方法)
	 * 
	 * @param userId
	 *            会员ID
	 * @param orderSn
	 *            结算的订单编号
	 * @param money
	 *            佣金总额
	 * @param points
	 *            积分总额
	 * @return 大于一说明操作成功
	 */
	public int ordersRebate(int userId, String orderSn, float money, int points) {
		if (money <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 会员钱包余额
		float user_money = userAccount.getAsFloat("user_money");
		user_money = user_money + money;
		userAccount.put("user_money", user_money);
		// 累计返佣(现金)
		float rebate_money = userAccount.getAsFloat("rebate_money");
		rebate_money = rebate_money + money;
		userAccount.put("rebate_money", rebate_money);
//		// 用户总共积分
//		int user_points = userAccount.getAsInt("user_points");
//		user_points = user_points + points;
//		userAccount.put("user_points", user_points);
//		// 累计返佣(积分)
//		float rebate_points = userAccount.getAsFloat("rebate_points");
//		rebate_points = rebate_points + points;
//		userAccount.put("rebate_points", rebate_points);
		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "订单返佣:" + orderSn;
		if (money > 0) {// 余额大于0
			logNote = logNote + "返佣现金(" + money + ");";
		}
//		if (points > 0) {
//			logNote = logNote + "返佣积分(" + points + ");";
//		}
		insertAccountLog(userId, money, 10, points, "", orderSn, logNote);
		/**
		 * 发送短信，极光推送
		 */
		PageData user= userMapper.getUserInfoById(userId);
		if(EAUtil.isNotEmpty(user)){
			String[] userid = null;
			String phone = user.getAsString("phone");
			userid = new String[] {String.valueOf(userId)};
			String content="会员:"+user.getAsString("nick_name")+",您好！您的团队在国士风完成了商品交易 ,成功为您返佣！" +" 返佣 订单号： " + orderSn + ",返佣现金(" + money + ") 元,"+
					"感谢您团队的支持！";
			Sender.sendUserMessage(userid, content, 3, phone);
		/*	JPushYthdUtil.userBuyPush("会员:"+user.getAsString("nick_name")+",您好！您的团队在国士风完成了商品交易 ,成功为您返佣！" +" 返佣 订单号： " + orderSn + ",返佣现金(" + money + ") 元,"+
					"感谢您团队的支持！",
					String.valueOf(userId));
			String str="";
			String content="会员:"+user.getAsString("nick_name")+",您好！您的团队在国士风完成了商品交易 ,成功为您返佣！" +" 返佣 订单号： " + orderSn + ",返佣现金(" + money + ") 元,"+
					"感谢您团队的支持！";
			if(EAUtil.isNotEmpty(user.getAsString("phone"))){
				str = EASMS.sendSms2(user.getAsString("phone"), content);
			}
			if (str.equals("2")) {
				System.out.println("购买商品发送短信成功！："+str);
			} else {
				System.out.println("购买商品发送短信失败！："+str);
			}*/
		}
		return 1;
	}

	/**
	 * 取消订单返佣
	 * 
	 * @param userId
	 *            会员ID
	 * @param orderSn
	 *            结算的订单编号
	 * @param money
	 *            佣金总额
	 * @param points
	 *            积分总额
	 * @return 大于一说明操作成功
	 */
	public int cancelOrdersRebate(int userId, String orderSn, float money, int points) {
		if (money > 0) {
			return 0;// 订单取消
		}
		money = -money;
		points = -points;
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 会员钱包余额
		float user_money = userAccount.getAsFloat("user_money");
		user_money = user_money + money;
		userAccount.put("user_money", user_money);
		// 累计返佣(现金)
		float rebate_money = userAccount.getAsFloat("rebate_money");
		rebate_money = rebate_money + money;
		userAccount.put("rebate_money", rebate_money);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points + points;
		userAccount.put("user_points", user_points);
		// 累计返佣(积分)
		float rebate_points = userAccount.getAsFloat("rebate_points");
		rebate_points = rebate_points + points;
		userAccount.put("rebate_points", rebate_points);

		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "取消订单返佣:" + orderSn;
		if (money > 0) {// 余额大于0
			logNote = logNote + "现金(" + money + ");";
		}
		if (points > 0) {
			logNote = logNote + "积分(" + points + ");";
		}
		insertAccountLog(userId, money, 10, points, "", orderSn, logNote);
		return 1;
	}

	/**
	 * 会员充值
	 * 
	 * @param userId
	 *            会员ID
	 * @param paySn
	 *            充值流水号(非自增ID)
	 * @param money
	 *            充值金额
	 * @param points
	 *            充值赠送的积分
	 * @return 大于一说明操作成功
	 */
	public int chargeMoney(int userId, String paySn, float money, int points) {
		if (money <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 会员钱包余额
		float user_money = userAccount.getAsFloat("user_money");// 总金额
		user_money = user_money + money;// 用户总金额加上新充值金额
		userAccount.put("user_money", user_money);
		// 累计充值多少钱;
		float charge_money = userAccount.getAsFloat("charge_money");
		charge_money = charge_money + money;// 累计充值金额加
		userAccount.put("charge_money", charge_money);
		// 累计充值金赠送积分
		float charge_points = userAccount.getAsFloat("charge_points");
		charge_money = charge_points + points;
		userAccount.put("charge_points", charge_points);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points + points;
		userAccount.put("user_points", user_points);
		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "会员充值:" + paySn;
		logNote = logNote + "现金(" + money + ");";
		if (points > 0) {
			logNote = logNote + "赠送积分(" + points + ");";
		}
		insertAccountLog(userId, money, 4, points, "", paySn, logNote);
		return 1;
	}

	/**
	 * 会员提现(在提现申请里操作"完成提现"时调用此方法更新用户余额)
	 * 
	 * @param userId
	 *            会员ID
	 * @param drawSn
	 *            提现申请流水号
	 * @param money
	 *            提现金额
	 * @param money
	 *            提现手续费
	 * @param points
	 *            提现退回积分
	 * @return 大于一说明操作成功
	 */
	public int withdrawMoney(int userId, String drawSn, float money, float fee, int points) {
		if (money <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 会员钱包余额
		float user_money = userAccount.getAsFloat("user_money");// 总金额
		user_money = user_money - money - fee;// 用户总金额加上新充值金额
		userAccount.put("user_money", user_money);
		// 累计提现多少钱
		float withdraw_money = userAccount.getAsFloat("withdraw_money");
		withdraw_money = withdraw_money + money;
		userAccount.put("withdraw_money", withdraw_money);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points + points;
		userAccount.put("user_points", user_points);
		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "会员提现:" + drawSn;
		logNote = logNote + "提现金额(" + money + ")手续费(" + fee + ")";
		if (points > 0) {
			logNote = logNote + "扣除积分(" + points + ");";
			points = -points;
		}
		insertAccountLog(userId, money, 5, points, "", drawSn, logNote);
		return 1;
	}

	/**
	 * 分享赠送
	 * 
	 * @param userId
	 *            会员ID
	 * @param shareSn
	 *            分享流水号
	 * @param shareType
	 *            分享类型(商品、文章、活动、设计师等）
	 * @param points
	 *            提现退回积分
	 * @return 大于一说明操作成功
	 */
	public int shareGive(int userId, String shareSn, String shareType, int points) {
		if (points <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 累计分享积分
		float share_points = userAccount.getAsFloat("share_points");
		share_points = share_points + points;
		userAccount.put("share_points", share_points);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points + points;
		userAccount.put("user_points", user_points);

		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "分享" + shareType + ":" + shareSn;
		logNote = logNote + "赠送积分(" + points + ");";
		insertAccountLog(userId, 0, 5, points, "", shareSn, logNote);
		return 1;
	}

	/**
	 * 分享被阅读赠送
	 * 
	 * @param userId
	 *            会员ID
	 * @param shareSn
	 *            分享流水号
	 * @param shareType
	 *            分享类型(商品、文章、活动、设计师等）
	 * @param points
	 *            提现退回积分
	 * @return 大于一说明操作成功
	 */
	public int shareRead(int userId, String shareSn, int points) {
		if (points <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		PageData userAccount = userAccountMapper.selectByUserId(userId);
		// 累计分享积分
		float share_points = userAccount.getAsFloat("share_points");
		share_points = share_points + points;
		userAccount.put("share_points", share_points);
		// 用户总共积分
		int user_points = userAccount.getAsInt("user_points");
		user_points = user_points + points;
		userAccount.put("user_points", user_points);

		String change_time = EADate.getCurrentTime();
		userAccount.put("change_time", change_time);
		userAccountMapper.update(userAccount);
		// 插入日志
		String logNote = "分享浏览:" + shareSn;
		logNote = logNote + "赠送积分(" + points + ");";
		insertAccountLog(userId, 0, 5, points, "", shareSn, logNote);
		return 1;
	}

	/**
	 * 变更会员钱包金额(仅限于管理员手动操作)
	 * 
	 * @param userId
	 *            会员ID
	 * @param money
	 *            积分变更数量,可以是正数或者负数(为0时不做任何处理)
	 * @param adminName
	 *            操作人账号,即管理员账号
	 * @param opType
	 *            日志类型(1:订单 ;2:充值 ;3:提现 ;4:返佣 ;5:冻结 ;6:解冻;
	 * @param note
	 *            操作备注(必填)
	 * @return
	 */
	public int changeMoney(int userId, float money, String adminName, int opType, String note) {
		if (money <= 0) {
			return 0;// 发生金额为0;
		}
		// 查出会员钱包
		// PageData userAccount = userAccountMapper.selectByUserId(userId);
		// String logType="";
		// float user_money = userAccount.getAsFloat("user_money");//总金额
		// user_money=user_money+money;
		// if(opType==1){//订单
		// float order_money =userAccount.getAsFloat("order_money");
		// order_money=order_money+money;
		// userAccount.put("order_money", order_money);
		// logType="订单操作";
		// }else if(opType==2){//充值
		// float charge_money =userAccount.getAsFloat("charge_money");
		// charge_money=charge_money+money;
		// userAccount.put("charge_money", charge_money);
		// logType="管理员充值";
		// }else if(opType==3){//提现
		// float withdraw_money =userAccount.getAsFloat("withdraw_money");
		// withdraw_money=withdraw_money+money;
		// userAccount.put("withdraw_money", withdraw_money);
		// logType="管理员提现";
		// }else if(opType==4){//返佣
		// float rebate_money =userAccount.getAsFloat("rebate_money");
		// rebate_money=rebate_money+money;
		// userAccount.put("order_money", rebate_money);
		// logType="管理员返佣";
		// }else if(opType==5){//冻结
		// float frozen_money =userAccount.getAsFloat("frozen_money");
		// frozen_money=frozen_money+money;
		// userAccount.put("frozen_money", frozen_money);
		// logType="管理员冻结";
		// }else if(opType==6){//解冻
		// float frozen_money =userAccount.getAsFloat("frozen_money");
		// frozen_money=frozen_money+money;
		// userAccount.put("frozen_money", frozen_money);
		// logType="管理员解冻";
		// }
		// String change_time=EADate.getCurrentTime();
		// userAccount.put("change_time", change_time);
		// userAccountMapper.update(userAccount);
		// //日志部分
		// insertAccountLog(userId, money, 1, logType, adminName, "", note);
		return 1;
	}

	/**
	 * 更新会员钱包
	 * 
	 * @param userAccount
	 *            钱包
	 * @param adminName
	 *            管理员登录账号
	 * @param money
	 *            账户金额变更数量
	 * @param points
	 *            账户积分变更数量
	 * @param note
	 *            管理员备注
	 * @param sysNote
	 *            系统自动备注
	 * @return
	 */
	public int editUserAccount(PageData userAccount, String adminName, float money, int points, String note,
			String sysNote) {
		int status = userAccountMapper.update(userAccount);
		if (status > 0) {
			PageData accountLog = new PageData();// 日志
			accountLog.put("user_id", userAccount.getAsInt("user_id"));// 用户ID
			accountLog.put("admin_name", adminName);// 管理员ID
			accountLog.put("log_money", money);// 变更金额
			accountLog.put("log_points", points);// 变更金额
			accountLog.put("log_type", 7);// 日志类型
			accountLog.put("log_value", "");// 日志外键
			accountLog.put("log_time", EADate.getCurrentTime());// 日志时间
			accountLog.put("log_note", note);// 日志备注
			accountLog.put("sys_note", sysNote);// 日志备注
			accountLog.put("log_symbol", "0");// 金额正负
			return userAccountLogMapper.insert(accountLog);
		}
		return 0;
	}

	/**
	 * 插入账变日志
	 * 
	 * @param userId
	 * @param money
	 *            现金总数
	 * @param log_type
	 *            1:订单支付 2:订单取消 3:充值 4:提现 5:冻结 6:解冻 7:手动更改 8:分享赠送:9退货 10返佣
	 * @param points
	 *            积分总数
	 * @param adminName
	 *            管理员账号(是账号,不是ID)
	 * @param log_value
	 *            日志外键(例如订单支付,此处应填订单SN)
	 * @param note
	 */
	private void insertAccountLog(int userId, float money, int log_type, int points, String adminName, String log_value,
			String note) {
		// 日志部分
		PageData accountLog = new PageData();// 日志
		accountLog.put("user_id", userId);// 用户ID
		accountLog.put("admin_name", adminName);// 管理员ID
		accountLog.put("log_money", money);// 变更金额
		accountLog.put("log_points", points);// 变更金额
		accountLog.put("log_type", log_type);// 日志类型
		accountLog.put("log_order", log_value);// 日志外键
		accountLog.put("log_time", EADate.getCurrentTime());// 日志时间
		accountLog.put("log_note", note);// 日志备注
		accountLog.put("log_symbol", "0");// 金额正负
		userAccountLogMapper.insert(accountLog);
	}

	/**
	 * =========================================================================
	 * ===================================== 会员钱包操作--------------------------结束
	 * =========================================================================
	 * =====================================
	 */

	/**
	 * 查询用户账号流水记录 "必须指定用户user_id"
	 * 
	 * @param page
	 * @return
	 */
	public List<PageData> getAccountLogByPage(Page page) {
		if (EAUtil.isEmpty(page) || EAUtil.isEmpty(page.getPd().get("user_id"))) {
			System.out.println("必须传入用户ID即user_id");
			return null;
		}
		return userAccountLogMapper.selectByMap(page);
	}

	/**
	 * 查询用户钱包 "必须指定用户user_id"
	 * 
	 * @param page
	 * @return
	 */
	public PageData getAccountByUserId(int user_id) {
		return userAccountMapper.selectByUserId(user_id);
	}

	@Override
	public EADao getDao() {
		return userAccountMapper;
	}

	/**
	 * 冻结（释放）用户冻结积分
	 * 
	 * @param pd
	 * @return
	 */
	public int updataUserpoints(PageData pd) {
		return userAccountMapper.updatePoints(pd);
	}

	/**
	 * ByUserId
	 * 
	 * @param pd
	 * @return
	 */
	public PageData selectCcountByUserId(int id) {
		return userAccountMapper.selectByUserId(id);
	}

}