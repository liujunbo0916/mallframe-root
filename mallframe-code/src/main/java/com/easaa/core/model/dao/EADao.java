package com.easaa.core.model.dao;

import java.util.List;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public interface EADao {
	public List<PageData> selectByPage(Page parameter);
	public List<PageData> selectByMap(Page page);
	public List<PageData> selectByIds(PageData page);
	public PageData selectById(String id);
	public PageData selectById(Integer id);
	public int insert(PageData model);
	public int update(PageData model);
	public int delete(Page page);
	public int getCount(PageData model);
}
