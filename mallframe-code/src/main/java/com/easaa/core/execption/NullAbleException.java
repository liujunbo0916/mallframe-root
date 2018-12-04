package com.easaa.core.execption;

import com.easaa.core.util.EAConst;


/**
 * 非空异常校验�?br>
 * 
 * @author BLRISE
 * @since 2013-07-15
 */
public class NullAbleException extends   RuntimeException {

	private String nullField;

	/**
	 * 构造函数1
	 * 
	 * @param 非空校验查
	 */
	public NullAbleException() {
		super(EAConst.Exception_Head + "对象不能为空,请检查");
	}

	/**
	 * 构造函数2
	 * 
	 * @param 非空校验查
	 */
	public NullAbleException(Class cs) {
		super(EAConst.Exception_Head + "对象不能为空,请检查[" + cs + "]");
	}

	/**
	 * 构造函数3
	 * 
	 * @param pNullField  异常附加信息
	 */
	public NullAbleException(String pNullField) {
		super(EAConst.Exception_Head + "对象属性[" + pNullField + "]不能为空,请检查");
		this.setNullField(pNullField);
	}

	public String getNullField() {
		return nullField;
	}

	public void setNullField(String nullField) {
		this.nullField = nullField;
	}
}
