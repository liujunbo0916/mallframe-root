package com.easaa.order.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.PageData;
import com.easaa.order.dao.OrdersGoodsMapper;
@Service
public class OrdersGoodsService extends EABaseService{
	@Autowired
	private OrdersGoodsMapper ordersGoodsMapper;
	@Override
	public EADao getDao(){
		return ordersGoodsMapper;
	}
	/**
	 * 查询指定订单的商品信息
	 * @param order_id 订单编号(自增ID,非SN)
	 * @return
	 */
	public List<PageData> getByOrderID(int order_id){
		PageData pd=new PageData();
		pd.put("order_id", order_id);
		return getByMap(pd);
	}
	/**
	 * 订单商品详情
	 * @param pd
	 * @return
	 */
	public PageData orderreturn(PageData pd)throws Exception{
		return ordersGoodsMapper.selectGRById(pd);
	}
}