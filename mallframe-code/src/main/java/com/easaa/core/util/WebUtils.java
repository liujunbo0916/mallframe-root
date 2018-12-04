package com.easaa.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.easaa.entity.PageData;


/**
 * 和Web层相关的实用工具类
 * 
 * @author 熊春
 * @since 2008-09-22
 */
public class WebUtils {
	/**
	 * 获取一个Session属性对象
	 * 
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public static Object getSessionAttribute(HttpServletRequest request,
			String sessionKey) {
		Object objSessionAttribute = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			objSessionAttribute = session.getAttribute(sessionKey);
		}
		return objSessionAttribute;
	}

	/**
	 * 设置一个Session属性对象
	 * 
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public static void setSessionAttribute(HttpServletRequest request,
			String sessionKey, Object objSessionAttribute) {
		HttpSession session = request.getSession();
		if (session != null)
			session.setAttribute(sessionKey, objSessionAttribute);
	}

	/**
	 * 移除Session对象属性值
	 * 
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public static void removeSessionAttribute(HttpServletRequest request,
			String sessionKey) {
		HttpSession session = request.getSession();
		if (session != null)
			session.removeAttribute(sessionKey);
	}

	/**
	 * 将请求参数封装为Dto
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static PageData getParamAsPageData(HttpServletRequest request) {
		PageData dto = new PageData();
		Map map = request.getParameterMap();
		Iterator keyIterator = (Iterator) map.keySet().iterator();
		try {
			while (keyIterator.hasNext()) {
				String key = (String) keyIterator.next();
				String value = ((String[]) (map.get(key)))[0];
				/** 对加密过的值进行解密
				if (key.startsWith("zc_")) {
					if (StringUtils.isNotEmpty(value)) {
						value = SecurityHelper.decrypt(value);
					}
				}**/
				dto.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	/**
	 * 将map转换成url
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}
	
	
	/**
	 * 将map转换成url
	 * @param map
	 * @return
	 */
	public static String formatUrlByParamsMap(String url,Map<String, Object> map) {
		if (EAString.isNullStr(url)||EAUtil.isEmpty(map)) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
		}
		String[] arrSplit=url.split("[?]");
		if(arrSplit.length>1){
			return url+"&"+s;
		}else{
			return url+"?"+s;
		}
	}
	/**
	 * 获取代码对照值
	 * 
	 * @param field
	 *            代码类别
	 * @param code
	 *            代码值
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getCodeDesc(String pField, String pCode,
			HttpServletRequest request) {
		List codeList = (List) request.getSession().getServletContext()
				.getAttribute("EACODELIST");
		String codedesc = null;
		for (int i = 0; i < codeList.size(); i++) {
			PageData codeDto = (PageData) codeList.get(i);
			if (pField.equalsIgnoreCase(codeDto.getAsString("group_code"))
					&& pCode.equalsIgnoreCase(codeDto.getAsString("code")))
				codedesc = codeDto.getAsString("value");
		}
		return codedesc;
	}

	/**
	 * 根据代码类别获取代码表列表
	 * 
	 * @param codeType
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getCodeListByField(String pField,
			HttpServletRequest request) {
		List codeList = (List) request.getSession().getServletContext()
				.getAttribute("EACODELIST");
		List lst = new ArrayList();
		for (int i = 0; i < codeList.size(); i++) {
			PageData codeDto = (PageData) codeList.get(i);
			if (codeDto.getAsString("group_code").equalsIgnoreCase(pField)) {
				lst.add(codeDto);
			}
		}
		return lst;
	}

	/**
	 * 获取全局参数值
	 * 
	 * @param pParamKey
	 *            参数键名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getParamValue(String pParamKey,
			HttpServletRequest request) {
		String paramValue = "";
		ServletContext context = request.getSession().getServletContext();
		if (EAUtil.isEmpty(context)) {
			return "";
		}
		List paramList = (List) context.getAttribute("EAPARAMLIST");
		for (int i = 0; i < paramList.size(); i++) {
			PageData paramDto = (PageData) paramList.get(i);
			if (pParamKey.equals(paramDto.getAsString("paramkey"))) {
				paramValue = paramDto.getAsString("paramvalue");
			}
		}
		return paramValue;
	}

	/**
	 * 获取全局参数
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List getParamList(HttpServletRequest request) {
		ServletContext context = request.getSession().getServletContext();
		if (EAUtil.isEmpty(context)) {
			return new ArrayList();
		}
		return (List) context.getAttribute("EAPARAMLIST");
	}

	/**
	 * 获取指定Cookie的值
	 * 
	 * @param cookies
	 *            cookie集合
	 * @param cookieName
	 *            cookie名字
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static String getCookieValue(Cookie[] cookies, String cookieName,
			String defaultValue) {
		if (cookies == null) {
			return defaultValue;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return (cookie.getValue());
		}
		return defaultValue;
	}
   
	
	public static  String getIpAddr(HttpServletRequest request)  {  
	      String ip  =  request.getHeader( " x-forwarded-for " );  
	       if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
	          ip  =  request.getHeader( " Proxy-Client-IP " );  
	      }   
	       if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
	          ip  =  request.getHeader( " WL-Proxy-Client-IP " );  
	      }   
	       if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
	         ip  =  request.getRemoteAddr();  
	     }   
	      return  ip;  
	 }  
}
