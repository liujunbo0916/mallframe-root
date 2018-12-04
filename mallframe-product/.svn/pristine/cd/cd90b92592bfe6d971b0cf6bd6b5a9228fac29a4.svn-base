package com.easaa.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.CouponMapper;

@Service
public class CouponService extends EABaseService {
	@Autowired
	private CouponMapper couponMapper;

	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return couponMapper;
	}

	/**
	 * 用户领取优惠劵
	 * 
	 * @param pd
	 * @return
	 */
	public boolean insertCashRecord(PageData pd) throws Exception {
		List<PageData> recordlist = new ArrayList<PageData>();
		if (EAUtil.isEmpty(pd.getAsString("cash_id"))) {
			throw new Exception("cash_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		PageData coupon = couponMapper.selectById(pd.getAsInt("cash_id"));
		// 可用的优惠劵才能领取
		if (EAUtil.isNotEmpty(coupon) && coupon.getAsInt("cash_status") == 1) {

			recordlist = couponMapper.selectrecord(pd);
			if (EAUtil.isNotEmpty(recordlist) && recordlist.size() > 0) {
				throw new Exception("已经领取过了");
			}
			pd.put("user_status", "0");
			couponMapper.insertrecord(pd);
			// 判断优惠劵是否抢光了
			PageData rpd = new PageData();
			rpd.put("cash_id", pd.getAsString("cash_id"));
			recordlist = couponMapper.selectrecord(rpd);
			if ((recordlist.size()) == coupon.getAsInt("cash_grant_num")) {
				rpd.remove("cash_id");
				rpd.put("id", pd.getAsString("cash_id"));
				rpd.put("cash_status", "2");
				couponMapper.update(rpd);
			}
			return true;
		} else {
			throw new Exception("优惠劵不存在或者已抢光！");
		}
	}

	/**
	 * app 优惠劵列表
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> selectCouponList(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("currentPage不能为空");
		}
		PageData data= new PageData();
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(pd.getAsInt("currentPage"));
		page.setShowCount(15);
		page.setPd(pd);
		if (EAUtil.isNotEmpty(pd.getAsString("user_status"))) {
			return couponMapper.selectAppByPage(page);
		} else {
			List<PageData> list=couponMapper.selectByPage(page);
			for (PageData pageData : list) {
				data.put("user_id", pd.getAsString("user_id"));
				data.put("cash_id", pageData.getAsString("id"));
				List<PageData> recordlist=couponMapper.selectrecord(data);
				if(recordlist!=null && recordlist.size()>0){
					pageData.put("is_Record", "1");
				}else{
					pageData.put("is_Record", "0");
				}
			}
			return list;
		}
	}
	
	/**
	 * 查询自己的优惠券列表所有的
	 * @param pd
	 * @return
	 */
	public List<PageData> selectAllOwer(PageData pd){
		if(StringUtils.isNotEmpty(pd.getAsString("user_id"))){
			return couponMapper.selectAllOwer(pd);
		}else{
			return null;
		}
	}
	
	
	

	
	public List<PageData> selectrecord(PageData pd){
		return couponMapper.selectrecord(pd);
	}
	
	public int editrecord(PageData pd){
		return couponMapper.updaterecord(pd);
	}
	
	public List<PageData> selectAppByPage(Page page){
		return couponMapper.selectAppByPage(page);
	}
}
