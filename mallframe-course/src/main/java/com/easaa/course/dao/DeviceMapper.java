package com.easaa.course.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface DeviceMapper extends EADao {

	public List<PageData> selectapplyByPage(Page parameter);

	public List<PageData> selectDeviceListByPage(Page parameter);

	public List<PageData> selectapplyByMap(Page page);

	public List<PageData> selectApplyListByPage(Page page);
	
	public List<PageData> selectVenueDeviceList(PageData pd);
	
	public PageData selectapplyById(Integer id);

	public int insertapply(PageData model);

	public int updateapply(PageData model);
	
	public PageData selectByDeviceSN(Integer model);

	public int deleteapply(Page page);
}
