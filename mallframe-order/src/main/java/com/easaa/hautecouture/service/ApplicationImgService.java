package com.easaa.hautecouture.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.FtpUtil;
import com.easaa.entity.PageData;
import com.easaa.hautecouture.dao.ApplicationImgMapper;

/**
 * 用户高端定制图片Service
 * @author hsia
 * @version 2016-08-24
 */
@Service
public class ApplicationImgService extends EABaseService{
	@Autowired
	private ApplicationImgMapper applicationImgMapper;

	@Override
	public EADao getDao() {
		return applicationImgMapper;
	}
	/*
	 * 根据订制申请id查询图片
	 */
	public List<PageData> getImagesById(String aplc_id) throws Exception{
		return (List<PageData>) applicationImgMapper.getImagesById(aplc_id);
	}
	public List<PageData> getDesigns(PageData temp) {
		return (List<PageData>) applicationImgMapper.getDesigns(temp);
	}
	
	@SuppressWarnings("restriction")
	public int uploadImg(PageData pd)throws Exception {
		byte[] bt = null;
		int i = 0;
		try {
			bt = new sun.misc.BASE64Decoder().decodeBuffer(pd.getAsString("paramContent"));
			String path = FtpUtil.upload(bt, "mallframe", "order", pd.getString("name"));
			//插入到数据库
			pd.put("path", path);
			i  = applicationImgMapper.insert(pd);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return i;
	}
	
	
	
	
}
