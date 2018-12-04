package com.easaa.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 说明：短信接口类 创建人：hisa Q371855779 修改时间：2016年3月15日
 * 
 * @version
 */
public class EASMS {

	public static void main(String[] args) {
	        sendSms2("13631705905", "尊敬的13631705905，您于2017-08-25日提交的退票申请已被驳回（驳回理由：改票不能退）。详情请咨询人工客户，退票单号012546。");
	}

	// 短信商三 http://sms.56dxw.com/
	// =====================================================================================
	private static final String do_String = "感谢您使用筝筝云联平台，本次验证码为:";
	private static final String comid = "2798";
	private static final String username = "ZhaoQing";
	private static final String userpwd = "zq123456";
	private static final String smsnumber = "10690";

	public static String invokeHttpTest(String phone, String code) throws Exception {
		String content = java.net.URLEncoder.encode(do_String + code, "gbk");

		URL url = new URL("http://jiekou.56dxw.com/sms/HttpInterface.aspx?comid=" + comid + "&username=" + username + ""
				+ "&userpwd=" + userpwd + "&handtel=" + phone + "&sendcontent=" + content + "" + "&sendtime=" + ""
				+ "&smsnumber=" + smsnumber + "");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String line = in.readLine();
		int i_ret = httpCon.getResponseCode();
		String sRet = httpCon.getResponseMessage();
		System.out.println(url.toString());
		// return line + "," + i_ret + "," + sRet;
		return line + "," + i_ret + "," + sRet;
	}

	// 返回值 说明
	// -1 用户名密码不正确
	// -2 内容不能为空
	// -3 验证此平台是否存在
	// -4 手机号不能为空
	// -5 客户短信数量为0
	// -21 代表要加签名，签名一般是【网站或公司简称】,比如:您的验证码是:1231231【56短信网】，如果还是返回-21错误，请参考:
	// http://www.56dxw.com/help/1255.html
	// -22 非法手机号
	// -23 对某个用户名进行了屏蔽处理
	// 1 代表发送成功

	/**
	 * 
	 * 短信商 二 http://www.ihuyi.com/
	 * =========================================================================
	 * ============
	 * 
	 */
	private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";

	/**
	 * 发送验证码短信 codetype // 0 注册 1绑定银行卡 2修改密码 3修改支付密码 4其他验证码
	 * 
	 * @param mobile
	 *            手机号
	 * @param code
	 *            短信内容
	 */
	public static String sendSms2(String mobile, String code, String codetype) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		String content = "";
		if (codetype != null && codetype.equals("0")) {
			// 验证码模板 ：您的【变量】验证码是：【变量】。请不要把验证码泄露给其他人。如非本人操作，可不用理会！
			content = new String("您的注册安全码是：" + code + "。请不要把安全码泄露给其他人。如非本人操作，可不用理会！");
		} else if (codetype != null && codetype.equals("1")) {
			content = new String("您的绑定银行卡安全码是：" + code + "。请不要把安全码泄露给其他人。如非本人操作，可不用理会！");
		} else if (codetype != null && codetype.equals("2")) {
			content = new String("您的修改密码安全码是：" + code + "。请不要把安全码泄露给其他人。如非本人操作，可不用理会！");
		} else if (codetype != null && codetype.equals("3")) {
			content = new String("您的修改支付密码安全码是：" + code + "。请不要把安全码泄露给其他人。如非本人操作，可不用理会！");
		}else if (codetype != null && codetype.equals("4")) {
				content = new String("您的安全码是：" + code + "。请不要把安全码泄露给其他人。如非本人操作，可不用理会！");
		}
		NameValuePair[] data = { // 提交短信
				new NameValuePair("account", "C10738758"),
				new NameValuePair("password", "d242c65c4e646c4ce0008fcbd2633a4b"), // 密码可以使用明文密码或使用32位MD5加密
				new NameValuePair("mobile", mobile), new NameValuePair("content", content), };
		method.setRequestBody(data);
		try {
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
			if (code == "2") {
				System.out.println("短信提交成功");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 发送自定义消息短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public static String sendSms2(String mobile, String content) {
		String code="";
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		NameValuePair[] data = { // 提交短信
				new NameValuePair("account", "C39466760"),
				new NameValuePair("password", "94dd933880186d5d86cfb22f0b63b91f"), // 密码可以使用明文密码或使用32位MD5加密
				new NameValuePair("mobile", mobile), new NameValuePair("content", content)};
		method.setRequestBody(data);
		try {
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
			if (code == "2") {
				System.out.println("短信提交成功");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return code;
	}
}
