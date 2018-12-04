package com.easaa.goods.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsBrandMapper;
import com.easaa.goods.dao.GoodsCategoryMapper;

@Service
public class GoodsBrandService extends EABaseService {

	@Autowired
	private GoodsBrandMapper goodsBrandMapper;
	
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return goodsBrandMapper;
	}
	public Map<String,List<PageData>> selectByGroup(PageData pd){
		pd.put("bs_id", "0");
		List<PageData> dataBaseData =  goodsBrandMapper.selectByGroup(pd);
		Map<String,List<PageData>> result = new HashMap<>();
		for(PageData tpd : dataBaseData){
			StringBuffer  categoryName =  new StringBuffer();
			if(StringUtils.isNotEmpty(tpd.getAsString("firstCategory"))){
				categoryName.append(tpd.getAsString("firstCategory")).append("/");
			}
			if(StringUtils.isNotEmpty(tpd.getAsString("secondCategory"))){
				categoryName.append(tpd.getAsString("secondCategory")).append("/");
			}
			if(StringUtils.isNotEmpty(tpd.getAsString("thirdCategory"))){
				categoryName.append(tpd.getAsString("thirdCategory")).append("/");
			}
			String ss = null;
			if(categoryName.toString().endsWith("/")){
				ss = categoryName.substring(0, categoryName.length()-1);
			}
			if(!result.containsKey(ss)){
				result.put(ss, new ArrayList<PageData>());
			}
			result.get(ss).add(tpd);
		}
		return result;
	}
	/**
	 * 根据分类向上递归查询商品
	 * @param pd
	 * @return
	 */
	public List<PageData> recursion(PageData pd) {
		/*Page page = new Page();
		page.setPd(pd);
		if(StringUtils.isNotEmpty(pd.getAsString("category2_id")) && !"-1".equals(pd.getAsString("category2_id"))){
			//如果传了二级分类   先查出一级分类
			pd.put("category_id", pd.getAsString("category2_id"));
			List<PageData> categoryOnes = goodsCategoryMapper.selectByMap(page);
			if(categoryOnes != null && categoryOnes.size() > 0){
				pd.clear();
				pd.put("category1_id", categoryOnes.get(0).getAsString("parent_id"));
			}
		}
		if(StringUtils.isNotEmpty(pd.getAsString("category3_id")) && !"-1".equals(pd.getAsString("category3_id"))){
			pd.put("category_id", pd.getAsString("category3_id"));
			List<PageData> categoryOnes = goodsCategoryMapper.selectByMap(page);
			if(categoryOnes != null && categoryOnes.size() > 0){
				pd.put("category_id", categoryOnes.get(0).getAsString("category_id"));
				List<PageData> categoryTwos = goodsCategoryMapper.selectByMap(page);
				if(categoryTwos != null && categoryTwos.size() >0 ){
					pd.clear();
					pd.put("category1_id", categoryTwos.get(0).getAsString("parent_id"));
				}
			}
		}
		return goodsBrandMapper.selectByMap(page);*/
		
		Page page = new Page();
		page.setPd(pd);
		if(StringUtils.isNotEmpty(pd.getAsString("category2_id")) && !"-1".equals(pd.getAsString("category2_id"))){
			//如果传了二级分类   先查出一级分类
			pd.put("category_id", pd.getAsString("category2_id"));
			List<PageData> categoryOnes = goodsCategoryMapper.selectByMap(page);
			if(categoryOnes != null && categoryOnes.size() > 0){
				pd.put("parent_id", categoryOnes.get(0).getAsString("parent_id"));
				pd.put("category1_id", categoryOnes.get(0).getAsString("parent_id"));
				List<PageData> categoryLists = goodsCategoryMapper.sublings(pd);
				StringBuffer sb = new StringBuffer();
				String sbParam = "";
				if(categoryLists != null && categoryLists.size() > 0 ){
					for(PageData tpd : categoryLists){
						sb.append(tpd.getAsString("category_id")+",");
					}
					sbParam = sb.subSequence(0, sb.length()-1).toString();
					pd.put("category2_id", sbParam);
				}else{
					pd.remove("category2_id");
				}
			}
		}
		if(StringUtils.isNotEmpty(pd.getAsString("category3_id")) && !"-1".equals(pd.getAsString("category3_id"))){
			pd.put("category_id", pd.getAsString("category3_id"));
			List<PageData> categoryOnes = goodsCategoryMapper.selectByMap(page);
			if(categoryOnes != null && categoryOnes.size() > 0){
				pd.put("category_id", categoryOnes.get(0).getAsString("parent_id"));
				List<PageData> categoryTwos = goodsCategoryMapper.selectByMap(page);
				pd.put("category_id", pd.getAsString("category3_id"));
				if(categoryTwos != null && categoryTwos.size() >0 ){
					pd.put("category1_id", categoryTwos.get(0).getAsString("parent_id"));
					pd.put("parent_id", categoryOnes.get(0).getAsString("parent_id"));
					pd.put("category_id", pd.getAsString("category3_id"));
					List<PageData> categoryLists = goodsCategoryMapper.sublings(pd);
					StringBuffer sb = new StringBuffer();
					String sbParam = "";
					if(categoryLists != null && categoryLists.size() > 0 ){
						for(PageData tpd : categoryLists){
							sb.append(tpd.getAsString("category_id")+",");
						}
						sbParam = sb.subSequence(0, sb.length()-1).toString();
						pd.put("category3_id", sbParam);
					}else{
						pd.remove("category3_id");
					}
				}
			}
		}
		if(pd.containsKey("parent_id")){
			pd.remove("parent_id");
		}
		pd.put("bs_id", "0");
		return goodsBrandMapper.recursion(pd);
		
		
	}
	
	
	public List<PageData> selectByCIds(PageData pd){
		pd.put("bs_id", "0");
		return goodsBrandMapper.selectByCIds(pd);
	}
	public List<PageData> selectlSQByPage(Page pd){
		return goodsBrandMapper.selectlSQByPage(pd);
	}
}
