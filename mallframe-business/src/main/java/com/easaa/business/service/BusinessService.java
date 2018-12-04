package com.easaa.business.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.BusinessClassMapper;
import com.easaa.business.dao.BusinessMapper;
import com.easaa.business.dao.SellerMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.FtpUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class BusinessService  extends EABaseService{

	@Autowired
	private BusinessMapper businessMapper;
	@Autowired
	private BusinessClassMapper businessClassMapper;
	
	@Override
	public EADao getDao() {
		return businessMapper;
	}
	/**
	 * 新增店铺
	 * @param pd
	 */
	public void addBusiness(PageData pd) {
		if(StringUtils.isEmpty(pd.getAsString("bs_seller_name"))){
			pd.put("bs_seller_name", pd.getAsString("user_name"));
		}
		pd.put("bs_create_time", EADate.getCurrentTime());
		pd.put("bs_open_time", EADate.getCurrentTime());
		pd.put("bs_parentid",0);
		pd.put("bs_level","0");
		pd.put("bs_grade_id","1");
		pd.put("bs_state","1");
		businessMapper.insert(pd);
	}
	
	/**
	 * 修改店铺
	 * @param pd
	 */
	public void updateBusiness(PageData pd) {
		if(EAUtil.isNotEmpty(pd.getAsString("bs_state")) && pd.getAsString("bs_state").equals("1")){
			pd.put("bs_closed_info", "");
		}
		businessMapper.update(pd);
	}
	
	/**
	 * 申请列表
	 */
	public List<PageData> stayList(Page page){
		return businessMapper.selectStayPage(page);
	}
	/**
	 * 店铺信息
	 */
	public PageData selectStaydata(PageData pd){
		return businessMapper.selectStaydata(pd);
	}
	
	public int updateBusinessJoin(PageData pd){
		return businessMapper.updateBusinessJoin(pd);
	}
	
	public int deleteBusinessJoin(PageData pd){
		return businessMapper.deleteBusinessJoin(pd);
	}
	
	public int insertBusinessJoin(PageData pd){
		return businessMapper.insertBusinessJoin(pd);
	}
	
	public List<PageData> selectBusinessJoinByMap(PageData pd){
		return businessMapper.selectBusinessJoinByMap(pd);
	}
	
	public List<PageData> selectBusinessJoinByPage(PageData pd){
		Page page = new Page();
		page.setPd(pd);
		return businessMapper.selectBusinessJoinByPage(page);
	}
	
	/**
	 * 添加商家店铺资格(APP使用)
	 */
	@SuppressWarnings("restriction")
	public void insertSellerJoin(PageData pd) throws Exception{
		byte[] bt = null;
		if(pd.getAsString("user_id").isEmpty()){
			throw new Exception("用户ID不能为空");
		}
		if(pd.getAsString("company_name").isEmpty()){
			throw new Exception("公司名称不能为空");
		}
		if(pd.getAsString("company_province_id").isEmpty()){
			throw new Exception("公司所在地省ID不能为空");
		}
		if(pd.getAsString("company_province_name").isEmpty()){
			throw new Exception("公司所在地省名称");
		}
		if(pd.getAsString("company_area_id").isEmpty()){
			throw new Exception("公司所在区域ID不能为空");
		}
		if(pd.getAsString("company_area_name").isEmpty()){
			throw new Exception("公司所在区域名称不能为空");
		}
		if(pd.getAsString("company_address").isEmpty()){
			throw new Exception("公司详细地址不能为空");
		}
		if(pd.getAsString("contacts_name").isEmpty()){
			throw new Exception("公司法人姓名不能为空");
		}
		if(pd.getAsString("contacts_phone").isEmpty()){
			throw new Exception("法人电话不能为空");
		}
		if(pd.getAsString("contacts_email").isEmpty()){
			throw new Exception("法人邮箱不能为空");
		}
		if(pd.getAsString("business_licence_number").isEmpty()){
			throw new Exception("营业执照号不能为空");
		}
		if(pd.getAsString("business_licence_address").isEmpty()){
			throw new Exception("营业执所在地不能为空");
		}
		if(pd.getAsString("business_licence_start").isEmpty()){
			throw new Exception("营业执照有效期开始时间不能为空");
		}
		if(pd.getAsString("business_licence_end").isEmpty()){
			throw new Exception("营业执照有效期结束时间不能为空");
		}
		if(pd.getAsString("business_sphere").isEmpty()){
			throw new Exception("法定经营范围不能为空");
		}
		if(pd.getAsString("business_licence_number_electronic").isEmpty()){
			throw new Exception("营业执照电子版图片不能为空");
		}
		if(pd.getAsString("bank_account_name").isEmpty()){
			throw new Exception("银行开户名不能为空");
		}
		if(pd.getAsString("bank_account_number").isEmpty()){
			throw new Exception("公司银行账号不能为空");
		}
		if(pd.getAsString("bank_name").isEmpty()){
			throw new Exception("开户银行支行名称不能为空");
		}
		if(pd.getAsString("bank_address").isEmpty()){
			throw new Exception("开户银行所在地不能为空");
		}
		if(pd.getAsString("join_year").isEmpty()){
			throw new Exception("开店时长不能为空");
		}
		if(pd.getAsString("sc_id").isEmpty()){
			throw new Exception("店铺分类ID不能为空");
		}
		if(pd.getAsString("bs_bail").isEmpty()){
			throw new Exception("店铺分类保证金不能为空");
		}
		if(pd.getAsString("bs_charge_name").isEmpty()){
			throw new Exception("店铺负责人不能为空");
		}
		if(pd.getAsString("bs_charge_phone").isEmpty()){
			throw new Exception("店铺负责人电话不能为空");
		}
		if(pd.getAsString("bs_charge_cardno").isEmpty()){
			throw new Exception("店铺负责人身份证号码不能为空");
		}
		if(pd.getAsString("bs_charge_handheld_cardphoto").isEmpty()){
			throw new Exception("店铺负责人手持身份证照不能为空");
		}
		if(pd.getAsString("bs_charge_front_cardphoto").isEmpty()){
			throw new Exception("店铺负责人正面身份证照不能为空");
		}
		if(pd.getAsString("bs_charge_negative_cardphoto").isEmpty()){
			throw new Exception("店铺负责人反面身份证照不能为空");
		}
		//校验开店金额是否一致
		PageData bsclass=businessClassMapper.selectById(pd.getAsInt("sc_id"));
		if(EAUtil.isEmpty(bsclass)){
			throw new Exception("平台店铺分类有误！");
		}
		BigDecimal payMoney=BigDecimal.valueOf(0);
		payMoney=bsclass.getAsBigDecimal("sc_bail").multiply(pd.getAsBigDecimal("join_year"));
		if(payMoney.compareTo(pd.getAsBigDecimal("bs_bail"))!=0){
			throw new Exception("平台店铺分类保证金异常！");
		}
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			bt = decoder.decodeBuffer(pd.getAsString("business_licence_number_electronic"));
			String blnesavePath = FtpUtil.upload(bt, "mallframe", "seller", EADate.getCurrentTimeAsNumber() + "1.jpg");
			pd.put("business_licence_number_electronic", blnesavePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		try {
			bt = decoder.decodeBuffer(pd.getAsString("bs_charge_handheld_cardphoto"));
			String savePath = FtpUtil.upload(bt, "mallframe", "seller", EADate.getCurrentTimeAsNumber() + "2.jpg");
			pd.put("bs_charge_handheld_cardphoto", savePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		try {
			bt = decoder.decodeBuffer(pd.getAsString("bs_charge_front_cardphoto"));
			String savePath = FtpUtil.upload(bt, "mallframe", "seller", EADate.getCurrentTimeAsNumber() + "3.jpg");
			pd.put("bs_charge_front_cardphoto", savePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		try {
			bt = decoder.decodeBuffer(pd.getAsString("bs_charge_negative_cardphoto"));
			String savePath = FtpUtil.upload(bt, "mallframe", "seller", EADate.getCurrentTimeAsNumber() + "4.jpg");
			pd.put("bs_charge_negative_cardphoto", savePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		pd.put("join_state", "0");
		pd.put("store_sg_id", "1");
		pd.put("create_time", EADate.getCurrentTime());
		pd.put("bsg_name", "系统默认");
		businessMapper.insertBusinessJoin(pd);
	}
}
