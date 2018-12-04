package com.easaa.user.dao;
import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
public interface CircleMapper extends EADao{
	//圈子
	public int insertCircle(PageData pd);
	public List<PageData> selectCircleByPage(Page pg);
	public List<PageData> selectCircleByUidPage(Page pg);
	public int updateCircle(PageData pd);
	public int deleteCircle(PageData pd);
	public PageData selectCircleById(int id);
	//评论回复
	public int insertCirclecomment(PageData pd);
	public List<PageData> selectCirclecommentByPage(Page pg);
	public List<PageData> selectCirclecommentByMap(PageData pg);
	public List<PageData> selectCirclecommentMyPage(Page pg);
	public int deleteCirclecomment(PageData pd);//删除评论
	public int deleteCommentback(PageData pd);//删除回复
	//点赞记录
	public PageData selectThumbsupById(PageData pd);
	public int insertThumbsup(PageData pd);
	
	public int updateCirclecomment(PageData pd);
	
}