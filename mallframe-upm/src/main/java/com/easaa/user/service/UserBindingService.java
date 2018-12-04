package com.easaa.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
import com.easaa.user.dao.UserBindingMapper;
@Service
public class UserBindingService extends EABaseService{
	@Autowired
	private UserBindingMapper userBindingMapper;
	
	public int updataBinding(int user_id,int address_id){
		PageData pd=new PageData();
		pd.put("user_id", user_id);
		pd.put("is_default", "0");
		this.edit(pd);
		pd.put("binding_id", address_id);
		pd.put("is_default", 1);
		return this.edit(pd);
	}
	
	public EADao getDao(){
		return userBindingMapper;
	}
}