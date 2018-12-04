package com.easaa.order.service;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.MD5;
import com.easaa.core.util.RSA;
import com.easaa.core.util.Util;
import com.easaa.course.dao.VenueOrderMapper;
import com.easaa.entity.PageData;
import com.easaa.order.dao.OrderMapper;
import com.easaa.order.dao.OrdersPayMapper;
import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;
import com.tencent.WXPay;
import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.XMLParser;
@Service
public class OrdersPayService extends EABaseService{
	@Autowired
	private OrdersPayMapper ordersPayMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private VenueOrderMapper venueOrderMapper;
	
	public EADao getDao(){
		return ordersPayMapper;
	}
	
	/**
	 * 创建支付订单
	 * @return
	 * pay_type //支付类型(微信00702，支付宝00701,00703 余额支付)
	 * 
	 * type 支付类型 type =1   商品支付  type=2    课程支付 type =3 场馆支付   type=4 充值
	 * 
	 * @throws Exception
	 */
	public PageData createActivityOrder(PageData pd) throws Exception {
		//获取系统配置参数
		PropertiesHelper param = PropertiesFactory.getPropertiesHelper(PropertiesFile.SYS); 
		/**
		 * 查询订单
		 */
		PageData order = null;
		String body = "";
		String billNo  = "";
		String notifyUrl = "";
		
		String order_id = pd.getAsString("order_id");        //订单id
		String pay_type = pd.getAsString("pay_type");        //支付类型(微信00702，支付宝00701，00700余额支付)
		// 根据订单id查询订单是否有效
		if (StringUtils.isEmpty(order_id)) {
			throw new Exception("订单id不能为空");
		}
		PageData orderPd =  orderMapper.selectById(Integer.parseInt(pd.getString("order_id")));
		if (EAUtil.isEmpty(orderPd)) {
			throw new Exception("订单不存在");
		}
		String pay_money = "0.01";    //订单额度
		//String pay_money = orderPd.getAsString("order_money");    //订单额度
		//String order_type = pd.getAsString("order_type");       //订单类型

		if (StringUtils.isEmpty(pay_money)) {
			throw new Exception("支付金额不能空");
		}
		if (StringUtils.isEmpty(pay_type)) {
			throw new Exception("支付类型不能为空");
		}
		/*if (StringUtils.isEmpty(order_type)) {
			throw new Exception("支付类型不能为空");
		}*/
		pd.put("pay_money", pay_money);   //先固定下
		pd.put("order_id", order_id);
		//pd.put("user_id", user_id);
		//pd.put("order_type", order_type);
		pd.put("pay_type", pay_type);
		pd.put("pay_sn", this.createPaySn());    //支付流水号
		pd.put("create_time", EADate.getCurrentTime());   
		
	/*	if (orderPd.get("order_money") != null) {
			//订单的金额跟支付金额是否一致
			if (orderPd.getAsBigDecimal("order_money").compareTo(BigDecimal.valueOf(Double.parseDouble(pay_money))) != 0) {
				throw new Exception("非法支付");
			}
		} else {
			throw new Exception("此订单的不需要支付");
		}*/
		// 保存预支付订单
		ordersPayMapper.insert(pd);
		PageData reParam = new PageData();     //返回参数
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();   //
		if (pay_type.equalsIgnoreCase("00701")) { // 支付宝支付

			String[] parameters = { 
					"partner=\"" + param.getValue("Ali_PARTNER") + "\"",                   // 商户号
					"seller_id=\"" + param.getValue("Ali_SELLER") + "\"",                  // 账户
					"out_trade_no=\"" + String.valueOf(pd.getAsString("pay_sn")) + "\"",   // 商户订单号（流水号）
					"subject=\"" + param.getValue("zf_title") + "\"",                      // 订单标题
					"body=\"" + param.getValue("zf_title") + "\"",                         //订单描述
					"total_fee=\"" + pay_money + "\"",                                     // 支付金额（元）
					"notify_url=\"" + param.getValue("Ali_NOTIFY_URL") + "\"",             // 回调地址
					"service=\"mobile.securitypay.pay\"",                                  // 固定值
					"payment_type=\"1\"",                                                  // 固定值
					"_input_charset=\"utf-8\"", 
					"it_b_pay=\"30m\"" };
			String result = signAllString(parameters, param.getValue("Ali_RSA_PRIVATE"));
			reParam.put("info", result);

			System.out.println("===========================>>result:" + result);

			return reParam;
		} else if (pay_type.equalsIgnoreCase("00702")) {// 微信支付
			WXPay.initSDKConfiguration(param.getValue("wx_key"), param.getValue("wx_appid"), param.getValue("wx_shid"),
					null, null, null);
			String totalmoney = String.format("%.0f",
					BigDecimal.valueOf(Double.parseDouble(pay_money)).doubleValue() * 100);                     // 支付金额（元）
			signParams.add(new BasicNameValuePair("appid", param.getValue("wx_appid")));                        // 应用ID
			signParams.add(new BasicNameValuePair("body", param.getValue("zf_title")));                         // 商品描述
			signParams.add(new BasicNameValuePair("mch_id", param.getValue("wx_mchid")));                       // 商户号
			String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
			signParams.add(new BasicNameValuePair("nonce_str", nonce_str));                                     // 随机字符串   
			signParams.add(new BasicNameValuePair("notify_url", param.getValue("wx_notity_url").toString()));   // 回调地址
			signParams.add(new BasicNameValuePair("out_trade_no", String.valueOf(pd.getAsString("pay_sn"))));   // 商户订单号
			signParams.add(new BasicNameValuePair("spbill_create_ip", "58.250.174.48"));                        // 终端IP
			signParams.add(new BasicNameValuePair("total_fee", totalmoney));                                    // 总金额
			signParams.add(new BasicNameValuePair("trade_type", "APP"));                                        // 交易类型
			String sign = genPackageSign(signParams);                                                           
			signParams.add(new BasicNameValuePair("sign", sign));                                               //签名
			byte[] data = Util.httpPost(param.getValue("wx_url"), toXml(signParams));  //第一次签名认证，wx_url接口链接
			String result = new String(data);
			Map<String, Object> mapdata = XMLParser.getMapFromXML(result);
			if (mapdata != null) {
				if ("SUCCESS".equals(mapdata.get("return_code")) && "SUCCESS".equals(mapdata.get("result_code"))) {
					String prepay_id = (String) mapdata.get("prepay_id");              //微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
					String nonceStr = (String) mapdata.get("nonce_str");               //微信返回的随机字符串
					/**
					 * 二次签名
					 */
					List<NameValuePair> sencondSign = new LinkedList<NameValuePair>();
					sencondSign.add(new BasicNameValuePair("appid", param.getValue("wx_appid")));            //微信开放平台审核通过的应用APPID
					String noncestrS = RandomStringGenerator.getRandomStringByLength(32);
					sencondSign.add(new BasicNameValuePair("noncestr", noncestrS));                           //随机字符串(不需返回)
					sencondSign.add(new BasicNameValuePair("package", "Sign=WXPay"));                         //扩展字段
					sencondSign.add(new BasicNameValuePair("partnerid", param.getValue("wx_mchid")));         //商户号
					sencondSign.add(new BasicNameValuePair("prepayid", prepay_id));                           //微信返回的支付交易会话ID
					sencondSign.add(new BasicNameValuePair("timestamp", (System.currentTimeMillis() / 1000) + ""));   //时间戳
					String signS = genPackageSign(sencondSign);                    //签名
					reParam.put("appID", param.getValue("wx_appid"));
					reParam.put("package", "Sign=WXPay");
					reParam.put("partnerId", param.getValue("wx_mchid"));
					reParam.put("nonceStr", noncestrS);
					reParam.put("prepayId", prepay_id);
					reParam.put("sign", signS);
					reParam.put("timestamp", (System.currentTimeMillis() / 1000) + "");
				} else {
					throw new Exception("支付提醒:" + (String) mapdata.get("return_msg"));
				}
			} else {
				throw new Exception("支付提醒:服务器异常");
			}
		} else if (pay_type.equalsIgnoreCase("00703")) { // 银联支付
			throw new Exception("不支持网银支付");
		}
		return reParam;
	}
	
	
	
	
	
	
	
	
	/**
	 * 微信支付订单
	 * @return
	 * @throws Exception
	 */
	public PageData wechatPayOrder(PageData pd) throws Exception {
		PropertiesHelper param = PropertiesFactory.getPropertiesHelper(PropertiesFile.SYS);     //获取系统配置参数
		String order_id = pd.getAsString("order_id");        //订单id
		String pay_type = pd.getAsString("pay_type");        //支付类型(微信00702，支付宝00701)
		// 根据订单id查询订单是否有效
		if (StringUtils.isEmpty(order_id)) {
			throw new Exception("订单id不能为空");
		}
		PageData orderPd =  orderMapper.selectById(Integer.parseInt(pd.getString("order_id")));
		if (EAUtil.isEmpty(orderPd)) {
			throw new Exception("订单不存在");
		}
		String pay_money = "0.01";    //订单额度
		//String pay_money = orderPd.getAsString("order_money");    //订单额度
		//String order_type = pd.getAsString("order_type");       //订单类型

		if (StringUtils.isEmpty(pay_money)) {
			throw new Exception("支付金额不能空");
		}
		if (StringUtils.isEmpty(pay_type)) {
			throw new Exception("支付类型不能为空");
		}
		/*if (StringUtils.isEmpty(order_type)) {
			throw new Exception("支付类型不能为空");
		}*/
		pd.put("pay_money", pay_money);   //先固定下
		pd.put("order_id", order_id);
		//pd.put("user_id", user_id);
		//pd.put("order_type", order_type);
		pd.put("pay_type", pay_type);
		pd.put("pay_sn", this.createPaySn());    //支付流水号
		pd.put("create_time", EADate.getCurrentTime());   
		
		// 保存预支付订单
		ordersPayMapper.insert(pd);
		PageData reParam = new PageData();     //返回参数
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();   //

		//微信支付
			WXPay.initSDKConfiguration(param.getValue("wx_key"), param.getValue("wx_appid"), param.getValue("wx_shid"),
					null, null, null);
			String totalmoney = String.format("%.0f",
					BigDecimal.valueOf(Double.parseDouble(pay_money)).doubleValue() * 100);                     // 支付金额（元）
			signParams.add(new BasicNameValuePair("appid", param.getValue("wx_appid")));                        // 应用ID
			signParams.add(new BasicNameValuePair("body", param.getValue("zf_title")));                         // 商品描述
			signParams.add(new BasicNameValuePair("mch_id", param.getValue("wx_mchid")));                       // 商户号
			String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
			signParams.add(new BasicNameValuePair("nonce_str", nonce_str));                                     // 随机字符串   
			signParams.add(new BasicNameValuePair("notify_url", param.getValue("wx_notity_url").toString()));   // 回调地址
			signParams.add(new BasicNameValuePair("out_trade_no", String.valueOf(pd.getAsString("pay_sn"))));   // 商户订单号
			signParams.add(new BasicNameValuePair("spbill_create_ip", "58.250.174.48"));                        // 终端IP
			signParams.add(new BasicNameValuePair("total_fee", totalmoney));                                    // 总金额
			signParams.add(new BasicNameValuePair("trade_type", "JSAPI"));                                        // 交易类型
			String sign = genPackageSign(signParams);                                                           
			signParams.add(new BasicNameValuePair("sign", sign));                                               //签名
			byte[] data = Util.httpPost(param.getValue("wx_url"), toXml(signParams));  //第一次签名认证，wx_url接口链接
			String result = new String(data);
			Map<String, Object> mapdata = XMLParser.getMapFromXML(result);
			if (mapdata != null) {
				if ("SUCCESS".equals(mapdata.get("return_code")) && "SUCCESS".equals(mapdata.get("result_code"))) {
					String prepay_id = (String) mapdata.get("prepay_id");              //微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
					String nonceStr = (String) mapdata.get("nonce_str");               //微信返回的随机字符串
					/*
					 * reParam.put("apiKey",
					 * encodeAndDecode(param.get("wx_key")));
					 * reParam.put("appID",
					 * encodeAndDecode(param.get("wx_appid")));
					 * reParam.put("package", encodeAndDecode("Sign=WXPay"));
					 * reParam.put("partnerId",
					 * encodeAndDecode(param.get("wx_mchid")));
					 * reParam.put("nonceStr", encodeAndDecode(nonceStr));
					 * reParam.put("prepayId", encodeAndDecode(prepay_id));
					 * reParam.put("sign", encodeAndDecode(sign_app));
					 * reParam.put("timestamp",
					 * encodeAndDecode((System.currentTimeMillis()/1000)+""));
					 */
					/**
					 * 二次签名
					 */
					List<NameValuePair> sencondSign = new LinkedList<NameValuePair>();
					sencondSign.add(new BasicNameValuePair("appid", param.getValue("wx_appid")));            //微信开放平台审核通过的应用APPID
					String noncestrS = RandomStringGenerator.getRandomStringByLength(32);
					sencondSign.add(new BasicNameValuePair("noncestr", noncestrS));                           //随机字符串(不需返回)
					sencondSign.add(new BasicNameValuePair("package", "Sign=WXPay"));                         //扩展字段
					sencondSign.add(new BasicNameValuePair("partnerid", param.getValue("wx_mchid")));         //商户号
					sencondSign.add(new BasicNameValuePair("prepayid", prepay_id));                           //微信返回的支付交易会话ID
					sencondSign.add(new BasicNameValuePair("timestamp", (System.currentTimeMillis() / 1000) + ""));   //时间戳
					String signS = genPackageSign(sencondSign);                    //签名
					reParam.put("appID", param.getValue("wx_appid"));
					reParam.put("package11", "prepay_id="+prepay_id);
					//reParam.put("partnerId", param.getValue("wx_mchid"));
					reParam.put("nonceStr", noncestrS);
					//reParam.put("prepayId", prepay_id);
					reParam.put("signType", "MD5");
					reParam.put("paySign", signS);
					reParam.put("timestamp", (System.currentTimeMillis() / 1000) + "");
				} else {
					throw new Exception("支付提醒:" + (String) mapdata.get("return_msg"));
				}
			} else {
				throw new Exception("支付提醒:服务器异常");
			}
		return reParam;
	}
	
	
	/**
	 * 生成定单支付流水号
	 * */
	private String createPaySn() {
		int g=(int)(Math.random()*10);
		int s=(int)(Math.random()*10);
		int b=(int)(Math.random()*10);
		String time = EADate.getOrderSnString();
		String pay_sn = time+g+s+b;                 //通过当前时间年月日时分秒为前缀加上三位自动生成的随机数
		return pay_sn;
	}

	/**
	 * 微信签名
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String genPackageSign(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
			System.out.println("key:" + params.get(i).getName() + "    value:" + params.get(i).getValue());
		}
		sb.append("key=");
		sb.append(Configure.getKey());
		System.out.println(Configure.getKey());
		return MD5.MD5Encode(sb.toString(), "utf-8").toUpperCase();
	}

	/**
	 * 支付宝签名
	 * 
	 * @param array
	 * @return
	 */
	private static String signAllString(String[] array, String praviteKey) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i == (array.length - 1)) {
				sb.append(array[i]);
			} else {
				sb.append(array[i] + "&");
			}
		}
		System.out.println(sb.toString());
		String sign = "";
		try {
			sign = URLEncoder.encode(RSA.sign(sb.toString(), praviteKey, "utf-8"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&sign=\"" + sign + "\"&");
		sb.append("sign_type=\"RSA\"");
		return sb.toString();
	}

	/**
	 * 对字符串字节码进行异或处理
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String encodeAndDecode(String target) throws UnsupportedEncodingException {
		byte[] bs = target.getBytes("iso8859-1");
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte) (bs[i] ^ 0xff);
		}
		return new String(bs, "iso8859-1");
	}

	public static void main(String[] args) throws UnsupportedEncodingException, AlipayApiException {
		PropertiesHelper param = PropertiesFactory.getPropertiesHelper(PropertiesFile.SYS);
		String[] parameters = { 
				"partner=\"" + param.getValue("Ali_PARTNER") + "\"", // 商户号
				"seller_id=\"" + param.getValue("Ali_SELLER") + "\"", // 账户
				"out_trade_no=\"201608171056001\"", // 商户内部订单号
				"subject=\"" + param.getValue("zf_title") + "\"", // 说明
				"body=\"" + param.getValue("zf_title") + "\"", 
				"total_fee=\"" + 0.01 + "\"", // 支付金额（元）
				"notify_url=\"" + param.getValue("Ali_NOTIFY_URL") + "\"", // 通知地址
				"service=\"mobile.securitypay.pay\"", // 固定值
				"payment_type=\"1\"", // 固定值
				"_input_charset=\"utf-8\"", 
				"it_b_pay=\"30m\"" };
		String result = signAllString(parameters, param.getValue("Ali_RSA_PRIVATE"));

		/*partner="2088711470953074"&seller_id="tech@308308.com"&out_trade_no="201608171056001"&subject="移商智能手环支付"&body="移商智能手环支付"&total_fee="0.01"&notify_url="http://liujunbo.tunnel.qydev.com/appPay"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&sign="rLGbvzSZA1xASbe6fHOEhAFlJ%2BQjjd3fZFpyTukEqsHYzM5WLOXfVjJcEhqb4i3Wy9jIa8cauMbcGapG%2Bq5wveFbHNQ8c2fdwep5EN50cFu7HG1w06e9fn%2FU1Vz9h6%2Ftp%2FCsTY0gFDbG75wEsZo8ZnFrquWRbMEO4YxX8qH%2BYcc%3D"&sign_type="RSA"*/
		/*partner="2088421367078075"&seller_id="3402737278@qq.com"&out_trade_no="201608171056001"&subject="移商智能手环支付"&body="移商智能手环支付"&total_fee="0.01"&notify_url="http://liujunbo.tunnel.qydev.com/appPay"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&sign="n%2BVNq0XmoPlzMomX0oMe2SF919vEJ%2BoPP2DLLnVTxYiH66g93QBhiWzNFHO2XkAUX8rtiC6qzPiKQDoO3EJR%2BnlF%2F7AS4sLPPdfIv3guYN%2BCoybaOI9Buz75iRhtRQ3j1pmj%2FX4z%2F0JqRZWzH%2BSeOlSxMWiNSuIAmJElduek6Pg%3D"&sign_type="RSA"*/
		System.out.println("===========================>>result:" + result);
	}

	private String toXml(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		System.out.println("xml<><><><><><><>><><><>" + sb.toString());
		return sb.toString();
	}

	/**
	 * 支付回调
	 */
	public boolean InsertActivityCallBack(PageData pd) throws Exception {
		try {
			//根据流水号获得对应的订单号
			PageData order = ordersPayMapper.selectByPaySnId(pd.getAsString("pay_sn"));
			// 更新预支付订单支付状态，更新支付时间
			pd.put("is_paid", 1);
			ordersPayMapper.update(pd);
			//根据订单号更新订单状态
			order.put("pay_status", 1);
			order.put("shipping_status","1");
			order.put("order_status","1");
			//order.put("pay_type", pd.getAsString("pay_type"));
			orderMapper.update(order);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// 加日志处理
		}
		return false;
	}

		
}