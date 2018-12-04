package com.easaa.rebate.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.order.dao.OrderMapper;
import com.easaa.order.service.OrdersLogService;
import com.easaa.rebate.dao.RebateOrderMapper;
import com.easaa.rebate.dao.RebateStatsMapper;
import com.easaa.user.dao.UserAccountMapper;
import com.easaa.user.dao.UserPointsLogMapper;
import com.easaa.user.service.UserAccountService;

/**
 * 佣金统计 ,佣金分为收入统计和奉献统计,统计维度为月
 * 
 * @author 约
 *
 */
@Service
public class RebateStatsService extends EABaseService {
	@Autowired
	private RebateStatsMapper rebateStatsMapper;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private OrdersLogService ordersLogService;
	@Autowired
	private OrderMapper OrderMapper;
	@Autowired
	private RebateOrderMapper rebateOrderMapper;

	/**
	 * =========================================================================
	 * =============================
	 * =====================================受益统计================================
	 * ===============
	 * =========================================================================
	 * =============================
	 */
	/**
	 * 获取用户一个月的统计信息(分销统计)
	 * 
	 * @param user_id
	 * @param month
	 *            , 月份,(注意格式201608即为2016年8月);如果为空则返回当前月份
	 * @return
	 */
	public PageData getIncome(int user_id, String month) {
		PageData pd = new PageData();
		if (EAString.isNullStr(month)) {
			Date date = new Date();
			month = EADate.getDateAsStr(date, 2);
		}
		pd.put("month", month);
		pd.put("user_id", user_id);
		return rebateStatsMapper.selectIncomeByMap(pd);
	}

	/**
	 * 更新指定会员指定月份的佣金统计数据
	 * @param user_id
	 * @param month
	 *            (注意格式201608即为2016年8月);
	 * @param rebateLevel
	 * @param money
	 * @param points
	 * @return
	 */
	private boolean updateIncome(int user_id, String month, int rebateLevel, float money, int points, String ordersn) {
		PageData pd = new PageData();
		if (rebateLevel != 1 & rebateLevel != 2 & rebateLevel != 3) {
			return false;
		}
		BigDecimal amount = BigDecimal.valueOf(money);
		BigDecimal zero = BigDecimal.valueOf(0);
		// 先取到数据库内的记录
		PageData record = this.getIncome(user_id, month);
		if (EAUtil.isNotEmpty(record)) {
			float oldRebate = record.getAsFloat("rebate_" + rebateLevel);
//			int oldPoints = record.getAsInt("points_" + rebateLevel);
			if (oldRebate < 0) {
				oldRebate = Float.valueOf("0.0");
			}
//			if (oldPoints < 0) {
//				oldPoints = 0;
//			}
			amount = record.getAsBigDecimal("amount");
			if (amount.compareTo(zero)<0) {
				amount = zero;
			}
			amount = amount.add(BigDecimal.valueOf(money));
			money = money + oldRebate;
//			points = points + oldPoints;
		}
		pd.put("amount", amount);
		pd.put("rebate_" + rebateLevel, money);
//		pd.put("points_" + rebateLevel, points);
		pd.put("last_update", EADate.getCurrentTime());
		pd.put("user_id", user_id);
		pd.put("month", month);
		if (EAUtil.isNotEmpty(record)) {
			pd.put("id", record.getAsInt("id"));
			this.rebateStatsMapper.updateIncome(pd);
		} else {
			this.rebateStatsMapper.insertIncome(pd);
		}
//		PageData rebateParam = new PageData();
//		// 插入积分变更日志
//		if (EAUtil.isNotEmpty(points) && points > 0) {
//			rebateParam.put("user_id", user_id);
//			rebateParam.put("log_pay_order", ordersn);
//			rebateParam.put("log_time", EADate.getCurrentTime());
//			rebateParam.put("log_points", points);
//			rebateParam.put("log_expenditure", "1"); // 支出
//			rebateParam.put("log_type", "4");
//			rebateParam.put("log_role", "1");
//			rebateParam.put("log_note", "下级会员返佣：订单  "+ordersn+" 返佣："+points+" 积分");
//			userPointsLogMapper.insert(rebateParam);
//		}
		return true;
	}

	/**
	 * 查询收益统计记录
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getIncomeByPage(Page page) {
		return rebateStatsMapper.selectIncomeByPage(page);
	}

	/**
	 * =========================================================================
	 * =============================
	 * =====================================受益统计==========结束====================
	 * =================
	 * =========================================================================
	 * =============================
	 */
	/**
	 * =========================================================================
	 * =============================
	 * =====================================奉献统计==========开始====================
	 * =================
	 * =========================================================================
	 * =============================
	 */
	/**
	 * 获取用户一个月的统计信息
	 * 
	 * @param user_id
	 * @param month
	 *            , 月份,(注意格式201608即为2016年8月);如果为空则返回当前月份
	 * @return
	 */
	public PageData getGive(int user_id, String month) {
		PageData pd = new PageData();
		if (EAString.isNullStr(month)) {
			Date date = new Date();
			month = EADate.getDateAsStr(date, 2);
		}
		pd.put("month", month);
		pd.put("user_id", user_id);
		return rebateStatsMapper.selectGiveSelfByMap(pd);
	}

	/**
	 * 更新指定会员指定月份的佣金统计数据
	 * 
	 * @param user_id
	 * @param month
	 *            (注意格式201608即为2016年8月);
	 * @param rebateLevel
	 * @param money
	 * @param points
	 * @return
	 */
	private boolean updateGive(int user_id, String month, PageData order) {
		PageData pd = new PageData();
		// 先取到数据库内的记录
		PageData record = this.getGive(user_id, month);
		if (EAUtil.isNotEmpty(record)) {// 不为空,记录已存在
			for (int rebateLevel = 1; rebateLevel < 4; rebateLevel++) {
				float oldRebate = record.getAsFloat("rebate_" + rebateLevel);
//				int oldPoints = record.getAsInt("points_" + rebateLevel);
				float nRebate = order.getAsFloat("rebate_" + rebateLevel);
//				int nPoints = order.getAsInt("points_" + rebateLevel);
				// 相加以后
				nRebate = nRebate + oldRebate;
//				nPoints = nPoints + oldPoints;
				if (nRebate < 0) {
					pd.put("rebate_" + rebateLevel, "0");
				} else {
					pd.put("rebate_" + rebateLevel, nRebate);
				}
//				if (nPoints < 0) {
//					pd.put("points_" + rebateLevel, "0");
//				} else {
//					pd.put("points_" + rebateLevel, nPoints);
//				}
			}
			float amount = record.getAsFloat("amount");
			pd.put("amount", amount + order.getAsFloat("total_rebate"));
		} else {
			for (int rebateLevel = 1; rebateLevel < 4; rebateLevel++) {
				float oldRebate = order.getAsFloat("rebate_" + rebateLevel);
//				int oldPoints = order.getAsInt("points_" + rebateLevel);
				if (oldRebate < 0) {
					pd.put("rebate_" + rebateLevel, "0");
				} else {
					pd.put("rebate_" + rebateLevel, oldRebate);
				}
//				if (oldPoints < 0) {
//					pd.put("points_" + rebateLevel, "0");
//				} else {
//					pd.put("points_" + rebateLevel, oldPoints);
//				}
			}
			pd.put("amount", order.getAsFloat("total_rebate"));
		}

		pd.put("last_update", EADate.getCurrentTime());
		pd.put("user_id", user_id);
		pd.put("month", month);
		if (EAUtil.isNotEmpty(record)) {
			pd.put("id", record.getAsInt("id"));
			this.rebateStatsMapper.updateGive(pd);
		} else {
			this.rebateStatsMapper.insertGive(pd);
		}
		return true;
	}

	/**
	 * 查询奉献统计记录
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> getGiveByPage(Page page) {
		return rebateStatsMapper.selectGiveByPage(page);
	}

	/**
	 * 查询奉献统计记录
	 * 
	 * @param page
	 * @param level
	 *            查询等级 1:一级2:二级3:三级0:总额
	 * @return
	 */
	public List<PageData> getGiveByPageAndOrderByLeve(Page page, int level) {
		if (level > 3 || level < 0) {
			return null;
		}
		PageData pd = page.getPd();
		if (level == 0) {
			pd.put("orderBy", "amount");
		} else {
			pd.put("orderBy", "rebate_" + level);
		}
		page.setPd(pd);
		return rebateStatsMapper.selectGiveByPage(page);
	}

	/**
	 * 查询奉献统计记录
	 * 
	 * @param page
	 * @param level
	 *            查询等级 1:一级2:二级3:三级0:总额
	 * @return
	 */
	public List<PageData> getGiveByPageAndOrderByLeve(Page page, int level, int rec_type) {
		if (level > 3 || level < 0) {
			return null;
		}
		PageData pd = page.getPd();
		if (level == 0) {
			pd.put("orderBy", "amount");
		} else {
			pd.put("orderBy", "rebate_" + level);
		}
		pd.put("rec_type", rec_type);
		page.setPd(pd);
		return rebateStatsMapper.selectGiveByPage(page);
	}

	/**
	 * 查询奉献统计记录
	 * 
	 * @param page
	 * @param level
	 *            查询等级 1:一级2:二级3:三级0:总额
	 * @return
	 */
	public List<PageData> getUserMonth(Page page, String user_id, String type, String month) {
		PageData pd = page.getPd();
		pd.put("rec_user_id", user_id);
		pd.put("rec_type", type);
		pd.put("month", month);
		page.setPd(pd);
		return rebateStatsMapper.selectClassification(page);
	}

	/**
	 * 查询下级分销商列表
	 * 
	 * @param page
	 * @param type
	 *            查询等级 1:一级2:二级3:三级0:总额
	 * @return
	 */
	public List<PageData> getUserMonth(Page page, int type) {
		PageData pd = page.getPd();
		pd.put("rec_type", type);
		page.setPd(pd);
		return rebateStatsMapper.selectClassification(page);
	}

	/**
	 * 查询下级分销商列表(分页)
	 * 
	 * @param page
	 * @param type
	 *            查询等级 1:一级2:二级3:三级 0:总额
	 * @return
	 */
	public List<PageData> getUserMonthPage(Page page, String type) {
		PageData pd = page.getPd();
		pd.put("rec_type", type);
		page.setPd(pd);
		return rebateStatsMapper.selectificationPage(page);
	}

	/**
	 * 查询下级分销商不分页
	 * 
	 * 
	 */
	public List<PageData> selectification(PageData pd, String type) {
		pd.put("rec_type", type);
		return rebateStatsMapper.selectification(pd);
	}

	/**
	 * SELECT
	 * tu.user_id,tu.nick_name,tu.head_portrait,tu.user_name,trsg.*,tr.rec_type
	 * FROM t_user_relation AS tr LEFT JOIN t_user AS tu ON tr.child_user_id =
	 * tu.user_id LEFT JOIN t_rebate_stats_give AS trsg ON tr.child_user_id =
	 * trsg.user_id WHERE tr.rec_user_id=1 AND tr.rec_type=3 ORDER BY
	 * trsg.rebate_3 DESC
	 */

	/**
	 * =========================================================================
	 * =============================
	 * =====================================奉献统计==========结束====================
	 * =================
	 * =========================================================================
	 * =============================
	 */
	/**
	 * 管理员返佣
	 * 
	 * @param adminName
	 *            触发本次操作的管理员账号
	 * @param order
	 *            分销订单(不是购物的订单) , 分销订单包含了不同等级的受益会员
	 * @param buyOrder
	 *            订单,购物订单
	 * @return -1没有受益人 ,0执行失败,1执行成功
	 */
	public boolean updaterakeback(String adminName, PageData order, PageData buyOrder) throws Exception {
		if (EAUtil.isEmpty(order)) {
			return false;
		}
		String buyDate = buyOrder.getAsString("create_time");
		Date date = EADate.stringToDate(buyDate);
		buyDate = EADate.getDateAsStr(date, 2);
		// 更新会员收益统计
		String logNote = "";
		for (int level = 1; level < 4; level++) {
			int userId = order.getAsInt("user_" + level);// 1级
			if (userId > 0) {// 有一级分销商
				userAccountService.ordersRebate(userId, order.getAsString("order_sn"),
						order.getAsFloat("rebate_" + level), order.getAsInt("points_" + level));
				updateIncome(userId, buyDate, level, order.getAsFloat("rebate_" + level),
						order.getAsInt("points_" + level), order.getAsString("order_sn"));
				logNote = logNote + level + "级(用户:" + userId + ";现金:" + order.getAsFloat("rebate_" + level)
				+ ";积分:"+ order.getAsFloat("points_" + level) + ")";
			}
		}
		// 插入订单日志
		insertOrderLog(adminName, buyOrder, "订单完成,返佣给各级别受益人" + logNote);
		updateGive(order.getAsInt("user_1"), buyDate, order);
		// 更新奉献
		buyOrder = new PageData();
		buyOrder.put("rebate_status", 5);
		buyOrder.put("order_id", order.getAsInt("order_id"));
		OrderMapper.update(buyOrder);
		// 更新订单状态
		order.getAsString("order_sn");
		order.put("rebate_status", 5);
		rebateOrderMapper.update(order);
		return true;
	}

	/**
	 * 取消订单返佣
	 * 
	 * @param adminName
	 *            触发本次操作的管理员账号
	 * @param order
	 *            分销订单(不是购物的订单) , 分销订单包含了不同等级的受益会员
	 * @param buyOrder
	 *            订单,购物订单
	 * @return -1没有受益人 ,0执行失败,1执行成功
	 */
	public int cancelRakeback(String adminName, PageData order, PageData buyOrder) {
		if (EAUtil.isEmpty(order)) {
			return -1;
		}
		String buyDate = buyOrder.getAsString("create_time");
		Date date = EADate.stringToDate(buyDate);
		buyDate = EADate.getDateAsStr(date, 2);
		// 更新会员收益统计
		String logNote = "";
		for (int level = 1; level < 4; level++) {
			int userId = order.getAsInt("user_" + level);// 1级
			if (userId > 0) {// 有一级分销商
				userAccountService.cancelOrdersRebate(userId, order.getAsString("order_sn"),
						-order.getAsFloat("rebate_" + level), -order.getAsInt("points_" + level));
				updateIncome(userId, buyDate, 1, order.getAsFloat("rebate_1"), order.getAsInt("points_1"),
						order.getAsString("order_sn"));
				logNote = logNote + level + "级(用户:" + userId + ";现金:" + (-order.getAsFloat("rebate_" + level)) + ";积分:"
						+ (-order.getAsFloat("points_" + level)) + ")";
			}
		}
		// 插入订单日志
		insertOrderLog(adminName, buyOrder, "订单返佣被取消,扣除给各级别受益人的佣金" + logNote);
		updateGive(order.getAsInt("user_id"), buyDate, order);
		// 更新奉献
		return 1;
	}

	/**
	 * 插入订单日志
	 * 
	 * @param adminName
	 * @param buyOrder
	 * @param logNote
	 * @return
	 */
	private int insertOrderLog(String adminName, PageData buyOrder, String logNote) {
		PageData logData = new PageData();
		logData.put("log_note", "");
		logData.put("create_time", EADate.getCurrentTime());
		logData.put("creator", adminName);
		logData.put("pay_status", buyOrder.getAsString("pay_status"));
		logData.put("shipping_status", buyOrder.getAsString("shipping_status"));
		logData.put("order_status", buyOrder.getAsString("order_status"));
		logData.put("order_id", buyOrder.getAsString("order_id"));
		logData.put("auto_note", logData.getAsString(logNote));
		return ordersLogService.create(logData);
	}

	/**
	 * 查询下级每个等级会员的总人数
	 * 
	 * @param adminName
	 * @param buyOrder
	 * @param logNote
	 * @return
	 */
	public int getCountRelation(int userId, int rec_type) {
		PageData pd = new PageData();
		pd.put("user_id", userId);
		if (rec_type != 0) {
			pd.put("rec_type", rec_type);
		}
		return rebateStatsMapper.selectCountRelation(pd);
	}

	/**
	 * 查询奉献统计记录
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> selectGiveBackstageByPage(Page page) {
		return rebateStatsMapper.selectGiveBackstageByPage(page);
	}

	/**
	 * 查询收益统计记录
	 * 
	 * @param pd
	 * @return
	 */
	public List<PageData> selectIncomeBackstageByPage(Page page) {
		return rebateStatsMapper.selectIncomeBackstageByPage(page);
	}

	@Override
	public EADao getDao() {
		return rebateStatsMapper;
	}
}