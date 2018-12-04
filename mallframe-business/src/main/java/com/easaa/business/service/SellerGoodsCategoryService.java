package com.easaa.business.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.business.dao.SellerGoodsCategoryMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

import net.sf.json.JSONArray;

@Service
public class SellerGoodsCategoryService extends EABaseService {
	@Autowired
	private SellerGoodsCategoryMapper sellerGoodsCategoryMapper;
	
	@Override
	public EADao getDao() {
		return sellerGoodsCategoryMapper;
	}
	/**
	 * 申请列表
	 */
	public List<PageData> bindCategoryList(PageData pd){
		return  sellerGoodsCategoryMapper.bindCategoryList(pd);
	}
	
	/**
	 * 申请审核列表（page分页）
	 */
	public List<PageData> bindCategoryListPage(Page page){
		return  sellerGoodsCategoryMapper.bindCategoryListPage(page);
	}
	
	
	/**
	 * 添加申请分类
	 * @param pd
	 * @throws Exception 
	 */
    public void doApply(PageData pd) throws Exception {
    	List<PageData> applyList = sellerGoodsCategoryMapper.selectApplyCategroy(pd);
    	if(applyList != null && applyList.size() > 0){
    		throw new Exception("已申请该分类");
    	}
    	sellerGoodsCategoryMapper.insertBindCategory(pd);
	}
    /**
     * 删除申请分类
     */
    public void delApply(PageData pd){
    	sellerGoodsCategoryMapper.delApply(pd);
    }
    
    /**
	 * 获取商家申请的所有分类
	 * 将商家商品分类或者课程分类封装成父子级的数据结构 并以json字符串返回
	 * @return
	 */
	public String  categoryBusinessToJson(List<PageData> varList,PageData pd){
		Page page = new Page();
		pd.put("state", "1");
		page.setPd(pd);
		List<PageData> bsList = sellerGoodsCategoryMapper.selectByMap(page);
		List<PageData> firstPd = new ArrayList<>();
		List<PageData> secondPd = new ArrayList<>();
		for (Iterator<PageData> outterIt = varList.iterator();outterIt.hasNext();) {
			PageData currentPd = outterIt.next();
			if("0".equalsIgnoreCase(currentPd.getAsString("parent_id")) || StringUtils.isEmpty(currentPd.getAsString("parent_id"))){
				for (PageData bsdata : bsList) {
					if(bsdata.getAsString("class_1").equals(currentPd.getAsString("category_id"))){
						if(!firstPd.contains(currentPd)){
							firstPd.add(currentPd);
						}
					}
				}
			}
		}
		for(PageData outterTemp:firstPd){
			for(PageData innerTemp : varList){
				if(outterTemp.getAsString("category_id").equals(innerTemp.getAsString("parent_id"))){
					List<PageData> tempChild = (List<PageData>) outterTemp.get("childs");
					for (PageData bsdata : bsList) {
						if(bsdata.getAsString("class_2").equals(innerTemp.getAsString("category_id"))){
							if(tempChild == null){
								tempChild = new ArrayList<>();
								outterTemp.put("childs", tempChild);
							}
							
							if(!tempChild.contains(innerTemp)){
								tempChild.add(innerTemp);
								//将第二级元素放在临时集合里面方便下面操作
								secondPd.add(innerTemp);
							}
						}
					}
				}
			}
		}
		for(PageData outterTemp:secondPd){
			for(PageData innerTemp : varList){
				if(outterTemp.getAsString("category_id").equals(innerTemp.getAsString("parent_id"))){
					List<PageData> tempChild = (List<PageData>) outterTemp.get("childs");
					for (PageData bsdata : bsList) {
						if(bsdata.getAsString("class_3").equals(innerTemp.getAsString("category_id"))){
							if(tempChild == null){
								tempChild = new ArrayList<>();
								outterTemp.put("childs", tempChild);
							}
							if(!tempChild.contains(innerTemp)){
								tempChild.add(innerTemp);
							}
						}
					}
				}
			}
		}
		JSONArray jsonlist=JSONArray.fromObject(firstPd);
		return jsonlist.toString();
	}
}
