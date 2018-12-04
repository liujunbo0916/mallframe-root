package com.easaa.hautecouture.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;

public interface ApplicationImgMapper extends EADao{

	public List<PageData> getImagesById(String aplc_id);

	public List<PageData> getDesigns(PageData temp);

}
