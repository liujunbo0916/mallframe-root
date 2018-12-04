package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface IndexMapper   extends EADao{

	public Integer schoolCount();
	public Integer teacherCount();
	public Integer studentCount();
	public Integer custodianCount();
	public List<PageData> findOwerMsg(PageData pd);
	public List<PageData> findActivity(PageData pd);
	public List<PageData> findTurnoverSchool(PageData pd);
	public List<PageData> findAttendanceClass(PageData pd);
	public List<PageData> findAlarmHistory(PageData pd);
	
}
