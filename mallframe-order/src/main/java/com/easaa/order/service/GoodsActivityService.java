package com.easaa.order.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.service.BusinessService;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.order.dao.GoodsActivityCompoMapper;
import com.easaa.order.dao.GoodsActivitySeckillMapper;
import com.easaa.order.dao.GoodsActivityTimelimitMapper;
import com.easaa.order.entity.Compo;
import com.easaa.util.properties.PropertiesFactory;
import com.easaa.util.properties.PropertiesFile;
import com.easaa.util.properties.PropertiesHelper;

@Service
public abstract class GoodsActivityService extends EABaseService {
	
	private static final PropertiesHelper PROPERTIESHELPER = PropertiesFactory.getPropertiesHelper(PropertiesFile.SYS);

	@Autowired
	private BusinessService businessService;

	/**
	 * 限时秒杀活动
	 */
	@Autowired
	private GoodsActivitySeckillMapper goodsActivitySeckillMapper;
	/**
	 * 商品打包活动
	 * 
	 */
	@Autowired
	private GoodsActivityCompoMapper goodsActivityCompoMapper;
	/**
	 * 限时折扣活动
	 */
	@Autowired
	private GoodsActivityTimelimitMapper goodsActivityTimelimitMapper;

	
}
