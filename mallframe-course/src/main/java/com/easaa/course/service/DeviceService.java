package com.easaa.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.course.dao.CourseHourMapper;
import com.easaa.course.dao.DeviceMapper;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class DeviceService extends EABaseService {

	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private CourseHourMapper courseHourMapper;

	@Override
	public EADao getDao() {
		return deviceMapper;
	}

	/***
	 * 查询设备列表
	 * 
	 * @return
	 */
	public List<PageData> selectDevicelists(Page page) throws Exception {
		return deviceMapper.selectDeviceListByPage(page);
	}

	/***
	 * 查询单个设备编号
	 * 
	 * @return
	 */
	public PageData selectByDeviceSN(Integer pd) throws Exception {
		return deviceMapper.selectByDeviceSN(pd);
	}

	/***
	 * 查询设备列表
	 * 
	 * @return
	 */
	public List<PageData> selectVenueDeviceList(PageData pd) throws Exception {
		return deviceMapper.selectVenueDeviceList(pd);
	}

	/***
	 * 查询设备申请列表
	 * 
	 * @return
	 */
	public List<PageData> selectApplylists(Page page) throws Exception {
		return deviceMapper.selectApplyListByPage(page);
	}

	/***
	 * 查询设备发货单byid
	 * 
	 * @return
	 */
	public PageData getApplyById(Integer id) throws Exception {
		return deviceMapper.selectapplyById(id);
	}

	/***
	 * 插入设备发货byid
	 * 
	 * @return
	 */
	public boolean insertApply(PageData model) throws Exception {
		PageData applypd = new PageData();
		if (EAUtil.isEmpty(model.getAsString("appply_dv_ids"))) {
			throw new Exception("请选择设备！");
		}
		applypd.put("apply_sn", System.currentTimeMillis() + EAString.getRandomString(4));
		applypd.put("device_category_id", model.getAsString("device_category_id"));
		if(EAUtil.isNotEmpty(model.getAsString("venue_id"))){
			applypd.put("venue_id", model.getAsString("venue_id"));
		}
		applypd.put("apply_num", model.getAsString("appply_dv_ids").split(",").length);
		applypd.put("apply_time", EADate.getCurrentTime());
		applypd.put("apply_status", "0");
		applypd.put("get_address", model.getAsString("get_address"));
		applypd.put("send_address", model.getAsString("send_address"));
		applypd.put("course_id", model.getAsString("course_id"));
		applypd.put("deploy_type", model.getAsString("deploy_type"));
		applypd.put("appply_dv_ids", model.getAsString("appply_dv_ids"));
		applypd.put("hour_id", model.getAsString("hour_id"));
		applypd.put("get_phone", model.getAsString("get_phone"));
		applypd.put("send_phone", model.getAsString("send_phone"));
		applypd.put("send_venue_id", model.getAsString("send_venue_id"));
		applypd.put("admin_note", model.getAsString("admin_note"));
		// 修改课时调配状态
		PageData hourdata = courseHourMapper.selectById(model.getAsInt("hour_id"));
		if (EAUtil.isEmpty(hourdata)) {
			throw new Exception("课程时间为空");
		}
		Integer num = hourdata.getAsInt("hour_equip_number") - applypd.getAsInt("apply_num");
		if (num <= 0) {
			hourdata.put("hour_equip_number", "0");
			hourdata.put("hour_status", "1");
		} else {
			hourdata.put("hour_equip_number", String.valueOf(num));
		}
		// 修改设备状态
		String str[] = model.getAsString("appply_dv_ids").split(",");
		for (String string : str) {
			PageData dvdata = deviceMapper.selectById(Integer.valueOf(string));
			if (EAUtil.isNotEmpty(dvdata)) {
				dvdata.put("dv_status", "3");
				if (deviceMapper.update(dvdata) <= 0) {
					throw new Exception("调配时修改设备情况出错");
				}
			}
		}
		if (deviceMapper.insertapply(applypd) > 0 && courseHourMapper.update(hourdata) > 0) {
			return true;
		} else {
			throw new Exception("插入设备发货单出错");
		}
	}

	/***
	 * 修改发货单(后台)
	 * 
	 * @return
	 */
	public boolean updateApply(PageData model) throws Exception {
		if (EAUtil.isEmpty(model.getAsString("apply_id"))) {
			throw new Exception("修改发货单时apply_id不存在");
		}
		PageData applydata = deviceMapper.selectapplyById(model.getAsInt("apply_id"));
		if (EAUtil.isEmpty(applydata)) {
			throw new Exception("修改发货单时apply_id出错");
		}
		// 审核失败
		if (model.getAsInt("apply_status") == 1) {
			PageData hourdata = courseHourMapper.selectById(applydata.getAsInt("hour_id"));
			Integer num = hourdata.getAsInt("hour_equip_number") + applydata.getAsInt("apply_num");
			hourdata.put("hour_equip_number", num);
			hourdata.put("hour_status", "0");
			if (courseHourMapper.update(hourdata) <= 0) {
				throw new Exception("审核失败修改课程调配情况出错");
			}
			String str[] = applydata.getAsString("appply_dv_ids").split(",");
			for (String string : str) {
				PageData dvdata = deviceMapper.selectById(Integer.valueOf(string));
				if (EAUtil.isNotEmpty(dvdata)) {
					dvdata.put("dv_status", "0");
					if (deviceMapper.update(dvdata) <= 0) {
						throw new Exception("审核失败修改设备情况出错");
					}
				}
			}
		}
		// 送达
		if (model.getAsInt("apply_status") == 4) {
			String str[] = applydata.getAsString("appply_dv_ids").split(",");
			for (String string : str) {
				PageData dvdata = deviceMapper.selectById(Integer.valueOf(string));
				if (EAUtil.isNotEmpty(dvdata)) {
					dvdata.put("dv_venue_id", applydata.getAsString("venue_id"));
					dvdata.put("dv_status", "0");
					dvdata.put("dv_time", EADate.getCurrentTime());
					if (deviceMapper.update(dvdata) <= 0) {
						throw new Exception("送达时修改课程设备情况出错");
					}
				}
			}
		}
		if (deviceMapper.updateapply(model) > 0) {
			return true;
		} else {
			throw new Exception("修改设备发货单出错");
		}
	}

	/***
	 * 修改发货单(后台定时器使用)
	 * 
	 * @return
	 */
	public int editApply(PageData model) {
		return deviceMapper.updateapply(model);
	}

	/***
	 * 修改发货单(APP)
	 * @return
	 */
	public boolean updateApplyAPP(PageData model) throws Exception {
		if (EAUtil.isEmpty(model.getAsString("user_id"))) {
			throw new Exception("用户ID不能为空");
		}
		if (EAUtil.isEmpty(model.getAsString("apply_id"))) {
			throw new Exception("设备发货单ID不能为空");
		}
		PageData applydata = deviceMapper.selectapplyById(model.getAsInt("apply_id"));
		if (EAUtil.isEmpty(applydata) || !applydata.getAsString("user_id").equals(model.getAsString("user_id"))
				&& !applydata.getAsString("apply_status").equals("3")) {
			throw new Exception("该设备发货单有误，请联系管理员！");
		}
		model.put("apply_status", "4");
		// 送达
		if (model.getAsInt("apply_status") == 4) {
			String str[] = applydata.getAsString("appply_dv_ids").split(",");
			for (String string : str) {
				PageData dvdata = deviceMapper.selectById(Integer.valueOf(string));
				if (EAUtil.isNotEmpty(dvdata)) {
					dvdata.put("dv_venue_id", applydata.getAsString("send_venue_id"));
					dvdata.put("dv_status", "0");
					dvdata.put("dv_time", EADate.getCurrentTime());
					if (deviceMapper.update(dvdata) <= 0) {
						throw new Exception("送达时修改课程设备情况出错");
					}
				}
			}
		}
		model.put("send_time", EADate.getCurrentTime());
		if (deviceMapper.updateapply(model) > 0) {
			return true;
		} else {
			throw new Exception("发货单出错");
		}
	}
}
