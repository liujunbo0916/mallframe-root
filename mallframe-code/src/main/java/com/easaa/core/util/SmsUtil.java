package com.easaa.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

//import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.easaa.entity.PageData;
import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;   

/** 
 * 说明：短信接口类
 * 创建人：hsia
 * 修改时间：2013年2月22日
 * @version
 */
public class SmsUtil {
	
	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory
			.getPropertiesHelper(PropertiesFile.PAY);
	
	
	
//	public static void main(String [] args) {
//		
//		HashMap<String, Object> result = null;
//		//初始化SDK
//		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
//		restAPI.init("app.cloopen.com","8883");
//		restAPI.setAccount("8a48b5515350d1e201535f1f802d1887", "853a79dcdb324bbc9ed36ae68ec8b22e");
//		restAPI.setAppId("8aaf070858cd982e0158f097bbbf12b4");
//		result = restAPI.sendTemplateSMS("13631705905","141142",new String[]{"123456","1"});
//		System.out.println("SDKTestGetSubAccounts result=" + result);
//		if("000000".equals(result.get("statusCode"))){
//			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
//			Set<String> keySet = data.keySet();
//			for(String key:keySet){
//				Object object = data.get(key);
//				System.out.println(key +" = "+object);
//			}
//		}else{
//			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
//		}
//	}

	 //短信商 一  http://www.dxton.com/ =====================================================================================
	/**
	 * 给一个人发送单条短信
	 * @param mobile 手机号
	 * @param code  短信内容
	 */
 	public static void sendSms1(String mobile,String code){
	    String account = "", password = "";
	    String strSMS1 = Tools.readTxtFile("admin/config/SMS2.txt");			//读取短信1配置
		if(null != strSMS1 && !"".equals(strSMS1)){
			String strS1[] = strSMS1.split(",fh,");
			if(strS1.length == 2){
				account = strS1[0];
				password = strS1[1];
			}
		}
 		String PostData = "";
		try {
			PostData = "account="+account+"&password="+password+"&mobile="+mobile+"&content="+URLEncoder.encode(code,"utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("短信提交失败");
		}
		 //System.out.println(PostData);
 	     String ret = SMS(PostData, "http://sms.106jiekou.com/utf8/sms.aspx");
 	     System.out.println(ret);
 	   /*  
 	   100			发送成功
 	   101			验证失败
 	   102			手机号码格式不正确
 	   103			会员级别不够
 	   104			内容未审核
 	   105			内容过多
 	   106			账户余额不足
 	   107			Ip受限
 	   108			手机号码发送太频繁，请换号或隔天再发
 	   109			帐号被锁定
 	   110			发送通道不正确
 	   111			当前时间段禁止短信发送
 	   120			系统升级
		*/
	}
	 public static String SMS(String postData, String postUrl) {
	        try {
	            //发送POST请求
	            URL url = new URL(postUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setUseCaches(false);
	            conn.setDoOutput(true);
	            conn.setRequestProperty("Content-Length", "" + postData.length());
	            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	            out.write(postData);
	            out.flush();
	            out.close();
	            //获取响应状态
	            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                System.out.println("connect failed!");
	                return "";
	            }
	            //获取响应内容体
	            String line, result = "";
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	            while ((line = in.readLine()) != null) {
	                result += line + "\n";
	            }
	            in.close();
	            return result;
	        } catch (IOException e) {
	            e.printStackTrace(System.out);
	        }
	        return "";
	    }
	 //===================================================================================================================
	 
	 
	/**
	 * 
	 * 短信商 二  http://www.ihuyi.com/ =====================================================================================
	 * 
	 */
	private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";
	/**
	 * 给一个人发送单条短信
	 * @param mobile 手机号
	 * @param code  短信内容
	 */
	public static void sendSms2(String mobile,String code){
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

	    String content = new String("您的验证码是："+code+"。请不要把验证码泄露给其他人。");  
		NameValuePair[] data = {//提交短信
		    new NameValuePair("account", "cf_34741477"), 
		    new NameValuePair("password", "j34741477"), 			//密码可以使用明文密码或使用32位MD5加密
		    new NameValuePair("mobile", mobile), 
		    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);
		
		try {
			client.executeMethod(method);
			
			String SubmitResult =method.getResponseBodyAsString();
					
			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
			
			if(code == "2"){
				System.out.println("短信提交成功");
			}
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * 给多个人发送单条短信
	 * @param list 手机号验证码
	 */
	public static void sendSmsAll(List<PageData> list){
		String code;
		String mobile;
		for(int i=0;i<list.size();i++){
			code=list.get(i).get("code").toString();
			mobile=list.get(i).get("mobile").toString();
			sendSms2(mobile,code);
		}
	}
	// =================================================================================================
	
	//短信商三  http://sms.56dxw.com/ =====================================================================================
	private static final String do_String = "【移商网】 感谢您使用肇庆手环平台，本次验证码为:";
	private static final String comid = "2798";
	private static final String username = "ZhaoQing";
	private static final String userpwd = "zq123456";
	private static final String smsnumber = "10690";
	
	
	public static String invokeHttpTest(String phone, String code) throws Exception 
	{
		String content = java.net.URLEncoder.encode(do_String + code,"gbk");
		
		URL url = new URL(
		"http://jiekou.56dxw.com/sms/HttpInterface.aspx?comid=" + comid + "&username="+ username +""
		+ "&userpwd="+ userpwd +"&handtel="+ phone +"&sendcontent="+ content +""
		+ "&sendtime="+ "" +"&smsnumber="+ smsnumber + "");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String line = in.readLine();
		int i_ret = httpCon.getResponseCode();
		String sRet = httpCon.getResponseMessage();
		System.out.println(url.toString());	
		return line + "," + i_ret + "," + sRet;
	}
	
	//	返回值 说明 
	//	-1 用户名密码不正确 
	//	-2 内容不能为空 
	//	-3  验证此平台是否存在 
	//	-4 手机号不能为空 
	//	-5 客户短信数量为0 
	//	-21 代表要加签名，签名一般是【网站或公司简称】,比如:您的验证码是:1231231【56短信网】，如果还是返回-21错误，请参考: http://www.56dxw.com/help/1255.html 
	//	-22 非法手机号 
	//	-23 对某个用户名进行了屏蔽处理 
	//	1 代表发送成功 


	// =================================================================================================
	
	
	
	//运营商4   容联 云通讯
	/**
	 * PHONE       电话号码
	 * LIMIT_MIN   几分钟类回复
	 * CODE        验证码
	 * 
	 * SMSTEMPLATE 短信模板
	 * @param param
	 */
//	public static void sendTemplateSMS(PageData param){
//		HashMap<String, Object> result = null;
//		//初始化SDK
//		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
//		//******************************注释*********************************************
//		//*初始化服务器地址和端口                                                       *
//		//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
//		//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
//		//*******************************************************************************
//		restAPI.init(PROPERTIESHELPER.getValue("RLYTX.T_REST_URL"),PROPERTIESHELPER.getValue("RLYTX.T_REST_PORT"));
//		
//		//******************************注释*********************************************
//		//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
//		//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
//		//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
//		//*******************************************************************************
//		restAPI.setAccount(PROPERTIESHELPER.getValue("RLYTX.ACCOUNT_SID"), PROPERTIESHELPER.getValue("RLYTX.AUTH_TOKEN"));
//		//******************************注释*********************************************
//		//*初始化应用ID                                                                 *
//		//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
//		//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
//		//*******************************************************************************
//		restAPI.setAppId(PROPERTIESHELPER.getValue("RLYTX.APPID"));
//		//******************************注释****************************************************************
//		//*调用发送模板短信的接口发送短信                                                                  *
//		//*参数顺序说明：                                                                                  *
//		//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
//		//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
//		//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
//		//*第三个参数是要替换的内容数组。																														       *
//		//**************************************************************************************************
//		//**************************************举例说明***********************************************************************
//		//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
//		//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
//		//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
//		//*********************************************************************************************************************
//		result = restAPI.sendTemplateSMS(param.getAsString("PHONE"),StringUtils.isNotEmpty(param.getAsString("SMSTEMPLATE"))?param.getAsString("SMSTEMPLATE"):"141142",new String[]{param.getAsString("CODE"),
//				"3分钟"});
//		System.out.println("SDKTestGetSubAccounts result=" + result);
//		if("000000".equals(result.get("statusCode"))){
//			//正常返回输出data包体信息（map）
//			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
//			Set<String> keySet = data.keySet();
//			for(String key:keySet){
//				Object object = data.get(key);
//				System.out.println(key +" = "+object);
//			}
//		}else{
//			//异常返回输出错误码和错误信息
//			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
//		}
//	}
	
}

