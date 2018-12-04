package com.easaa.wechatMenu.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.wechatMenu.dao.WechatMenuMapper;
@Service
public class WechatMenuService extends EABaseService{
	@Autowired
	private WechatMenuMapper wechatMenuMapper;
	
	@Override
	public EADao getDao(){
		return wechatMenuMapper;
	}
	
	/**
	 * 根菜单
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listRootMenu()throws Exception{
		Page page=new Page();
		PageData pd=new PageData();
		pd.put("parent_id", "0");
		page.setPd(pd);
		return wechatMenuMapper.selectByMap(page);
	}
	/**
	 * 
	 * @param parent_id
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listSubMenu(int parent_id)throws Exception{
		Page page=new Page();
		PageData pd=new PageData();
		pd.put("parent_id", parent_id);
		page.setPd(pd);
		return wechatMenuMapper.selectByMap(page);
	}
	
	/**
	 * 删除段位制项目目录小分类
	 * @param dto
	 * @return 
	 */
	public int delete(String id){
		return wechatMenuMapper.delete(id);
	}
	
	public int getCount(PageData pd){
		return wechatMenuMapper.getCount(pd);
	}
	
}