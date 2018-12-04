package com.easaa.course.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseListingsData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String startTime;
	
	private String endTime;
	
	private boolean isDel;
	
	private String maxTime;
	
	private String minTime;
	
	private String target = "";
	
	
	
	public CourseListingsData(Integer id, String startTime, String endTime,int index) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public CourseListingsData(Integer id, String startTime, String endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		target = this.startTime+"--"+this.endTime;
		maxTime = this.endTime;
		minTime = this.startTime;
	}
	/**
	 * 自定义比较
	 * @return
	 */
	public void compare(CourseListingsData cd){
		if(cd == null){
			return;
		}
		if(this.endTime.equals(cd.getStartTime())){
			//如果开始时间等于自己的结束时间  将自己的结束时间设置成 对方的结束时间
			this.setEndTime(cd.getEndTime());
			cd.setDel(true);
			target =  this.startTime+"--"+this.endTime;
			maxTime = cd.getEndTime();
		}
		if(this.endTime.equals(cd.getStartTime())){
			//如果开始时间等于自己的结束时间  将自己的结束时间设置成 对方的结束时间
			this.setEndTime(cd.getEndTime());
			cd.setDel(true);
			target =  this.startTime+"--"+this.endTime;
			maxTime = cd.getEndTime();
		}
		
	}
	
	public String  merge(CourseListingsData cd){
		return this.target+","+cd.getTarget();
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public boolean isDel() {
		return isDel;
	}
	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public String getMaxTime() {
		return maxTime;
	}
	public String getMinTime() {
		return minTime;
	}
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
