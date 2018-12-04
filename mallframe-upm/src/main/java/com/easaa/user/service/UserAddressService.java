package com.easaa.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserAddressMapper;

@Service
public class UserAddressService extends EABaseService {
	@Autowired
	private UserAddressMapper userAddressMapper;

	public List<PageData> getUserAddressList(int userId) {
		Page page = new Page();
		PageData pd = new PageData();
		pd.put("user_id", userId);
		pd.put("default", 1);
		page.setPd(pd);
		return getDao().selectByMap(page);
	}

	public List<PageData> getUserAddress(PageData pd) {
		Page page = new Page();
		pd.put("orderby", 1);
		pd.put("is_default", 1);
		page.setPd(pd);
		List<PageData> list	= getDao().selectByMap(page);
		pd.put("is_default", "0");
		page.setPd(pd);
		list.addAll(getDao().selectByMap(page));
		return list;
	}

	public int updateDefaultAddress(int user_id, int address_id) {
		PageData pd = new PageData();
		pd.put("user_id", user_id);
		pd.put("is_default", "0");
		this.edit(pd);
		pd.put("address_id", address_id);
		pd.put("is_default", 1);
		return this.edit(pd);
	}

	@Override
	public EADao getDao() {
		return userAddressMapper;
	}

	/**
	 * 根据用户id查询默认收货地址信息
	 * 
	 * @param string
	 * @return
	 */
	public PageData getDefaultByUid(String uid) {
		return userAddressMapper.getDefaultByUid(uid);
	}
}