package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.upm.entity.LogisticsMin;
import com.easaa.upm.entity.Province;

public interface LogisticsMapper extends EADao{
	
	public List<PageData> getprovince(PageData pd);
	
	public int insertArea(PageData pd);
	
	public List<PageData> selectArea(Page pg);
	
	public List<PageData> selectProvince (PageData pd);
	
	public List<PageData> selectCity (PageData pd);
	
	public int deleteAreaById(PageData pd);
	
	public int updateAreaById(PageData pd);
	
	public int insertContnuheavy(PageData pd);
	
	public List<PageData> selectContnuheavy(Page pg);
	
	public int deleteContnuheavyById(PageData pd);
	
	public int updateContnuheavyById(PageData pd);
	
	public int deleteContnuheavyAll(int pd);
	
	public List<PageData> selectlogsticsByProvince(PageData pd);
	
	public List<PageData> selectContnuByWeightDesc(PageData pd);

	
	/**
	 * 商家模板操作
	 * 
	 * @param pd
	 * @return
	 */
	
	public List<LogisticsMin> selectBusiTpl(PageData pd);
	
	public List<Province> selectProvinceAndCity(PageData pd);
	
	
	public void insertAreaTpl(PageData pd);
	
	public void insertAreaFee(PageData pd);

	public void updateAreaTpl(PageData tplData);

	public void updateAreaFee(PageData storePd);
	
	/**
	 * 级联删除模板
	 */
	public void deleteTplCascade(PageData pd);
	
	
	public void deleteTplFee(PageData pd);
	
	
	public PageData selectUseFee(PageData pd);
	
	
	public PageData selectDefaultFee(PageData pd);
	
	
	
	
}
