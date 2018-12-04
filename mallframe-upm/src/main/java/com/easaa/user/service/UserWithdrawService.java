package com.easaa.user.service;

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
import com.easaa.user.dao.UserAccountLogMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserBankMapper;
import com.easaa.user.dao.UserBindingMapper;
import com.easaa.user.dao.UserWithdrawMapper;

@Service
public class UserWithdrawService extends EABaseService {
	@Autowired
	private UserWithdrawMapper userWithdrawMapper;
	@Autowired
	private UserBankMapper userBankMapper;
	@Autowired
	private UserBindingMapper userBindingMapper;
	@Autowired
	private UserAccountMapper userAccountMapper;
	@Autowired
	private UserAccountLogMapper useraccountlogMapper;

	@Override
	public EADao getDao() {
		return userWithdrawMapper;
	}

	/**
	 * 查询银行
	 */
	public List<PageData> selectBank(Page model) {
		return userBankMapper.selectByMap(model);
	}

	/**
	 * 查询用户绑定银行卡信息
	 */
	public List<PageData> selectBinding(PageData page) {
		return userBindingMapper.selectBinding(page);
	}

	/**
	 * 用户绑定银行卡
	 */
	public int createBinding(PageData model) {
		return userBindingMapper.insert(model);
	}

	/**
	 * 用户绑定银行卡
	 */
	public PageData getBindingId(int user_id) {
		return userBindingMapper.selectById(user_id);
	}

	/**
	 * 用户绑定银行卡列表
	 */
	public List<PageData> getBindingMap(Page page) {
		return userBindingMapper.selectByMap(page);
	}

	/**
	 * 新增用户提现
	 */
	public void addWithDraw(PageData pd) {
		Page page = new Page();
		PageData data = new PageData();
		data.put("create_time", EADate.getCurrentTime());
		data.put("withdraw_sn", EAString.getSn());
		// 总金额（提现金额，提现手续费）
		data.put("amount", pd.get("amount"));
		data.put("bank_name", pd.getAsString("bank_name"));
		data.put("bank_type", pd.getAsString("bank_type"));
		data.put("bank_card_no", pd.getAsString("bank_card_no"));
		data.put("status", "0");
		data.put("withdraw_fee", pd.getAsString("withdraw_fee"));
		data.put("user_id", pd.getAsString("user_id"));
		page.setPd(data);
		PageData account = userAccountMapper.selectByMap(page).get(0);
		/**
		 * 金额后台校验
		 */
		/**
		 * 扣除用户账户余额
		 */
		userWithdrawMapper.insert(data);
		System.out.println("体现金额《》《》《《》》《》"+pd.getAsBigDecimal("amount").doubleValue());
		System.out.println("账户余额《》《》《《》》《》"+account.getAsBigDecimal("user_money").doubleValue());
		System.out.println("更改后余额《》《》《《》》《》"+ account.getAsBigDecimal("user_money").subtract(pd.getAsBigDecimal("amount")));
		account.put("user_money", account.getAsBigDecimal("user_money").subtract(pd.getAsBigDecimal("amount")));
		account.put("withdraw_money", account.getAsBigDecimal("withdraw_money").add(pd.getAsBigDecimal("amount")));
		userAccountMapper.update(account);
	}

	/**
	 * 用户提现确认
	 */
	public void updateSureWithDraw(PageData pd) throws Exception{
		// 1未打款 2已打款 0申请无效
		PageData accpd;
		if (pd.getAsString("status").equals("2")) {
			/**
			 * 插入用户钱包明细 金额变更类型(充值、提现 冻结、解冻、手动更改等） 1:订单支付 2:订单取消 3:充值 4:提现 5:冻结
			 * 6:解冻 7:手动更改 8:分享赠送:9退货
			 */
			accpd = new PageData();
			PageData wu = userWithdrawMapper.selectById(pd.getAsInt("wit_id"));
			if (!wu.getAsString("status").equals("1")) {
				throw new Exception("此订单已经打款或者失效！");
			}
			accpd.put("user_id", wu.getAsString("user_id"));
			accpd.put("admin_name", pd.getAsString("admin_id"));
			accpd.put("log_money", wu.getAsBigDecimal("amount"));
			accpd.put("log_points", "0");
			accpd.put("log_type", 4);
			accpd.put("log_symbol", 1);//金额正负
			accpd.put("log_value", EAString.getSn());
			accpd.put("log_time", EADate.getCurrentTime());
			accpd.put("log_note", pd.getAsString("admin_note"));
			useraccountlogMapper.insert(accpd);
			if (EAUtil.isNotEmpty(wu.getAsString("withdraw_fee")) && wu.getAsDouble("withdraw_fee") > 0) {
				accpd = new PageData();
				accpd.put("user_id", wu.getAsString("user_id"));
				accpd.put("admin_name", pd.getAsString("admin_id"));
				accpd.put("log_money",  wu.getAsBigDecimal("withdraw_fee"));
				accpd.put("log_points", "0");
				accpd.put("log_type", 4);
				accpd.put("log_symbol", 1);//金额正负
				accpd.put("log_value", EAString.getSn());
				accpd.put("log_time", EADate.getCurrentTime());
				accpd.put("log_note", pd.getAsString("admin_note"));
				useraccountlogMapper.insert(accpd);
			}
		} else if (pd.getAsString("status").equals("0")) {
			PageData wus = userWithdrawMapper.selectById(pd.getAsInt("wit_id"));
			if (!wus.getAsString("status").equals("1")) {
				throw new Exception("此订单已失效！");
			}
			accpd = new PageData();
			accpd.put("user_id", wus.getAsString("user_id"));
			Page page = new Page();
			page.setPd(accpd);
			PageData account = userAccountMapper.selectByPage(page).get(0);
			account.put("user_money", account.getAsBigDecimal("user_money").add(wus.getAsBigDecimal("amount")));
			account.put("withdraw_money", account.getAsBigDecimal("withdraw_money").subtract(wus.getAsBigDecimal("amount")));
 			userAccountMapper.update(account);
		}
		userWithdrawMapper.update(pd);
	}

}