package com.easaa.entity;

/**
 * 快递公司枚举类
 * 
 * @author liujunbo
 *
 */
public enum Express {
	/**
	 * 顺丰 SF 百世快递 HTKY 中通 ZTO 申通 STO 圆通 YTO 韵达 YD 邮政平邮 YZPY EMS EMS 天天 HHTT 京东
	 * JD 全峰 QFKD 国通 GTO 优速 UC 德邦 DBL 快捷 FAST 亚马逊 AMAZON 宅急送 ZJS
	 * 
	 */
	SHUNFENG("顺丰", "SF"), YUANTONG("圆通", "YTO"), ZHONGTONG("中通", "ZTO"), EMS("EMS", "EMS"), SHENTONG("申通", "STO"),GUOTONG("国通", "GTO"),
	RUNDA("韵达", "YD"), TIANTIAN("天天", "HHTT"), DEBANG("德邦物流", "DBL"), BAISHIKUAIDI("百世快递", "HTKY"),YOUZHENG("邮政平邮", "YZPY"),
	YAMAXUN("亚马逊", "AMAZON"), YOUSU("优速", "UC"), KUAIJIE("快捷", "FAST"), QUANFENG("全峰", "QFKD"), ZHAIJISONG("宅急送", "ZJS");
	
	private String desc;
	private String code;

	private Express(String desc, String code) {
		this.desc = desc;
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
