package com.easaa.hautecouture.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.entity.PageData;
import com.easaa.hautecouture.dao.CoutureOrderLogMapper;
@Service
public class CoutureOrderLogService extends EABaseService{
	@Autowired
	private CoutureOrderLogMapper coutureOrderLogMapper;
	@Override
	public EADao getDao(){
		return coutureOrderLogMapper;
	}
	public List<PageData> selectall(){
		return coutureOrderLogMapper.selectall();
	}
	
	public int create(PageData model,PageData pd) {
		PageData setpd = new PageData();
		String logType = null;
		if(model.get("log_note")!=null){
			setpd.put("log_note", model.getAsString("log_note"));
		}
		if(model.getAsString("logType")!=null){
			logType = model.getAsString("logType") ;
		}
		if("basicInfoEdit".equals(logType)){
			setpd.put("log_note","修改了赠送积分数量");
		}
		setpd.put("create_time",EADate.getCurrentTime());
		setpd.put("creator", model.getAsString("creator"));
		setpd.put("pay_status",  model.getAsString("pay_status"));
		setpd.put("shipping_status",model.getAsString("shipping_status"));
		setpd.put("order_status", model.getAsString("order_status"));
		setpd.put("order_id",model.getAsString("id"));
		if(model.get("auto_note")!=null)
			setpd.put("auto_note",model.getAsString("auto_note"));
		return getDao().insert(setpd);
	}
	/**
	 *  修改费用信息详细日志记录（model：是未修改前费用信息
	 *  											pd：是修改后的费用信息）
	 * */
	public  void cosInfoEdit(PageData model,PageData pd) {
		List<String> log_note= new ArrayList<String>();
		if(!model.getAsString("pay_by_points").equals(pd.getAsString("pay_by_points"))){
			log_note.add("修改积分支付金额");
		}
		if(!model.getAsString("pay_by_money").equals(pd.getAsString("pay_by_money"))){
			log_note.add("修改货币支付金额");
		}
		if(!model.getAsString("shipping_fee").equals(pd.getAsString("shipping_fee"))){
			log_note.add("修改公司配送费用");
		}
		if(!model.getAsString("shipping_money") .equals(pd.getAsString("shipping_money"))){
			log_note.add("修改客户配送费用");
		}
		if(!model.getAsString("mer_invoice") .equals(pd.getAsString("mer_invoice"))){
			log_note.add("修改公司发票费用");
		}
		if(!model.getAsString("goods_money") .equals(pd.getAsString("goods_money"))){
			log_note.add("修改商品总额");
		}
		if(!model.getAsString("pay_fee") .equals(pd.getAsString("pay_fee"))){
			log_note.add("修改支付手续费");
		}
		if(log_note.isEmpty()){
			log_note.add("未做任何修改，进行了一次访问");
		}
		for(String log:log_note){
			PageData setpd = new PageData();
			setpd.put("create_time",EADate.getCurrentTime());
			setpd.put("creator", pd.getAsString("creator"));
			setpd.put("pay_status",  pd.getAsString("pay_status"));
			setpd.put("shipping_status",pd.getAsString("shipping_status"));
			setpd.put("order_status", pd.getAsString("order_status"));
			setpd.put("order_id",pd.getAsString("id"));
			setpd.put("auto_note",pd.getAsString("auto_note"));
			setpd.put("log_note",log);
			getDao().insert(setpd);
		}
	}
	
	/**
	 *  修改收货人信息详细日志记录（model：是未修改前费用信息
	 *  											pd：是修改后的费用信息）
	 * */
	public  void personInfoEdit(PageData model,PageData pd) {
		List<String> log_note= new ArrayList<String>();
		if(!model.getAsString("contact_name").equals(pd.getAsString("contact_name"))){
			log_note.add("修改收货人名字");
		}
		if(!model.getAsString("contact_phone").equals(pd.getAsString("contact_phone"))){
			log_note.add("修改收货人手机号码");
		}
		if(!model.getAsString("user_note").equals(pd.getAsString("user_note"))){
			log_note.add("修改用户备注信息");
		}
		if(!model.getAsString("province") .equals(pd.getAsString("province"))){
			log_note.add("修改收货人省份信息");
		}
		if(!model.getAsString("city") .equals(pd.getAsString("city") )){
			log_note.add("修改收货人城市信息");
		}
		if(!model.getAsString("city") .equals(pd.getAsString("city") )){
			log_note.add("修改区/县信息");
		}
		if(!model.getAsString("address") .equals(pd.getAsString("address") )){
			log_note.add("修改收货人地址信息");
		}
		if(log_note.isEmpty()){
			log_note.add("未做任何修改，进行了一次访问");
		}
		for(String log:log_note){
			PageData setpd = new PageData();
			setpd.put("create_time",EADate.getCurrentTime());
			setpd.put("creator", pd.getAsString("creator"));
			setpd.put("pay_status",  pd.getAsString("pay_status"));
			setpd.put("shipping_status",pd.getAsString("shipping_status"));
			setpd.put("order_status", pd.getAsString("order_status"));
			setpd.put("order_id",pd.getAsString("id"));
			setpd.put("auto_note",pd.getAsString("auto_note"));
			setpd.put("log_note",log);
			getDao().insert(setpd);
		}
	}
	
	/**
	 * 修改订单状态，使订单状态实现交互时记录日志
	 * （model 订单详情）
	 * */
	public  int creates(PageData model) {
		PageData pd = new PageData();
		String logType = model.getAsString("logType") ;
		if("pay".equals(logType)){
			pd.put("log_note","将订单设为已付款");
		}
		if("nopay".equals(logType)){
			pd.put("log_note","将订单设为未付款");
		}
		if("goCargo".equals(logType)){
			pd.put("log_note","已经设为已经发货");
		}
		if("withCargo".equals(logType)){
			pd.put("log_note","已经设为配货");
		}
		if("growWithCargo".equals(logType)){
			pd.put("log_note","发货单已经生成");
		}
		if("noCargo".equals(logType)){
			pd.put("log_note","已经设为未发货");
		}
		if("closeCargo".equals(logType)){
			pd.put("log_note","已经设为已收货");
		}
		if("returnCargo".equals(logType)){
			pd.put("log_note","已经设为已收货");
		}
		if("cancelPay".equals(logType)){
			pd.put("log_note","已经取消该订单了");
		}
		if("invalidPay".equals(logType)){
			pd.put("log_note","该订单已经被设置为无效订单");
		}
		if("afterSale".equals(logType)){
			pd.put("log_note","售后添加");
		}
		pd.put("create_time",EADate.getCurrentTime());
		pd.put("creator", model.getAsString("creator"));
		pd.put("pay_status",  model.getAsString("pay_status"));
		pd.put("shipping_status",model.getAsString("shipping_status"));
		pd.put("order_status", model.getAsString("order_status"));
		pd.put("order_id",model.getAsString("id"));
		if(model.get("auto_note")!=null)
		pd.put("auto_note",model.getAsString("auto_note"));
		return getDao().insert(pd);
	}
	public int insert(PageData pd) {
		return coutureOrderLogMapper.insert(pd);
	}
	public List<PageData> selectByOrdId(String order_id) {
		return coutureOrderLogMapper.selectByOrdId(order_id);
	}
	
}