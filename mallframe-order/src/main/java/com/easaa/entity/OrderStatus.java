package com.easaa.entity;

public enum OrderStatus{
	
	/**
	 * TJDD. 提交订单
	 */
	TJDD("ea001"),
	/**
	 * DDCL. 等待材料
	 */
	DDCL("ea002"),
	/**
	 * QRCL. 确认材料
	 */
	QRCL("ea003"),
	/**
	 * DDSJF. 等待支付(设计费)
	 */
	DDSJF("ea004"),
	/**
	 * QRSJF. 已支付(设计费)
	 */
	QRSJF("ea005"),
	/**
	 * SJGZZZ. 设计稿制作中
	 */
	SJGZZZ("ea006"),
	/**
	 * QRSJG. 已确认设计稿 
	 */
	QRSJG("ea007"),
	/**
	 * DDZZF. 等待支付(制作费头款)
	 */
	DDZZF("ea008"),
	/**
	 * QRZZFTK. 已支付(制作费头款)
	 */
	QRZZFTK("ea009"),
	/**
	 * CPZZZ. 成品制作中
	 */
	CPZZZ("ea010"),
	/**
	 * ZZWC. 成品制作完成
	 */
	ZZWC("ea011"),
	/**
	 * DDZZFWK. 等待支付(制作费尾款)
	 */
	DDZZFWK("ea012"),
	/**
	 * QRZZFWK. 已支付(制作费尾款)
	 */
	QRZZFWK("ea013"),
	/**
	 * DDPS. 等待配送
	 */
	DDPS("ea014"),
	/**
	 * YFH. 已发货
	 */
	YFH("ea015"),
	/**
	 * YHQS. 验货签收
	 */
	YHQS("ea016"),
	/**
	 * DDWC. 订单完成
	 */
	DDWC("ea017"),
	/**
	 * YQX. 已取消
	 */
	YQX("ea018");
	
	
	/**
	 * 订单状态枚取
	 */
	private String value = "";

	/**
	 * Constructor.
	 * 
	 * @param value
	 */
	private OrderStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
