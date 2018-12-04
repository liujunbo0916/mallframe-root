package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface SysConfigMapper extends EADao {
	
	public List<PageData> getByCondition(PageData pd);

	public int updateByCondition(PageData pd);
	/**
	 * app 使用
	 * @param pd
	 * @return
	 */
	public List<PageData> selectVersionList(PageData pd);
	/**
	 * 后台查询使用
	 * @param page
	 * @return
	 */
	public List<PageData> getVersionListByPage(Page page);

	public int insertVersion(PageData pd);

	public int updateVersion(PageData pd);
	
	/**
	 * 短信模板列表 增
	 * @param pd
	 * @return
	 */
	public int insertSmsTpl(PageData pd);
	/**
	 * 短信模板列表 改
	 * @param pd
	 * @return
	 */
	public int updateSmsTpl(PageData pd);
	/**
	 * 短信模板列表 删
	 * @param pd
	 * @return
	 */
	public int deleteSmsTpl(PageData pd);
	/**
	 * 短信模板列表 查
	 * @return
	 */
	public List<PageData> selectAllSmsTpl(PageData pd);
}
