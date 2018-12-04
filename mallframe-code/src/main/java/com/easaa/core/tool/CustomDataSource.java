package com.easaa.core.tool;

import com.alibaba.druid.pool.DruidDataSource;
import com.easaa.core.util.EADES;

/**
 * 自定义数据库源
 * 对数据库用户名密码加密的密文进行解密
 * @author Administrator
 *
 */
public class CustomDataSource  extends DruidDataSource{

	@Override
	public synchronized void setPassword(String password) {
		super.setPassword(EADES.decryptBasedDes(password));
	}
	@Override
	public synchronized void setUsername(String username) {
		super.setUsername(EADES.decryptBasedDes(username));
	}
	@Override
	public void setUrl(String jdbcUrl) {
		super.setUrl(EADES.decryptBasedDes(jdbcUrl));
	}
	public static void main(String[] args) {
		
		System.out.println("acount-----------》"+EADES.encryptBasedDes("jdbc:mysql://585a205b3fdbd.gz.cdb.myqcloud.com:15237/db_intelligent?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"));
		System.out.println("acount-----------》"+EADES.encryptBasedDes("cdb_outerroot"));
		System.out.println("acount-----------》"+EADES.encryptBasedDes("szmcit2016"));
	    System.out.println(EADES.decryptBasedDes("Swp9G8Df1Ug3N2RQHGfCIUslMMfKPOz51rQ4j9CBfSLKAYEvXVkLfE2P25s8g3yAU9/682hU17Y8yVVXroM1jr2JYHzsA4++P5OFFontgFVySfEeqTuMsUf6S4knrebZRMAEKyVRL0Elo/IOZN06fQRCfvbZb7RKq64ZSzH5SG0APmy4eJhHOA=="));
	   
	}
}
