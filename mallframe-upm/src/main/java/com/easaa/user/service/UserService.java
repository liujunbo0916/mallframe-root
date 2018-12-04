package com.easaa.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EACheckUtil;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EATools;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.MD5;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.upm.upm.contants.Const;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserMapper;
import net.sf.json.JSONObject;

@Service
public class UserService extends EABaseService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserAccountMapper userAccountMapper;

	@Override
	public int create(PageData model) {
		// TODO Auto-generated method stub
		return getDao().insert(model);
	}

	/**
	 * 登录逻辑处理
	 * 
	 * @throws Exception
	 */
	public void doLogin(PageData pd, HttpServletRequest request) throws Exception {
		String KEYDATA[] = pd.getString("KEYDATA").split(",ea,");
		HttpSession session = request.getSession();
		String USERNAME = KEYDATA[0]; // 登录过来的用户名
		String PASSWORD = KEYDATA[1]; // 登录过来的密码
		pd.put("user_name", USERNAME);
		pd.put("password", PASSWORD);
		pd = userMapper.getUserInfo(pd); // 根据用户名和密码去读取用户信息
		if (EAUtil.isNotEmpty(pd)) {
			session.setAttribute("userInfo", pd); // 把用户信息放session中
		} else {
			throw new Exception(USERNAME + "登录系统密码或用户名错误");
		}
	}

	/**
	 * 创建一个新的用户,创建用户的同时需要创建用户钱包和积分
	 * 
	 * @param model
	 *            用户对象
	 * @param recCode
	 *            推荐码
	 * @return
	 */
	public int createUser(PageData model) {
		PageData recommed = new PageData();
		/**
		 * 判断推荐码是否重复
		 */
		List<PageData> results = null;
		String recommendCode = null;
		do {
			recommendCode = EATools.getRegisterCode(6, "4").toUpperCase();
			recommed.put("recommend_code", recommendCode);
			results = getByMap(recommed);

		} while (results != null && results.size() > 0);
		model.put("recommend_code", recommendCode);
		recommed.clear();
		int rec = getDao().insert(model);
		int user_id = model.getAsInt("user_id");
		recommed.put("user_id", user_id);
		recommed.put("user_1", user_id);
		if (rec > 0 & model.getAsInt("user_id") > 0) {// 如果插入用户成功
			// 创建用户钱包记录
			PageData account = new PageData();
			account.put("user_id", model.get("user_id"));
			account.put("user_money", "0");
			account.put("frozen_money", "0");
			account.put("charge_money", "0");
			account.put("withdraw_money", "0");
			account.put("order_money", "0");
			account.put("change_time", EADate.getCurrentTime());
			userAccountMapper.insert(account);
		}
		// 更新推荐人
		/**
		 * 1，新会员注册会影响上级会员的一级分销人数 上上级会员的二级分销人数 上上上级会员的三级分销人数 分别查出来设置 2，
		 * 将自己的直接上级会员查出来存到表里面的一级分销用户字段中 将上上级会员查出来存到二级分销会员的字段中
		 * 将上上上级会员查出来存到三级分销会员的字段中
		 */
		if (model.getAsInt("parent_id") > 0) {
			/**
			 * 查询上级推荐人 更改上级推荐人的一级分销员的统计数量
			 */
			PageData user = userMapper.selectById(model.getAsInt("parent_id"));
			int childCount = user.getAsInt("child_2_count");// 新注册用户是推荐人的二级分销会员
			childCount = childCount + 1;
			user.put("child_2_count", childCount);
			recommed.put("user_2", user.getAsInt("user_id"));
			userMapper.update(user);
			// 更新用户关系表
			PageData ur1 = new PageData();
			ur1.put("rec_user_id", user.getAsInt("user_id"));
			ur1.put("child_user_id", user_id);
			ur1.put("rec_type", 2);
			ur1.put("create_time", EADate.getCurDate());
			userMapper.insertRelation(ur1);
			if (user.getAsInt("parent_id") > 0) {
				// 说明上级分销会员有上级分销
				user = userMapper.selectById(user.getAsInt("parent_id"));
				childCount = user.getAsInt("child_3_count");// 新注册用户是推荐人的一级分销会员
				childCount = childCount + 1;
				user.put("child_3_count", childCount);
				recommed.put("user_3", user.getAsInt("user_id"));
				userMapper.update(user);
				// 更新用户关系表
				ur1 = new PageData();
				ur1.put("rec_user_id", user.getAsInt("user_id"));
				ur1.put("child_user_id", user_id);
				ur1.put("rec_type", 3);
				ur1.put("create_time", EADate.getCurDate());
				userMapper.insertRelation(ur1);
				/*
				 * if(user.getAsInt("parent_id") >0){
				 * user=userMapper.selectById(user.getAsInt("parent_id"));
				 * childCount=user.getAsInt("child_2_count");//新注册用户是推荐人的一级分销会员
				 * childCount=childCount+1; user.put("child_3_count",
				 * childCount); recommed.put("user_3", user.getAsInt("id"));
				 * userMapper.update(user); }
				 */
			}

			/**
			 * 将自己的返佣获得佣金的分销商入库
			 */
			userMapper.update(recommed);

			/**
			 * 插入用户分销列表
			 */
		}
		return user_id;
	}

	/**
	 * 插入用户推荐关联关系
	 * 
	 * @param recId
	 *            推荐人ID
	 * @param childId
	 *            被推荐人ID
	 * @param recType
	 *            推荐类型(1:一级;2:二级;3:三级)
	 * @return
	 */
	public PageData relation(int recId, int childId, int recType) {

		try {
			PageData reuslt = new PageData();
			// 根据父id和子id查询关系表里面是否存在关系
			List<PageData> relations = null;
			PageData targetUser = userMapper.selectById(recId);
			Integer child2Count = targetUser.getAsInteger("child_2_count");
			child2Count = child2Count == null ? 1 : child2Count + 1;
			targetUser.put("child_2_count", child2Count);
			userMapper.update(targetUser);
			PageData pd = new PageData();
			pd.put("rec_user_id", recId);
			pd.put("child_user_id", childId);
			relations = userMapper.selectRelationByCondition(pd);
			if (relations == null || relations.size() == 0) {
				pd.put("rec_type", 2);
				pd.put("create_time", EADate.getCurrentTime());
				userMapper.insertRelation(pd);
			}
			if (StringUtils.isNotEmpty(targetUser.getAsString("parent_id"))
					&& !targetUser.getAsString("parent_id").equals("0")) {
				PageData parentPd = userMapper.selectById(EAString.stringToInt(targetUser.getAsString("parent_id"), 0));
				if (parentPd != null) {
					reuslt.put("user_3", parentPd.getAsString("user_id"));
					Integer child3Count = parentPd.getAsInteger("child_3_count");
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
			reuslt.put("user_2", recId);
			return reuslt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 查询 recId上级 如果有上级 将上级和childID的对应关系插入数据库
	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 * @return 大于0表示成功,-2没找到用户,-3旧密码不对
	 */
	public int updatePassword(int userId, String oldPassword, String nPassword) {
		PageData user = getById(userId);
		if (EAUtil.isEmpty(user)) {
			return -2;// 用户不存在
		}
		if (!oldPassword.equals(user.getAsString("password"))) {
			return -3;
		}
		user.put("password", nPassword);
		return this.edit(user);
	}

	/**
	 * 获取指定会员的推荐会员
	 * 
	 * @param userId
	 * @return 返回格式(p1:一级会员;p2:二级会员;p3:三级会员
	 */
	public PageData getAllParentByUserID(int userId) {
		PageData udata = userMapper.selectById(userId);
		return getAllParentByUser(udata);
	}

	/**
	 * 用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public PageData getUserInfo(int userId) {
		return userMapper.getUserInfoById(userId);
	}

	/**
	 * 获取指定会员的推荐会员
	 * 
	 * @param userId
	 * @return 返回格式(p1:一级会员;p2:二级会员;p3:三级会员
	 * 
	 *         返利 一级会员是自己 二级会员是上级 三级会员是上上级
	 */
	public PageData getAllParentByUser(PageData user) {
		/*
		 * if (EAUtil.isEmpty(user) || EAUtil.isEmpty(user.get("parent_id")))
		 * {// 没上级 return null; } PageData pd = new PageData(); PageData p1 =
		 * getDao().selectById(user.getAsInt("user_1")); pd.put("p1", p1); if
		 * (EAUtil.isNotEmpty(p1) && p1.getAsInt("parent_id") > 0) { PageData p2
		 * = getDao().selectById(p1.getAsInt("parent_id")); pd.put("p2", p2); if
		 * (EAUtil.isNotEmpty(p2) && EAUtil.isNotEmpty(p2.get("parent_id"))) {
		 * PageData p3 = getDao().selectById(p2.getAsInt("parent_id"));
		 * pd.put("p3", p3); } }
		 */
		PageData pd = new PageData();
		if (EAUtil.isEmpty(user)) {
			return null;
		}
		pd.put("p1", user);
		if (EAUtil.isNotEmpty(user.get("parent_id")) && user.getAsInt("parent_id") != 0) {
			PageData p2 = getDao().selectById(user.getAsInt("parent_id"));
			pd.put("p2", p2);
			if (EAUtil.isNotEmpty(p2) && EAUtil.isNotEmpty(p2.get("parent_id")) && p2.getAsInt("parent_id") != 0) {
				PageData p3 = getDao().selectById(p2.getAsInt("parent_id"));
				pd.put("p3", p3);
			}
		}
		return pd;
	}

	/**
	 * 获取指定会员的推荐会员（原来的）
	 * 
	 * @param userId
	 * @return 返回格式(p1:一级会员;p2:二级会员;p3:三级会员
	 */
	public PageData getAllParentByUsers(PageData user) {
		if (EAUtil.isEmpty(user) || EAUtil.isEmpty(user.get("parent_id"))) {// 没上级
			return null;
		}
		PageData pd = new PageData();
		PageData p1 = getDao().selectById(user.getAsInt("parent_id"));
		pd.put("p1", p1);
		if (EAUtil.isNotEmpty(p1) && p1.getAsInt("parent_id") > 0) {
			PageData p2 = getDao().selectById(p1.getAsInt("parent_id"));
			pd.put("p2", p2);
			if (EAUtil.isNotEmpty(p2) && EAUtil.isNotEmpty(p2.get("parent_id"))) {
				PageData p3 = getDao().selectById(p2.getAsInt("parent_id"));
				pd.put("p3", p3);
			}
		}
		return pd;
	}

	/**
	 * 依据推荐码查询用户信息
	 */
	public PageData getUserByRecCode(String recommendCode) {
		return userMapper.selectByRecCode(recommendCode);
	}

	/**
	 * 查询下级会员, "rec_type 1:一级;2:二级;3:三级"
	 * 
	 * @param parameter
	 * @return
	 */
	public List<PageData> getChildsByPage(Page parameter) {
		return userMapper.selectChildsByPage(parameter);
	}

	/**
	 * 微信关注公众号时保存用户信息
	 * 
	 * @param pd
	 * @param request
	 * @throws Exception
	 */

	public synchronized void subscribe(PageData pd, HttpServletRequest request) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("open_id")) || pd.getAsString("open_id").equals("-1")) {
			return;
		}
		//JSONObject jsonObj = Wechat.readUserInfo(StringUtils.trim(pd.getAsString("open_id")));
		JSONObject jsonObj = (JSONObject) pd.get("jsonObj");
		/*
		 * PageData oldUser =
		 * userMapper.selectByOpenId(StringUtils.trim(pd.getAsString("open_id"))
		 * );
		 */
		// 利用unionid查询用户 避免用户在app授权之后再登陆公众号
		//System.out.println("<><>><><><><><><><><<><><<>>用户unionid<>><><<>><" + jsonObj.get("unionid").toString());
		PageData oldUser   = userMapper.selectByOpenId(jsonObj.get("openid").toString());
		pd.put("headimgurl", jsonObj.get("headimgurl"));
		pd.put("head_portrait", jsonObj.get("headimgurl"));
		pd.put("subscribe_time", jsonObj.get("subscribe_time"));
		pd.put("open_id", pd.getAsString("open_id"));
		pd.put("register_time", EADate.getCurrentTime());
		pd.put("phone", jsonObj.get("phone"));
		pd.put("register_channel", "1");
		pd.put("is_validated", "1");
		pd.put("is_rebate", "0");
		pd.put("status", 1);
		pd.put("user_type", 1);
		pd.put("is_commenter", 1);
		pd.put("is_loginer", 1);
		pd.put("is_buyer", 1);
		pd.put("nick_name",
				EAUtil.isEmpty(jsonObj.getString("nickname")) ? "GSF" + System.currentTimeMillis() : jsonObj.get("nickname"));
		pd.put("subscribe", 1);
		if (oldUser == null) {
			pd.put("parent_id", "0"); // 如果新注册的将父亲id改成0
			getDao().insert(pd);
			// 创建用户钱包
			// 查询用户是否有钱包 没有则创建用户钱包记录
		} else {
			pd.put("user_id", oldUser.getAsString("user_id"));
			getDao().update(pd);
		}
		createUserAccount(pd.getAsString("user_id"));
	}

	/**
	 * app微信授权登陆
	 * 
	 * @param args
	 */
	public PageData appAuthorization(PageData pd) throws Exception {
		// 根据unionid查询用户
		if (EAUtil.isEmpty(pd.getAsString("unionid")) || pd.getAsString("unionid").equals("-1")) {
			throw new Exception("授权失败！");
		}
		PageData userPd = userMapper.selectByUnionId(pd.getAsString("unionid"));
		pd.remove("openid");
		pd.put("headimgurl", pd.get("headimgurl"));
		pd.put("head_portrait", pd.get("headimgurl"));
		// pd.put("open_id", pd.getAsString("openid"));
		pd.put("register_time", EADate.getCurrentTime());
		pd.put("phone", "");
		pd.put("register_channel", "1");
		pd.put("is_validated", "1");
		pd.put("is_rebate", "0");
		pd.put("parent_id", "0");
		pd.put("nick_name", pd.get("nickname"));
		pd.put("user_type", 1);
		pd.put("is_commenter", 1);
		pd.put("is_loginer", 1);
		pd.put("is_buyer", 1);
		pd.put("status", 1);
		pd.put("extension_juri", 1);
		if (userPd == null) {
			getDao().insert(pd);
			createUserAccount(pd.getAsString("user_id"));
			return pd;
		} else {
			getDao().update(userPd);
			return userPd;
		}
	}


	/**
	 * 用户打开分享商品链接
	 */
	public void clickShareLink(PageData pd) {
		// 根据openid查询用户 用unionid匹配库里用户
		if (EAUtil.isEmpty(pd.getAsString("open_id")) || pd.getAsString("open_id").equals("-1")) {
			return;
		}
	//	JSONObject jsonObj = WechatUtil.readUserInfo(StringUtils.trim(pd.getAsString("open_id")));
		JSONObject jsonObj = (JSONObject) pd.get("jsonObj");
		// 查点击人的账号
		PageData clickUser = userMapper.selectByOpenId(jsonObj.get("openid").toString());
		PageData createUserPd = new PageData();
		createUserPd.put("head_portrait", jsonObj.get("headimgurl"));
		createUserPd.put("headimgurl", jsonObj.get("headimgurl"));
		createUserPd.put("subscribe_time", jsonObj.get("subscribe_time"));
		createUserPd.put("open_id", StringUtils.trim(pd.getAsString("open_id")));
		if (clickUser != null) {
			createUserPd.put("parent_id", clickUser.getAsString("parent_id"));
			createUserPd.put("phone", clickUser.getAsString("phone"));
			createUserPd.put("register_channel", clickUser.getAsString("register_channel"));
			createUserPd.put("is_validated", clickUser.getAsString("is_validated"));
			createUserPd.put("is_rebate", clickUser.getAsString("is_rebate"));
			createUserPd.put("user_type", clickUser.getAsString("user_type"));
			createUserPd.put("is_commenter", clickUser.getAsString("is_commenter"));
			createUserPd.put("is_loginer", clickUser.getAsString("is_loginer"));
			createUserPd.put("is_buyer", clickUser.getAsString("is_buyer"));
			createUserPd.put("extension_juri", clickUser.getAsString("extension_juri"));
			createUserPd.put("register_time", clickUser.getAsString("register_time"));
			createUserPd.put("subscribe", "1");
		} else {
			createUserPd.put("phone", "");
			createUserPd.put("register_channel", "1");
			createUserPd.put("is_validated", "1");
			createUserPd.put("is_rebate", "0");
			createUserPd.put("parent_id", "0");
			createUserPd.put("user_type", 1);
			createUserPd.put("is_commenter", 1);
			createUserPd.put("is_loginer", 1);
			createUserPd.put("is_buyer", 1);
			createUserPd.put("register_time", EADate.getCurrentTime());
			createUserPd.put("extension_juri", 1);
			createUserPd.put("subscribe", "0");
		}
		createUserPd.put("unionid", jsonObj.get("unionid"));
		createUserPd.put("nick_name",EAUtil.isEmpty(jsonObj.getString("nickname")) ? "GSF" + System.currentTimeMillis() : jsonObj.get("nickname"));
		// PageData clickUser =
		// userMapper.selectByOpenId(StringUtils.trim(pd.getAsString("open_id")));
		if (clickUser == null) { // 如果用户在平台里面存在 则看其是否有上级
			// 如果不存在账号 创建账号和钱包
			getDao().insert(createUserPd); // o6ZmsxJ1G3LTj7N5wHD0Hfiyd5KU
			// 创建用户钱包
			createUserAccount(createUserPd.getAsString("user_id"));
		} else {
			createUserPd.put("user_id", clickUser.getAsString("user_id"));
			createUserPd.put("phone", clickUser.getAsString("phone"));
			getDao().update(createUserPd);
		}
	}

	/**
	 * 创建用户钱包 1，查询用户钱包 2，如果没有钱包创建
	 * 
	 * @param user_id
	 */
	private void createUserAccount(String user_id) {
		PageData userAccount = userAccountMapper.selectByUserId(EAString.stringToInt(user_id, 0));
		if (userAccount != null) {
			return;
		}
		PageData account = new PageData();
		account.put("user_id", user_id);
		account.put("user_money", "0");
		account.put("frozen_money", "0");
		account.put("charge_money", "0");
		account.put("withdraw_money", "0");
		account.put("order_money", "0");
		account.put("change_time", EADate.getCurrentTime());
		userAccountMapper.insert(account);
	}

	public void unsubscribe(String openId) throws Exception {
		PageData user = userMapper.selectByOpenId(openId);
		if (user != null) {
			user.put("subscribe", "0");
			getDao().update(user);
		}
	}

	/**
	 * 查询用户登陆状态
	 */
	public PageData selectLoginStatus(PageData pd) {
		return userMapper.selectLoginStatus(pd);
	}

	/**
	 * 插入用户登陆记录
	 */
	public void insertLoginStatus(PageData pd) {
		userMapper.insertLoginStatus(pd);
	}

	/**
	 * 查询用户反馈
	 */
	public List<PageData> selectfeedback(PageData pd) {
		return userMapper.selectfeedback(pd);
	}

	/**
	 * 根据opendid查询
	 */
	public PageData selectByOpenId(String openId) {
		return userMapper.selectByOpenId(openId);
	}

	/**
	 * 后台条件查询所有会员
	 */
	public List<PageData> selectByAllPage(Page page) {
		return userMapper.selectByAllPage(page);
	}

	/**
	 * 插入反馈
	 */
	public boolean insertfeedback(PageData pd) {
		int index = userMapper.insertfeedback(pd);
		if (index > 0) {
			return true;
		}
		return false;
	}

	@Override
	public EADao getDao() {
		return userMapper;
	}

	/**
	 * 后台创建用户
	 * 
	 * @param args
	 */
	public PageData insertUser(PageData pd) {
		pd.put("headimgurl", pd.getAsString("head_portrait"));
		pd.put("head_portrait", pd.getAsString("head_portrait"));
		pd.put("register_time", EADate.getCurrentTime());
		pd.put("phone", pd.getAsString("user_name"));
		pd.put("password",StringUtils.isEmpty(pd.getAsString("password"))? MD5.md5(pd.getAsString("user_name")):MD5.md5(pd.getAsString("password")));
		pd.put("register_channel", "1");
		pd.put("is_validated", "1");
		pd.put("is_rebate","0");
		pd.put("parent_id", "0");
		pd.put("nick_name", pd.getAsString("user_name"));
		pd.put("user_type", StringUtils.isEmpty(pd.getAsString("user_type"))?"1":pd.getAsString("user_type"));
		pd.put("is_commenter", StringUtils.isEmpty(pd.getAsString("is_commenter"))?"1":pd.getAsString("is_commenter"));
		pd.put("is_loginer", StringUtils.isEmpty(pd.getAsString("is_loginer"))?"1":pd.getAsString("is_loginer"));
		pd.put("is_buyer", StringUtils.isEmpty(pd.getAsString("is_buyer"))?"1":pd.getAsString("is_buyer"));
		pd.put("status", StringUtils.isEmpty(pd.getAsString("status"))?"1":pd.getAsString("status"));
		pd.put("extension_juri", 1);
		userMapper.insert(pd);
		createUserAccount(pd.getAsString("user_id"));
		return pd;
	}

	
	/**
	 * 微信端绑定手机号   如果该手机号在pc端已经登录过 则直接合并账号  将openid 更新到pc账号里面
	 * @param user
	 * @throws Exception 
	 */
	public void doBingPhone(PageData user) throws Exception {
		PageData userInfo = userMapper.selectByUserName(user.getAsString("phone"));
		/**
		 * 用户之前手机号已经注册过  判断是否在微信端注册的。
		 */
		if(userInfo != null){
			
			if(StringUtils.isEmpty(user.getAsString("user_id"))){
				throw new Exception("用户id为空");
			}
			/**
			 * 如果根据手机账号查出来的用户的用户id和用openid查询的用户id一样  说明该账户已经绑定过手机号
			 */
			if(userInfo.getAsString("user_id").equals(user.getAsString("user_id"))){
				throw new Exception("请绑定新的手机号");
			}
			/**
			 * 如果手机号被另一个微信号绑定则提示这个手机号已经被绑定
			 * 
			 */
			if(StringUtils.isNotEmpty(userInfo.getAsString("open_id"))){
				throw new Exception("该手机号已被绑定");
			}
			/**
			 * 否则  则将要绑定的手机号的用户 的openid和图像
			 * 等一系列微信参数信息更新到用username查出的账户下面
			 */
			userInfo.put("phone", user.getAsString("phone"));
			userInfo.put("open_id", user.getAsString("open_id"));
			userInfo.put("subscribe_time", user.getAsString("subscribe_time"));
			userInfo.put("headimgurl", user.getAsString("headimgurl"));
			userInfo.put("unionid", user.get("unionid"));
			userInfo.put("subscribe", user.get("subscribe"));
			userInfo.put("nick_name", user.get("nick_name"));
			userInfo.put("head_portrait", user.get("head_portrait"));
			userMapper.update(userInfo);
			/**
			 * 删除原来的账号
			 */
			Page page = new Page();
			PageData deleteParam = new PageData();
			deleteParam.put("user_id", StringUtils.isEmpty(user.getAsString("user_id"))?"0":user.getAsString("user_id"));
			page.setPd(deleteParam);
			userMapper.delete(page);
		}else{
			user.put("user_name", user.getAsString("phone"));
			userMapper.update(user);
		}
	}

	public void doLoginByCode(PageData pd, HttpServletRequest request) throws Exception {
		
		String userName = pd.getAsString("username");
		if(StringUtils.isEmpty(userName)){
			throw new Exception("手机号码不能为空");
		}
		if(StringUtils.isEmpty(pd.getAsString("smsCode"))){
			throw new Exception("验证码超时");
		}
		if(!EACheckUtil.isMobileNO(userName)){
			throw new Exception("手机号码格式不正确");
		}
		/**
		 * 校验验证码
		 * 
		 */
		String serverCode = (String) request.getSession().getAttribute("smsCode");
		if(!pd.getAsString("smsCode").equals(serverCode)){
			throw new Exception("验证码错误");
		}
		/**
		 * 创建用户
		 */
		PageData userInfo = userMapper.selectByUserName(userName);
		if(userInfo == null){
			//创建用户
			userInfo = new PageData();
			userInfo.put("phone", userName);
			userInfo.put("user_name", userName);
			userInfo.put("register_channel", 4);
			userInfo.put("register_time", EADate.getCurrentTime());
			userInfo.put("user_money", 0);
			userInfo.put("user_points", 0);
			userInfo.put("is_validated", 0);
			userInfo.put("is_buyer", 1);
			userInfo.put("is_loginer", 1);
			userInfo.put("user_type", 1);
			userMapper.insert(userInfo);
		}
		request.getSession().setAttribute("userInfo", userInfo);
	}

	public List<PageData> selectRelationByCondition(PageData pd) {
		return userMapper.selectRelationByCondition(pd);
	}
}