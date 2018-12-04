package com.easaa.order.entity;

import java.util.List;

public class SecKillGoodsList {

	private Seckill secKill;
	
	private List<SeckillPro> pros;

	public Seckill getSecKill() {
		return secKill;
	}


	public void setSecKill(Seckill secKill) {
		this.secKill = secKill;
	}


	public List<SeckillPro> getPros() {
		return pros;
	}


	public void setPros(List<SeckillPro> pros) {
		this.pros = pros;
	}
	
	
}
