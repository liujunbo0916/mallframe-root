package com.easaa.hautecouture.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EATools;
import com.easaa.entity.OrderStatus;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.hautecouture.dao.ApplicationMapper;
import com.easaa.hautecouture.dao.CoutureOrderLogMapper;
import com.easaa.hautecouture.dao.CoutureOrderMapper;
@Service
public class CoutureOrderService extends EABaseService{
	@Autowired
	private CoutureOrderMapper coutureOrderMapper;
	@Autowired
	private ApplicationMapper applicationMapper;
	@Autowired
	private CoutureOrderLogMapper coutureOrderLogMapper;
	
	@Override
	public EADao getDao() {
		return coutureOrderMapper;
	}
	
	public List<PageData> getByPage(Page parameter) {
		return coutureOrderMapper.selectByPage(parameter);
	}
	
	/**
	 * 新增订单
	 * @throws Exception 
	 * */
	public int addOrder(PageData pd) throws Exception{
		String order_sn = EATools.getOrderSN();
		pd.put("order_sn", order_sn);
		pd.put("aplc_id", pd.getString("aplc_id"));
		pd.put("user_id", pd.getString("applicant"));
		pd.put("shipping_money", "0");
		pd.put("design_fee", "0");
		pd.put("shipping_fee", "0");
		pd.put("order_status", "未确认");
		pd.put("shipping_status", "未配货");
		pd.put("shipping_type", "顺丰");
		pd.put("pay_status", "未支付");
		pd.put("pay_type", "微信");
		pd.put("create_date", EADate.getCurrentTime());
		pd.put("create_by","");
		pd.put("order_money", "0");
		pd.put("received_money","0");
		pd.put("remarks","");
		return coutureOrderMapper.insert(pd);
	}
	
	public int edits(PageData model) {
		String logType = model.get("logType").toString();
		if("pay".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","未配货");
			model.put("order_status","已确认");
		}
		if("nopay".equals(logType)){
			model.put("pay_status",  "未支付");
			model.put("shipping_status","未配货");
			model.put("order_status","未确认");
		}
		if("goCargo".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","已发货");
			model.put("order_status","配送中");
		}
		if("withCargo".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","已配货");
			model.put("order_status","已确认");
		}
		if("growWithCargo".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","未发货");
			model.put("order_status","等待配送");
		}
		if("noCargo".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","未发货");
			model.put("order_status","等待配送");
		}
		if("closeCargo".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","已签收");
			model.put("order_status","已收货");
		}
		if("returnCargo".equals(logType)){
			model.put("pay_status",  "支付完成");
			model.put("shipping_status","已签收");
			model.put("order_status","已退货");
			model.put("log_note","已经设为已收货");
		}
		if("cancelPay".equals(logType)){
			model.put("pay_status",  "未支付");
			model.put("shipping_status","未配货");
			model.put("order_status","未确认");
			model.put("log_note","已经取消该订单了");
		}
		if("invalidPay".equals(logType)){
			model.put("pay_status",  "未支付");
			model.put("shipping_status","未配货");
			model.put("order_status","未确认");
		}
		return getDao().update(model);
	}
	
	/**
	 * 查询历史订单列表
	 * */
	public List<PageData> selectHistory(Page pd){
		return coutureOrderMapper.selectByMap(pd);
	}
	
	/**
	 * 查询订单详情
	 * */
	public List<PageData> selectDetails(PageData pd){
		return coutureOrderMapper.selectDetails(pd);
	}
	
	/**
	 * 取消订单
	 * */
	public int cancelOrder(PageData pd){
		pd.put("order_status", "未确认");
		return coutureOrderMapper.update(pd);
	}
	
	/**
	 * 签收订单
	 * */
	public int signOrder(PageData pd){
		pd.put("order_status", "已签收");
		return coutureOrderMapper.update(pd);
	}

	public int deleteBytype(PageData pd) throws Exception{
		String delType = pd.getString("delType");
		if("0".equals(delType)){
			return coutureOrderMapper.deleteByPhysics(pd);
		}else if("1".equals(delType)){
			pd.put("del_flag", "1");
			return coutureOrderMapper.deleteByLogic(pd);
		}
		return 0;
	}

	public int editByMode(PageData pd) throws Exception{
		String id = pd.getString("id");
		String mode = pd.getString("mode");
		Boolean bool = StringUtils.isNotBlank(pd.getString("id")) ? true : false;
		PageData oldData = coutureOrderMapper.selectById(EAString.stringToInt(pd.get("id").toString(), 0));
		int i = 0;
		if(StringUtils.isNotBlank(id)){//修改
			pd.put("update_by", "");
			pd.put("update_date", EADate.getCurrentTime());
			i = coutureOrderMapper.update(pd);
		}else{//新增
			pd.put("create_by", "");
			pd.put("create_date", EADate.getCurrentTime());
			pd.put("order_sn", EATools.getOrderSN());
			pd.put("order_status", OrderStatus.TJDD.getValue());
			coutureOrderMapper.insert(pd);
			i = pd.getAsInt("id");
			if(i>=1){
				PageData APLC = new PageData();
				APLC.put("id", pd.get("aplc_id"));
				APLC.put("update_by", pd.get(""));
				APLC.put("update_date", EADate.getCurrentTime());
				APLC.put("auditor", pd.get(""));
				APLC.put("reviewed","0");
				APLC.put("audit_time", EADate.getCurrentTime());
				applicationMapper.audit(APLC);
			}
		}
		
		PageData newData = coutureOrderMapper.selectById(EAString.stringToInt(pd.get("id").toString(), 0));
		if(i>=1){
			pd.put("auto_note", pd.getString("auto_note"));
			pd.put("creator", "");
			pd.put("create_time", EADate.getCurrentTime());
			StringBuffer sb = new StringBuffer();
			if("costInfoEdit".equals(mode)){//
				if(bool){//修改订单费用
					if(newData.getAsBigDecimal("process_fee").compareTo(oldData.getAsBigDecimal("process_fee"))!=0){
						sb.append("修改制作费为:"+newData.getAsBigDecimal("process_fee"));
					}else if(newData.getAsBigDecimal("design_fee").compareTo(oldData.getAsBigDecimal("design_fee")) !=0){
						sb.append("修改设计费为:"+newData.getAsBigDecimal("design_fee"));
					}else if(newData.getAsBigDecimal("shipping_money").compareTo(oldData.getAsBigDecimal("shipping_money"))!=0){
						sb.append("配送费用(收取客户的费用)为:"+newData.getAsBigDecimal("shipping_money"));
					}else if(newData.getAsBigDecimal("shipping_fee").compareTo(oldData.getAsBigDecimal("shipping_fee"))!=0){
						sb.append("修改配送费(公司支出)为:"+newData.getAsBigDecimal("shipping_fee"));
					}else if(newData.getAsBigDecimal("pay_fee").compareTo(oldData.getAsBigDecimal("pay_fee"))!=0){
						sb.append("修改支付手续费为"+newData.getAsBigDecimal("pay_fee"));
					}else if(newData.getAsBigDecimal("mer_invoice").compareTo(oldData.getAsBigDecimal("mer_invoice"))!=0){
						sb.append("修改公司发票费用为"+newData.getAsBigDecimal("mer_invoice"));
					}
					pd.put("order_id", pd.getString("id"));
					pd.put("log_note", sb.toString());
				}else{//确认订单费用,提交订单
					pd.put("order_id", i);
					pd.put("log_note", "生成订单");
				}
				coutureOrderLogMapper.insert(pd);
			}else if("personInfoEdit".equals(mode)){//
				if(bool){//修改配送信息
					if(newData.getString("address_id").equals(oldData.getString("address_id"))){
						PageData address = coutureOrderMapper.selectAddrById(newData.getAsInt("address_id"));
						sb.append(address.getString("province")).
						append("   ").
						append("city").
						append("   ").
						append("area").
						append("   ").
						append("address").
						append("   ").
						append("zip").
						append("   ").
						append("contact_name").
						append("   ").
						append("contact_phone");
						
						pd.put("log_note", "变更收货地址信息："+sb.toString());
						pd.put("order_status", "修改收货信息");
						coutureOrderLogMapper.insert(pd);
					}
				}
			}
		}
		return i;
	}

	public void updateStatus(PageData pd) throws Exception{
		pd.put("update_by", "");
		pd.put("update_date", EADate.getCurrentTime());
		int i = coutureOrderMapper.update(pd);
		if(i>=1){
			pd.put("order_id", pd.getString("id"));
			pd.put("creator", "");
			pd.put("create_time", EADate.getCurrentTime());
			pd.put("log_note", "修改订单状态："+pd.getString("order_status"));
			coutureOrderLogMapper.insert(pd);
		}
	}

	public PageData getByOsn(String order_sn) throws Exception {
		return coutureOrderMapper.selectByOsn(order_sn);
	}

	public List<PageData> selectOrderList(Page page)throws Exception {
		return coutureOrderMapper.selectOrderList(page);
	}
	
}