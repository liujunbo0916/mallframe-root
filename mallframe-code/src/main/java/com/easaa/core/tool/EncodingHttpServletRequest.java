package com.easaa.core.tool;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodingHttpServletRequest extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	private String encoding;
	private boolean isNotEncode = true;

	public EncodingHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	public EncodingHttpServletRequest(HttpServletRequest request, String encoding) {
		super(request);
		this.request = request;
		this.encoding = encoding;

	}
/*
 * 服务器是tomcat的时候把注释代码打开
 * 
 * 
 */
	public String getParameter(String name) {
		String value = request.getParameter(name);
		if (null != value) {
			try {
				// tomcat默认以ISO8859-1处理GET传来的参数。把tomcat上的值用ISO8859-1获取字节流，再转换成UTF-8字符串
				value = new String(value.getBytes("ISO8859-1"), encoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	@Override
	public Map getParameterMap() {
		try {
			Map<String, String[]> map = request.getParameterMap();
			if (isNotEncode) {
				Collection<String[]> coll = map.values();
				for (String[] vs : coll) {
					for (int i = 0; i < vs.length; i++) {
						//vs[i] = new String(vs[i].getBytes("iso8859-1"), encoding);
						// vs[i] = new String(vs[i].getBytes("GB2312"),
						// encoding);
						// vs[i] = new String(vs[i].getBytes("GBK"), encoding);
					}
				}
				isNotEncode = false;
			}
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
//		return super.getParameterMap();
	}

}
