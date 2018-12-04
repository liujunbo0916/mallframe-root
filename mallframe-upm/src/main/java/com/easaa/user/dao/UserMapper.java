package com.easaa.user.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface UserMapper extends EADao {

	public PageData selectByRecCode(String recommend_code);

	/**
	 * 插入用户关联关系
	 * 
	 * @return
	 */
	public int insertRelation(PageData pd);

	public List<PageData> selectChildsByPage(Page parameter);

	/**
	 * 获得用户信息
	 */
	public PageData getUserInfo(PageData pd);
	
	public PageData getUserInfoById(int pd);
	
	public PageData selectByOpenId(String open_id);
	/**
	 * 根据unionid查询用户
	 * @param unionid
	 * @return
	 */
	public PageData selectByUnionId(String unionid);
	
	/**
	 * 根据用户名查询
	 * 
	 */
	public PageData selectByUserName(String userName);
	
	/**
	 * 查询用户登陆状态
	 */
	public PageData selectLoginStatus(PageData pd);

	/**
	 * 插入登陆记录
	 */
	public void insertLoginStatus(PageData pd);

	/**
	 * 查询反馈
	 */
	public List<PageData> selectfeedback(PageData pd);

	/**
	 * 插入反馈
	 */
	public int insertfeedback(PageData pd);
	
	
	/**
	 * 关系查询
	 * 
	 */
	public List<PageData> selectRelationByCondition(PageData pd);
	/**
	 * 后台条件查询所有会员
	 * @param page
	 * @return
	 */
	public List<PageData> selectByAllPage(Page page);
	
}
