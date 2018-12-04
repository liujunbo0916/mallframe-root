package com.easaa.course.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.course.dao.VenueListingsMapper;
import com.easaa.course.entity.CourseListingsData;
import com.easaa.entity.PageData;

@Service
public class VenueListingsService extends EABaseService {

	@Autowired
	private VenueListingsMapper venueListingsMapper;

	@Autowired
	private VenueService venueService;

	@Override
	public EADao getDao() {
		return venueListingsMapper;
	}

	public void genListings(PageData pageData) throws Exception {
		String type = pageData.getAsString("type");
		String interval_time = pageData.getAsString("interval_time");
		type = StringUtils.isNotEmpty(type) ? type : "0";
		String venue_id = pageData.getAsString("venue_id");
		String place_id = pageData.getAsString("place_id");
		PageData venue = venueService.getById(Integer.parseInt(venue_id));
		List<String> splitListings = new ArrayList<String>();
		if (type.equalsIgnoreCase("0")) {
			// 手动添加
			String timeStr = pageData.getAsString("timeStr");
			if (StringUtils.isEmpty(timeStr)) {
				throw new Exception("请选择时间段");
			}
			String[] timeEntry = timeStr.split("\\|");
			if (timeEntry.length == 0) {
				throw new Exception("请选择时间段");
			}
			Collections.addAll(splitListings, timeEntry);
		} else {
			// 自动生成
			if (StringUtils.isEmpty(interval_time)) {
				throw new Exception("请输入间隔时间");
			}
			if (!EAString.isPositiveInt(interval_time)) {
				throw new Exception("间隔时间必须为正整数");
			}
			// 开放时间
			String venue_opentime = venue.getAsString("venue_opentime");
			int startTime = 0, endTime = 0;
			if (1 == venue.getAsInteger("venue_limittime")) { // 限制开放时间
				if (StringUtils.isNotEmpty(venue_opentime)) {
					String[] venueOpenTimeAry = venue_opentime.split(",");
					startTime = Integer.parseInt(venueOpenTimeAry[0]);
					endTime = Integer.parseInt(venueOpenTimeAry[1]);
				}
			}
			splitListings = splitListings(startTime, endTime, Integer.parseInt(interval_time));
		}
		
		if(newsplitListings(splitListings)){
			// id更新，入库
			for (String s : splitListings) {
				String[] ss = s.split(",");
				if (EAUtil.isNotEmpty(ss[0]) && !ss[0].equals("0")) {
					pageData.put("id", ss[0]);
					pageData.put("place_id", place_id);
					pageData.put("listings_start_time", ss[1]);
					pageData.put("listings_end_time", ss[2]);
					this.edit(pageData);
				} else {
					pageData.put("listings_start_time", ss[1]);
					pageData.put("listings_end_time", ss[2]);
					this.create(pageData);
				}
			}
		}else{
			throw new Exception("时间段有交叉，请重新选择！");
		};
	}

	private static List<String> splitListings(int startTime, int endTime, int intervalTime) {
		List<String> result = new ArrayList<String>();
		Calendar compare = Calendar.getInstance();
		if (endTime == 0 || endTime == 24) {
			compare.set(2017, 3, 2, endTime, 0, 0);
		} else {
			compare.set(2017, 3, 1, endTime, 0, 0);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 3, 1, startTime, 0, 0);
		for (;;) {
			String firstTime = calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ StringUtils.leftPad(calendar.get(Calendar.MINUTE) + "", 2, "0");
			calendar.add(Calendar.MINUTE, intervalTime);
			String secondTime = calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ StringUtils.leftPad(calendar.get(Calendar.MINUTE) + "", 2, "0");
			if (compare.compareTo(calendar) == -1) {
				break;
			}
			result.add("0," + firstTime + "," + secondTime);
		}
		return result;
	}

	/**
	 * 判断时间是否有交叉
	 * @param splitListings
	 * @return
	 */
	private static boolean newsplitListings(List<String> splitListings) {
		List<CourseListingsData> data = new ArrayList<CourseListingsData>();
		for (String string : splitListings) {
			String[] ss = string.split(",");
			if(EAUtil.isEmpty(ss[0])){
				ss[0]="0";
			}
			data.add(new CourseListingsData(Integer.valueOf(ss[0]), ss[1], ss[2], 0));
		}
		for (int i = 0; i < data.size(); i++) {
			String st=data.get(i).getStartTime();
			String et=data.get(i).getEndTime();
			long newStartTime = EADate.getMillis(EADate.stringToDate("2017-5-3 "+data.get(i).getStartTime()+":00"));
			long newEndTime = EADate.getMillis(EADate.stringToDate("2017-5-3 "+data.get(i).getEndTime()+":00"));
			for (int j = data.size()-1; j >0; j--) {
				String ost=data.get(j).getStartTime();
				String oet=data.get(j).getEndTime();
				long oldStartTime = EADate.getMillis(EADate.stringToDate("2017-5-3 "+data.get(j).getStartTime()+":00"));
				long oldEndTime = EADate.getMillis(EADate.stringToDate("2017-5-3 "+data.get(j).getEndTime()+":00"));
				if(newStartTime>oldStartTime && newStartTime<oldEndTime || newEndTime > oldStartTime && newEndTime<oldEndTime){
					return false;
				}
				/*else
				if(newStartTime==oldStartTime && newEndTime==oldEndTime){
					break;
				}else
				if(newStartTime==oldStartTime || newStartTime==oldEndTime || newEndTime == oldStartTime || newEndTime==oldEndTime){
					return false;
				}*/
			}
		}
		return true;

	}

	public PageData selectIdByCId(Integer pd){
		return venueListingsMapper.selectIdByCId(pd);
	}
	public List<PageData> selectTimeByCId(PageData pd){
		return venueListingsMapper.selectTimeByCId(pd);
	}
	
	
	public static void main(String[] args) {
		//[200,11:00,12:00, 202,09:00,10:30, 203,14:30,15:30, ,09:30,11:30]
		String s = "200,11:00,12:00";
		String s1 = "202,09:00,10:30";
		String s2 = "203,14:30,15:30";
		String s3 = ",15:30,17:30";
		List<String> list= new ArrayList<String>();
		list.add(s);
		list.add(s1);
		list.add(s2);
		list.add(s3);
		System.out.println(newsplitListings(list));

	}

}
