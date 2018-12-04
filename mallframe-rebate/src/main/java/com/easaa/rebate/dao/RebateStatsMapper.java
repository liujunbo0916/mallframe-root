package com.easaa.rebate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.Page;
/**
 * 佣金统计
 * @author 约
 *
 */
import com.easaa.entity.PageData;
public interface RebateStatsMapper extends EADao{
	/**
	 * 查询指定月份的用户贡献统计记录,
	 * @param pd
	 * @return
	 */
	public PageData selectGiveByMap(PageData pd);
	/**
	 * 更新指定月份的用户贡献统计记录,
	 * @param pd
	 * @return
	 */
	public int updateGive(PageData pd);
	/**
	 * 插入指定月份的用户贡献统计记录,
	 * @param pd
	 * @return
	 */
	public int insertGive(PageData pd);
	/**
	 * 查询奉献统计记录
	 * @param pd
	 * @return
	 */
	public List<PageData> selectGiveByPage(Page page);
	/**
	 * 下级分销商列表
	 * @param page
	 * @return
	 */
	public List<PageData> selectClassification(Page page);
	/**
	 * 下级分销商列表（分页）
	 * @param page
	 * @return
	 */
	public List<PageData> selectificationPage(Page page);
	/**
	 * 下级分销商不分页
	 */
	public List<PageData> selectification(PageData pd);
	
	
	
	
	/**
	 * 查询指定月份的用户收益统计记录,
	 * @param pd
	 * @return
	 */
	public PageData selectIncomeByMap(PageData pd);
	
	/**
	 * 更新指定月份的用户收益统计记录,
	 * @param pd
	 * @return
	 */
	public int updateIncome(PageData pd);
	/**
	 * 插入指定月份的用户收益统计记录,
	 * @param pd
	 * @return
	 */
	public int insertIncome(PageData pd);
	/**
	 * 查询收益统计记录
	 * @param pd
	 * @return
	 */
	public List<PageData> selectIncomeByPage(Page page);
	
	/***
	 * 查询所有奉献记录（后台）
	 * @param page
	 * @return
	 */
	public List<PageData> selectGiveBackstageByPage(Page page);
	
	/***
	 * 查询所有返佣收益记录（后台）
	 * @param page
	 * @return
	 */
	public List<PageData> selectIncomeBackstageByPage(Page page);
	
	/**
	 *  查询等级会员的总人数
	 * @param pd
	 * @return
	 * */
	public int selectCountRelation(PageData pd);
	
	
	/**
	 * 查询指定月份的用户贡献统计记录
	 * @param pd
	 * @return
	 */
	public PageData selectGiveSelfByMap(PageData pd);
}
