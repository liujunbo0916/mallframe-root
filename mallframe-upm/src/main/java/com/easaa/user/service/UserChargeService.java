package com.easaa.user.service;

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
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserChargeMapper;
import com.easaa.user.dao.UserMapper;
import com.easaa.user.dao.UserPointsLogMapper;

/**
 * 会员充值
 * 
 * @author 约
 *
 */
@Service
public class UserChargeService extends EABaseService {
	@Autowired
	private UserChargeMapper userChargeMapper;
	
	@Autowired
	private UserAccountLogMapper useraccountlogMapper;
	
	@Autowired
	private UserAccountMapper userAccountMapper;
	
	@Autowired
	private UserPayLogMapper userPayLogMapper;
	
	@Autowired
	private UserPointsLogMapper userPointsLogMapper;
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public EADao getDao() {
		return userChargeMapper;
	}

	public PageData addUserCharge(PageData pd) {
		int uc = userChargeMapper.insert(pd);
		pd.put("id", pd.getAsInt("id"));
		/**
		 * 插入用户钱包明细 金额变更类型(充值、提现 冻结、解冻、手动更改等） 1:订单支付 2:订单取消 3:充值 4:提现 5:冻结 6:解冻
		 * 7:手动更改 8:分享赠送:9退货
		 */
//		PageData accpd = new PageData();
//		accpd.put("user_id", wu.getAsString("user_id"));
//		accpd.put("admin_name", pd.getAsString("admin_id"));
//		accpd.put("log_money", "-" + wu.getAsString("amount"));
//		accpd.put("log_points", 0);
//		accpd.put("log_type", 3);
//		accpd.put("log_value", EAString.getSn());
//		accpd.put("log_time", EADate.getCurrentTime());
//		accpd.put("log_note", pd.getAsString("admin_note"));
//		useraccountlogMapper.insert(accpd);
		return pd;
	}
	/**
	 * 
	 * 微信充值回调
	 * 
	 * @param param
	 * @return
	 */
	public boolean doWxCall(PageData param){
		Page selectParam = new Page();
		//更改订单状态
		String orderNo = param.getAsString("out_trade_no"); //商品订单号
		PageData order = userChargeMapper.selectBySn(orderNo);
		if("1".equalsIgnoreCase(order.getAsString("pay_status"))){
			return true;
		}
		order.put("pay_status", "1");
		order.put("status", 2);
		userChargeMapper.update(order);
		/**
		 * 更改用户钱包
		 */
		PageData userAccount = new PageData();
		userAccount.put("user_id", order.getAsString("user_id"));
		selectParam.setPd(userAccount);
		List<PageData> userAccounts = userAccountMapper.selectByMap(selectParam);
		if(userAccounts == null || userAccounts.size() == 0){
			//创建用户钱包
			userAccount.put("user_money", order.getAsString("amount"));
			userAccount.put("charge_points", order.getAsString("give_point"));
			userAccount.put("charge_money", order.getAsString("amount"));
			userAccount.put("user_points", order.getAsString("give_point"));
			userAccount.put("frozen_money", "0");
			userAccount.put("withdraw_money", "0");
			userAccount.put("order_money", "0");
			userAccount.put("rebate_money", "0");
			userAccount.put("frozen_points", "0");
			userAccount.put("order_points", "0");
			userAccount.put("rebate_points", "0");
			userAccount.put("share_points", "0");
			userAccount.put("change_time", EADate.getCurrentTime());
			userAccountMapper.insert(userAccount);
		}else{
			userAccount = userAccounts.get(0);
		}
		userAccount.put("user_money", userAccount.getAsBigDecimal("user_money").add(order.getAsBigDecimal("amount")));
		userAccount.put("charge_money", userAccount.getAsBigDecimal("charge_money").add(order.getAsBigDecimal("amount")));
		userAccount.put("charge_points", EAString.stringToInt(userAccount.getAsString("charge_points"), 0)+EAString.stringToInt(order.getAsString("give_point"), 0));
		System.out.println("<><><>><><><><><>"+EAString.stringToInt(order.getAsString("give_point"), 0));
		userAccount.put("user_points", EAString.stringToInt(userAccount.getAsString("user_points"), 0)+EAString.stringToInt(order.getAsString("give_point"), 0));
		userAccountMapper.update(userAccount);
		/**
		 * 插入钱包日志
		 */
		PageData accountLog = new PageData();
		accountLog.put("user_id", userAccount.getAsString("user_id"));
		accountLog.put("log_money", order.getAsString("amount"));
		accountLog.put("log_points", order.getAsString("give_point"));
		accountLog.put("log_type", "1");
		accountLog.put("log_symbol", "0");
		accountLog.put("log_note","国士风账户充值");
		accountLog.put("log_order",order.getAsString("charge_sn"));
		accountLog.put("log_time", EADate.getCurrentTime());
		useraccountlogMapper.insert(accountLog);
		/**
		 * 插入支付记录
		 */
		PageData payLog = new PageData();
		payLog.put("pay_type", param.getAsString("pay_type"));
		payLog.put("pay_money", order.getAsString("amount"));
		payLog.put("pay_time", EADate.getCurrentTime());
		payLog.put("user_id", order.getAsString("user_id"));
		payLog.put("pay_note", "用户充值");
		payLog.put("pay_source", "3");
		payLog.put("pay_order",order.getAsString("charge_sn"));
		userPayLogMapper.insert(payLog);
		/**
		 * 积分变动明细
		 */
		PageData pointsLog = new PageData();
		pointsLog.put("user_id", order.getAsString("user_id"));
		pointsLog.put("log_pay_order", order.getAsString("charge_sn"));
		pointsLog.put("log_time", EADate.getCurrentTime());
		pointsLog.put("log_role", "1");
		if(StringUtils.isNotEmpty(order.getAsString("give_point")) 
				&& !"0".equalsIgnoreCase(order.getAsString("give_point"))){
			pointsLog.put("log_points", order.getAsString("give_point"));
			pointsLog.put("log_expenditure", "1"); //收入
			pointsLog.put("log_type", "1");
			pointsLog.put("log_note", "充值：订单  "+order.getAsString("charge_sn")+" 赠送："+order.getAsString("give_point")+" 积分");
			userPointsLogMapper.insert(pointsLog);
		}
		PageData user = userMapper.getUserInfoById(order.getAsInt("user_id"));
		if (EAUtil.isNotEmpty(user)) {
			String content = "";
			String[] userid = null;
			String phone = user.getAsString("phone");
			if (param.getAsString("pay_type").equals("1")) {
				userid = new String[] { order.getAsString("user_id") };
				content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风使用支付宝充值了国士风平台： "+order.getAsBigDecimal("amount")+" 元,充值赠送: "+order.getAsString("give_point")+" 积分,感谢信任国士风平台！";
			} else if (param.getAsString("pay_type").equals("0") || param.getAsString("pay_type").equals("3")) {
				content =  "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风使用微信充值了国士风平台： "+order.getAsBigDecimal("amount")+" 元,充值赠送: "+order.getAsString("give_point")+" 积分,感谢信任国士风平台！";
			}
			Sender.sendUserMessage(userid, content, 3, phone);
			/*String str = "";
			if(param.getAsString("pay_type").equals("1")){
				JPushYthdUtil.userBuyPush("会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风使用支付宝充值了国士风平台： "+order.getAsBigDecimal("amount")+" 元,充值赠送: "+order.getAsString("give_point")+" 积分,感谢信任国士风平台！",
						order.getAsString("user_id"));
				String content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风使用支付宝充值了国士风平台： "+order.getAsBigDecimal("amount")+" 元,充值赠送: "+order.getAsString("give_point")+" 积分,感谢信任国士风平台！";
				if (EAUtil.isNotEmpty(user.getAsString("phone"))) {
					str = EASMS.sendSms2(user.getAsString("phone"), content);
				}
			}else if(param.getAsString("pay_type").equals("0") || param.getAsString("pay_type").equals("3")){
				JPushYthdUtil.userBuyPush("会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风使用微信充值了国士风平台： "+order.getAsBigDecimal("amount")+" 元,充值赠送: "+order.getAsString("give_point")+" 积分,感谢信任国士风平台！",
						order.getAsString("user_id"));
				String content = "会员:" + user.getAsString("nick_name") + ",您好！感谢您在国士风使用微信充值了国士风平台： "+order.getAsBigDecimal("amount")+" 元,充值赠送: "+order.getAsString("give_point")+" 积分,感谢信任国士风平台！";
				if (EAUtil.isNotEmpty(user.getAsString("phone"))) {
					str = EASMS.sendSms2(user.getAsString("phone"), content);
				}
			}
			if (str.equals("2")) {
				System.out.println("充值成功发送短信成功！：" + str);
			} else {
				System.out.println("充值成功发送短信失败！：" + str);
			}*/
		}
		return true;
	}
}