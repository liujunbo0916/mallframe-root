package com.easaa.goods.service;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsCategoryMapper;
import com.easaa.goods.dao.GoodsTypeMapper;
@Service
public class GoodsTypeService extends EABaseService{
	@Autowired
	private GoodsTypeMapper goodsTypeMapper;
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Override
	public EADao getDao(){
		return goodsTypeMapper;
	}
	
	
	
	/**
	 * 根据分类向上递归查询商品
	 * @param pd
	 * @return
	 */
	public List<PageData> addRecursion(PageData pd) {
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
		return goodsTypeMapper.recursion(pd);
	}
}