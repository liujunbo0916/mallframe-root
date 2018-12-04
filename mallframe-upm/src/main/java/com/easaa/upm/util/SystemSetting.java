package com.easaa.upm.util;

import java.util.List;
import java.util.Map;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.PageData;
import com.easaa.upm.dao.SysConfigMapper;

import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
public class SystemSetting {

	private static Map<String, String> setting = null;

	private SysConfigMapper sysConfigMapper;

	public SystemSetting(SysConfigMapper sysConfigMapper) {
		this.sysConfigMapper = sysConfigMapper;
		init();
	}

	private void init() {
		if (sysConfigMapper != null) {
			try {
				if (setting == null) {
					PageData pd = new PageData();
					pd.put("CFG_VALUE", "system");
					List<PageData> pds = sysConfigMapper.getByCondition(pd);
					if (EAUtil.isNotEmpty(pds)) {
						pd = pds.get(0);
					}
					String json = pd.getAsString("CFG_VALUE");
					setting = (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(json), Map.class);
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 重新请求数据库赋值
	 */
	public void reset() {
		PageData pd = new PageData();
		pd.put("CFG_VALUE", "system");
		List<PageData> pds = sysConfigMapper.getByCondition(pd);
		if (EAUtil.isNotEmpty(pds)) {
			pd = pds.get(0);
		}
		String json = pd.getAsString("CFG_VALUE");
		setting = (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(json), Map.class);
	}

	public static Map<String, String> getSetting() {
		return setting;
	}

	public static void setSetting(Map<String, String> setting) {
		SystemSetting.setting = setting;
	}

}
