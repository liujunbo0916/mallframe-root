package com.easaa.content.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface ContentMapper  extends EADao{
	
	public List<PageData> selectlistByPage(Page pg);
	public List<PageData> selectAllByPage(Page pg);
	public PageData selectDetails(PageData pg);
	
	public List<PageData> selectHomelist(PageData pg);

	public List<PageData> selMaxLv(PageData pd);
	
	public void deleteImg(PageData pd);
	/**
	 * 查询固定新闻   
	 * limit  限制条数
	 * exclude 排除的新闻
	 * @param pd
	 * @return
	 */
	public List<PageData> selectTopLimit(PageData pd);
	/**
	 * 查询焦点新闻
	 * limit  限制条数
	 * exclude 排除的新闻
	 */
	public List<PageData> selectFoucsLimit(PageData pd);
	
	/**
	 * 根据发布时间查询新闻 排序  先按固定级别降序  再按发布时间降序(不分页)
	 * limit 限制条数
	 * exclude 排除的新闻
	 */
	public List<PageData> selectByPutime(PageData pd);
	
	/**
	 * 根据发布时间查询新闻  先按固定级别降序  再按发布时间降序(分页)
	 * exclude 排除的新闻
	 */
	public List<PageData> selectByPutimePage(Page pd);
	
	/**
	 * 根据用户id和新闻id查询是否点赞
	 */
	public List<PageData> selectThumbsup(PageData pd);
	
	/**
	 * 根据新闻id查询相冊
	 */
	public List<PageData> selectContentAlbums(Integer contentId);
	
	public void insertContentAlbums(PageData pd);
	
	public void updateContentAlbums(PageData pd);
	
	public void deleteContentAlbums(PageData pd);
	public List<PageData> selectHotContent(PageData pd);
	
	public PageData selectPrevOrNext(PageData pd);
	
}
