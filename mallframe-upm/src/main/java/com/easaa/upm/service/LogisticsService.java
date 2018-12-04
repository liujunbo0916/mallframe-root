package com.easaa.upm.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.upm.dao.LogisticsMapper;
import com.easaa.upm.entity.Logistics;
import com.easaa.upm.entity.Logistics.Contnuheavy;
import com.easaa.upm.entity.Logistics.PClist;
import com.easaa.upm.entity.Logistics.ProCity;
import com.easaa.upm.entity.LogisticsFee;
import com.easaa.upm.entity.LogisticsMin;
import com.easaa.upm.entity.Province;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class LogisticsService extends EABaseService {
	@Autowired
	private LogisticsMapper logisticsMapper;

	
	private List<Province> provinceAndCity;
	
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return logisticsMapper;
	}

	/**
	 * 获取省
	 */
	public List<PageData> getProvince(PageData pd) {
		try {
			return logisticsMapper.getprovince(pd);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取市区 province_id
	 */
	public List<PageData> getCityArea(PageData pd) {
		try {
			return logisticsMapper.selectCity(pd);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取区域列表byid
	 */
	public List<PageData> selectAreaByID(PageData pd) {
		try {
			pd.put("area_id", pd.getAsString("area_id"));
			Page pg= new Page();
			pg.setPd(pd);
			return logisticsMapper.selectArea(pg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取区域列表（全部）
	 */
	public List<PageData> selectArea(PageData pd) {
		try {
			Page pg= new Page();
			pg.setPd(pd);
			return logisticsMapper.selectArea(pg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}	
	/**
	 * 保存区域设置
	 */
	public boolean setArea(PageData pd) throws Exception {
		pd.put("create_time", EADate.getCurrentTime());
		pd.remove("area_id");
		if (logisticsMapper.insertArea(pd) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除区域设置
	 */
	public boolean delArea(PageData pd) throws Exception {
		pd.put("area_id", pd.getAsString("area_id"));
		if (logisticsMapper.deleteAreaById(pd) > 0) {
			Page pg= new Page();
			List<PageData>	clist=logisticsMapper.selectContnuheavy(pg);
			if (clist!=null && clist.size()>0) {
				if (logisticsMapper.deleteContnuheavyAll(pd.getAsInt("area_id"))>0) {
					return true;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 修改区域设置
	 */
	public boolean updateArea(PageData pd) throws Exception {
		pd.put("create_time", EADate.getCurrentTime());
		if (logisticsMapper.updateAreaById(pd) > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 查询区域物流列表
	 */
	public List<Logistics> selectArea() throws Exception {
		PageData pd = new PageData();
		Page pg= new Page();
		pg.setPd(pd);
		// 区域列表
		List<Logistics> arealist = new ArrayList<Logistics>();
		List<PageData> list = logisticsMapper.selectArea(pg);

		for (PageData data : list) {
			// 省市级列表
			List<PClist> pclist = new ArrayList<PClist>();
			pd.put("province_id", data.getAsString("province_id"));
			List<PageData> plist = logisticsMapper.selectProvince(pd);
			for (int i = 0; i < plist.size(); i++) {
				// 城市
				List<ProCity> c_list = new ArrayList<ProCity>();
				String cNmae = plist.get(i).getAsString("region_name") + " : ";
				pd.put("province_id", plist.get(i).getAsString("region_id"));
				List<PageData> clist = logisticsMapper.selectCity(pd);

				for (int j = 0; j < clist.size(); j++) {
					c_list.add(new ProCity(clist.get(j).getAsString("region_id"), clist.get(j).getAsString("parent_id"),
							clist.get(j).getAsString("region_name")));
					cNmae += clist.get(j).getAsString("region_name") + " , ";
				}

				pclist.add(new PClist(plist.get(i).getAsString("region_id"), plist.get(i).getAsString("region_name"),
						c_list, cNmae.substring(0, cNmae.length() - 3)));
			}
			
			//续重列表
			List<Contnuheavy> chlist = new ArrayList<Contnuheavy>();
			Page chpg= new Page();
			PageData chpd= new PageData();
			chpd.put("area_id", data.getAsString("area_id"));
			chpg.setPd(chpd);
			List<PageData> listch = logisticsMapper.selectContnuheavy(chpg);
			for (int i = 0; i < listch.size(); i++) {
				chlist.add(new Contnuheavy(listch.get(i).getAsString("contnu_id"), listch.get(i).getAsString("contnu_name"), listch.get(i).getAsString("contnu_kg"), listch.get(i).getAsString("contnu_price")));
			}
			listch.clear();
			arealist.add(new Logistics(data.getAsString("area_id"), data.getAsString("area_name"),
					data.getAsString("first_kg"), data.getAsString("first_price"),
					data.getAsString("send_time"),
					data.getAsString("create_time"), pclist,chlist));
		}
		return arealist;
	}

	
	/**
	 * 保存续重
	 */
	public boolean addcontnuheavy(PageData pd) throws Exception {
		pd.put("contnu_name", pd.getAsString("contnu_kg")+"KG以上");
		pd.put("create_time", EADate.getCurrentTime());
		pd.remove("contnu_id");
		if (logisticsMapper.insertContnuheavy(pd) > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 获取续重列表（全部）
	 */
	public List<PageData> selectContnuheavy(PageData pd) {
		try {
			Page pg= new Page();
			pg.setPd(pd);
			return logisticsMapper.selectContnuheavy(pg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取续重列表（BYID）
	 */
	public List<PageData> selectContnuheavyById(PageData pd) {
		try {
			pd.put("contnu_id", pd.getAsString("contnu_id"));
			Page pg= new Page();
			pg.setPd(pd);
			return logisticsMapper.selectContnuheavy(pg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除续重
	 */
	public boolean deleteContnuheavy(PageData pd) throws Exception {
		pd.put("area_id", pd.getAsString("area_id"));
		if (logisticsMapper.deleteContnuheavyById(pd) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 修改续重
	 */
	public boolean updateContnuheavy(PageData pd) throws Exception {
		pd.put("contnu_name", pd.getAsString("contnu_kg")+"KG以上 ");
		pd.put("create_time", EADate.getCurrentTime());
		if (logisticsMapper.updateContnuheavyById(pd) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 
	 * 获取省市地区
	 */
	public List<Province> getProvinceAndCity(PageData pd){
		provinceAndCity = logisticsMapper.selectProvinceAndCity(pd);
		return provinceAndCity;
	}
	
	/**
	 * 
	 * 查询商家 运费模板列表
	 * 
	 */
	public List<LogisticsMin> getTplByBusi(PageData pd){
		return logisticsMapper.selectBusiTpl(pd);
	}
	/**
	 * 保存商家运费模板
	 * {paramStr={"ftpl_name":"模板1","bs_id":"",
	 * "feeList":[{"first_num":"1","first_fee":"5","follow_num":"1","follow_fee":"1","areasname_list":"全国","areasid_list":"-1"},{"first_num":"1","first_fee":"7","follow_num":"1","follow_fee":"1","areasname_list":"北京,天津,河北省,山西省","areasid_list":"37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60"}]}}
	 * @param pd
	 */
	public void saveOrUpdateAreaTpl(PageData pd) {
		String paramStr = pd.getAsString("paramStr");
		JSONObject jsonObject = JSONObject.fromObject(paramStr);
		//保存模板
		
		
			PageData tplData = new PageData();
			tplData.put("ftpl_name", jsonObject.get("ftpl_name"));
			tplData.put("create_time", EADate.getCurrentTime());
			tplData.put("bs_id", pd.getAsString("bs_id"));
			
			if(StringUtils.isEmpty(jsonObject.getString("ftpl_id"))){
				logisticsMapper.insertAreaTpl(tplData);
			}else{
				tplData.put("tpl_id", jsonObject.getString("ftpl_id"));
				logisticsMapper.updateAreaTpl(tplData);
			}
			JSONArray jsonArray = jsonObject.getJSONArray("feeList");
			for(int i = 0 ; i < jsonArray.size() ; i++){
				JSONObject storeObj = jsonArray.getJSONObject(i);
				PageData storePd = new PageData();
				storePd.put("tpl_id", tplData.getAsString("tpl_id"));
				storePd.put("first_num", storeObj.get("first_num"));
				storePd.put("first_fee", storeObj.get("first_fee"));
				storePd.put("follow_fee", storeObj.get("follow_fee"));
				storePd.put("follow_num", storeObj.get("follow_num"));
				storePd.put("areasname_list", storeObj.get("areasname_list"));
				storePd.put("areasid_list", storeObj.get("areasid_list"));
				if("-1".equalsIgnoreCase(storeObj.getString("areasid_list"))){
					storePd.put("is_default", 1);
				}else{
					storePd.put("is_default", 0);
				}
				if(StringUtils.isEmpty(storeObj.getString("fee_id"))){
					logisticsMapper.insertAreaFee(storePd);
				}else{
					storePd.put("fee_id", storeObj.getString("fee_id"));
					logisticsMapper.updateAreaFee(storePd);
				}
				
			}
	}

	/**
	 * 复制运费模板
	 * tpl_id 模板id
	 * @param pd
	 */
	public void copyTpl(PageData pd) {
		List<LogisticsMin> logistics = getTplByBusi(pd);
		LogisticsMin logisticsEntity = logistics.get(0);
		PageData tplPd = new PageData();
		tplPd.put("ftpl_name", logisticsEntity.getLogisticsName()+"的复制");
		tplPd.put("create_time", EADate.getCurrentTime());
		tplPd.put("bs_id", logisticsEntity.getBsId());
		logisticsMapper.insertAreaTpl(tplPd);
		for(LogisticsFee fee : logisticsEntity.getLogisticsFees()){
			PageData feePd = new PageData();
			feePd.put("tpl_id", tplPd.getAsString("tpl_id"));
			feePd.put("first_num", fee.getFeeFirstNum());
			feePd.put("first_fee", fee.getFirstFee());
			feePd.put("follow_num", fee.getFollowNum());
			feePd.put("follow_fee", fee.getFollowFee());
			feePd.put("areasname_list", fee.getAreaNames());
			feePd.put("areasid_list", fee.getAreaIds());
			feePd.put("is_default", fee.getDefaultArea());
			logisticsMapper.insertAreaFee(feePd);
		}
	}
	/**
	 * 级联删除模板
	 * 
	 */
	public void deleteTplCascade(PageData pd){
		logisticsMapper.deleteTplCascade(pd);
	}

	public void deleteFee(PageData pd) {
		logisticsMapper.deleteTplFee(pd);
	}
	
	
	
	
	
	
	
	
}
