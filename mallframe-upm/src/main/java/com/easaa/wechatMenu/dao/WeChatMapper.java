package com.easaa.wechatMenu.dao;
import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
public interface WeChatMapper extends EADao{
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public int insert(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public int delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public int update(PageData pd);
	
	
	/**
	 * 通过Id获取数据
	 * @param pd
	 * @return
	 */
	public PageData findById(PageData pd);
	
	/**
	 * 微信自動回復按關鍵詞查詢回復內容
	 * @param pd
	 * @return
	 */
	public PageData findByKeyWord(PageData pd);
	
	/**
	 * 微信自動回復按關鍵詞查詢回復內容
	 * @param pd
	 * @return
	 */
	public PageData findByEventKey(PageData pd);
	
	/**匹配关键词
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByKw(PageData pd);
	
	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> list(PageData pd);
	
	/**列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS);
	
	/**列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> datalistPage(PageData pd);
	
	
	/**
	 * =======================================================================================================================================
	 * 微信菜单
	 * =======================================================================================================================================
	 */
	public void saveMenu(PageData pd);
	public void deleteMenu(PageData pd);
	public void editMenu(PageData pd);
	public List<PageData> listMenu();
	public List<PageData> listMenu(PageData pd);
	public PageData findMenuById(int menuId);
	public void deleteMenuById(PageData pd);

}