package com.easaa.appoint.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.appoint.dao.AppointmentMapper;
@Service
public class AppointmentService extends EABaseService{
	@Autowired
	private AppointmentMapper appointmentMapper;
	@Override
	public EADao getDao(){
		return appointmentMapper;
	}
}