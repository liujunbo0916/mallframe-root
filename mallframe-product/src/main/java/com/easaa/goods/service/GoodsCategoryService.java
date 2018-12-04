package com.easaa.goods.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsCategoryMapper;

import net.sf.json.JSONArray;

@Service
public class GoodsCategoryService extends EABaseService {

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return goodsCategoryMapper;
	}

	/**
	 * 商品类目表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> goodsCategoryList(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("parent_id"))) {
			throw new Exception("类目ID不能为空");
		}
		return getByMap(pd);
	}
	

	/**
	 * 查看一个类目是否 是叶子节点
	 */
	public boolean queryCategoryIsLeaf(PageData pd) {
		/**
		 * 如果没有上级 则不是叶子节点 没有达到三级
		 */
		if (pd.getAsInteger("parent_id") == 0) {
			return false;
		}
		PageData param = new PageData();
		param.put("category_id", pd.getAsInt("parent_id"));
		PageData parentCategory = getByMap(param).get(0);
		/**
		 * 如果父亲没有上级 则也不是叶子节点
		 */
		if (parentCategory.getAsInt("parent_id") == 0) {
			return false;
		} else {
			return true;
		}
	}

	public PageData selectOrder(PageData pageData) {
		/**
		 * 查询最大值
		 */
		List<PageData> result = goodsCategoryMapper.selectMaxOrder(pageData);
		if(result == null || result.size() == 0){
			pageData.put("maxValue", pageData.getAsString("orderValue"));
			pageData.put("orderValue", 1);
		}else{
			pageData.put("maxValue", result.get(0).getAsString("category_order"));
			pageData.put("orderValue", result.size()+1);
		}
		return pageData;
	}

	/**
	 * 获取所有分类
	 * 将商品分类或者课程分类封装成父子级的数据结构 并以json字符串返回
	 * @return
	 */
	public String  categoryEncapsulationToJson(PageData pd){
		Page page = new Page();
		page.setPd(pd);
		List<PageData> varList = goodsCategoryMapper.selectByMap(page);
		List<PageData> firstPd = new ArrayList<>();
		List<PageData> secondPd = new ArrayList<>();
		for (Iterator<PageData> outterIt = varList.iterator();outterIt.hasNext();) {
			PageData currentPd = outterIt.next();
			if("0".equalsIgnoreCase(currentPd.getAsString("parent_id")) || StringUtils.isEmpty(currentPd.getAsString("parent_id"))){
				firstPd.add(currentPd);
			}
		}
		for(PageData outterTemp:firstPd){
			for(PageData innerTemp : varList){
				if(outterTemp.getAsString("category_id").equals(innerTemp.getAsString("parent_id"))){
					List<PageData> tempChild = (List<PageData>) outterTemp.get("childs");
					if(tempChild == null){
						tempChild = new ArrayList<>();
						outterTemp.put("childs", tempChild);
					}
					tempChild.add(innerTemp);
					//将第二级元素放在临时集合里面方便下面操作
					secondPd.add(innerTemp);
				}
			}
		}
		for(PageData outterTemp:secondPd){
			for(PageData innerTemp : varList){
				if(outterTemp.getAsString("category_id").equals(innerTemp.getAsString("parent_id"))){
					List<PageData> tempChild = (List<PageData>) outterTemp.get("childs");
					if(tempChild == null){
						tempChild = new ArrayList<>();
						outterTemp.put("childs", tempChild);
					}
					tempChild.add(innerTemp);
				}
			}
		}
		JSONArray jsonlist=JSONArray.fromObject(firstPd);
		return jsonlist.toString();
	}
	
	public List<PageData> selectHeatList() {
		return goodsCategoryMapper.selectHeatList();
	}
}
