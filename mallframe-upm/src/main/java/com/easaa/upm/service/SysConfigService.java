package com.easaa.upm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.upm.dao.SysConfigMapper;
import com.easaa.upm.util.SystemSetting;

import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
@Service
public class SysConfigService extends EABaseService {

	
	
	private List<PageData> smsTplList;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Autowired
	private SystemSetting systemSetting;
	
	@Override
	public EADao getDao() {
		return null;
	}
	
	/**
	 * 查询所有的配置信息封装成对象
	 */
	public Map<String, String> getSysConfig(PageData pd) {
		if (StringUtils.isEmpty(pd.getAsString("CFG_GROUP"))) {// 默认获取系统配置
			pd.put("CFG_GROUP", "system");
		}
		List<PageData> config = sysConfigMapper.getByCondition(pd);
		if (EAUtil.isNotEmpty(config)) {
			pd = config.get(0);
		}
		String json = pd.getAsString("CFG_VALUE");
		Map<String, String> configMap = (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(json), Map.class);
		return configMap;
	}

	/**
	 * 保存更改的设置
	 */
	public void updateSys(PageData pd, HttpServletRequest request) {
		JSONObject json = JSONObject.fromObject(pd);
		pd.put("CFG_VALUE", json.toString());
		if (StringUtils.isEmpty(pd.getAsString("CFG_GROUP"))) {
			pd.put("CFG_GROUP", "system"); // 默认更新系统配置
		}
		sysConfigMapper.updateByCondition(pd);
		/**
		 * 系统重新加载配置之后的配置表
		 */
		systemSetting.reset();
	}

	/**
	 * app使用
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> selectVersionList(PageData pd) {
		return sysConfigMapper.selectVersionList(pd);
	}

	/**
	 * 后台查询使用
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getVersionList(Page pd) {
		return sysConfigMapper.getVersionListByPage(pd);
	}

	public int insertVersion(PageData pd) {
		return sysConfigMapper.insertVersion(pd);
	}

	public int updateVersion(PageData pd) {
		return sysConfigMapper.updateVersion(pd);
	}

	public PageData selectOneVersion(PageData pd) {
		Page page= new Page();
		page.setPd(pd);
		List<PageData> list=sysConfigMapper.getVersionListByPage(page);
		if(EAUtil.isNotEmpty(list) && list.size()==1){
			return list.get(0);
		}
		return null;
	}

	public PageData getSmsTplByCode(String code){
		PageData resultPd = new PageData();
		if(smsTplList == null){
			smsTplList = sysConfigMapper.selectAllSmsTpl(resultPd);
		}
		for(PageData tmp:smsTplList){
			if(code.equals(tmp.getAsString("tpl_var"))){
				resultPd = tmp;
			}
		}
		return resultPd;
	}
	
	public List<PageData> selectAllSmsTpl(PageData pd) {
		return sysConfigMapper.selectAllSmsTpl(pd);
	}
	public int insertSmsTpl(PageData pd) {
		return sysConfigMapper.insertSmsTpl(pd);
	}
	public int updateSmsTpl(PageData pd) {
		return sysConfigMapper.updateSmsTpl(pd);
	}
	public int deleteSmsTpl(PageData pd) {
		return sysConfigMapper.deleteSmsTpl(pd);
	}
}
