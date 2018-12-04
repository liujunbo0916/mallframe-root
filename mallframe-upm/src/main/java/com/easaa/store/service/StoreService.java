package com.easaa.store.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.store.dao.StoreMapper;

@Service
public class StoreService extends EABaseService{
	@Autowired
	private StoreMapper storeMapper;
	@Override
	public EADao getDao(){
		return storeMapper;
	}
}