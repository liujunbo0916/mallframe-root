package com.easaa.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.easaa.common.service.DictionariesService;
import com.easaa.entity.PageData;

public class CodeUtil {
	
	private static List<PageData> allCode;
	private static List<PageData> groupCode;
	public  DictionariesService dictionariesService;
	
	public CodeUtil(DictionariesService dictionariesService){
		this.dictionariesService = dictionariesService;
		if(allCode==null){
			try {
				allCode=dictionariesService.dataList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public DictionariesService getDictionariesService() {
		return dictionariesService;
	}
	public void setDictionariesService(DictionariesService dictionariesService) {
		this.dictionariesService = dictionariesService;
	}
	
	/**
	 * 查询编码对应的值
	 * @param bianma
	 * @return
	 * @throws Exception
	 */
	public String getCodeDesc(String bianma) throws Exception {
		
		bianma = bianma == null ?"":bianma;
		String codedesc = null;
		for(PageData pd : allCode){
			if(bianma.equals(pd.getString("BIANMA"))){
				codedesc = pd.getString("NAME");
			}
		}
		return codedesc;
	}
	
	public String getCodeByEnName(String enName) throws Exception{
		String code = null;
		for(PageData pd : allCode){
			if(enName.equals(pd.getString("NAME_EN"))){
				code = pd.getString("BIANMA");
			}
		}
		return code;
	}
	
	/**
	 * 根据名字
	 * 查询一组编码值
	 * @throws Exception 
	 */
	public List<PageData> getGroupCode(String name) throws Exception{
		String codeId = null;
		groupCode = new ArrayList<PageData>();
		for(PageData pd : allCode){
			if(name.equals(pd.getString("NAME"))){
				codeId = pd.getString("DICTIONARIES_ID");
			}
		}
		for(PageData pd : allCode){
			if(codeId.equals(pd.getString("PARENT_ID"))){
				groupCode.add(pd);
			}
		}
		return groupCode;
	}
	
	/**
	 * 根据英文名字查询一组编码
	 * @param en
	 * @return
	 * @throws Exception
	 */
	
	public List<PageData> getGroupCodeByEn(String en) throws Exception{
		String codeId = null;
		groupCode = new ArrayList<PageData>();
		for(PageData pd : allCode){
			if(en.equals(pd.getString("NAME_EN"))){
				codeId = pd.getString("DICTIONARIES_ID");
			}
		}
		for(PageData pd : allCode){
			if(codeId.equals(pd.getString("PARENT_ID"))){
				groupCode.add(pd);
			}
		}
		Collections.sort(groupCode, new Comparator<PageData>() {
			public int compare(PageData o1, PageData o2) {
				return o1.get("ORDER_BY").toString().compareTo(o2.get("ORDER_BY").toString());
			}
		});
		return groupCode;
	}
	
	/**
	 * 根据编码查询一组编码
	 * @param masterId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getGroupCodeByCode(String code) throws Exception{
		String codeId = null;
		groupCode = new ArrayList<PageData>();
		for(PageData pd : allCode){
			if(code.equals(pd.getString("BIANMA"))){
				codeId = pd.getString("DICTIONARIES_ID");
			}
		}
		for(PageData pd : allCode){
			if(codeId.equals(pd.getString("PARENT_ID"))){
				groupCode.add(pd);
			}
		}
		Collections.sort(groupCode, new Comparator<PageData>() {
			public int compare(PageData o1, PageData o2) {
				return o1.get("ORDER_BY").toString().compareTo(o2.get("ORDER_BY").toString());
			}
		});
		return groupCode;
	}
	
	/**
	 * 根据编码查询一组编码
	 * @param masterId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getGroupByCode(String code) throws Exception{
		String parentId = null;
		groupCode = new ArrayList<PageData>();
		for(PageData pd : allCode){
			if(code.equals(pd.getString("BIANMA"))){
				parentId = pd.getString("PARENT_ID");
			}
		}
		for(PageData pd : allCode){
			if(parentId.equals(pd.getString("PARENT_ID"))){
				groupCode.add(pd);
			}
		}
		Collections.sort(groupCode, new Comparator<PageData>() {
			public int compare(PageData o1, PageData o2) {
				return o1.get("ORDER_BY").toString().compareTo(o2.get("ORDER_BY").toString());
			}
		});
		return groupCode;
	}
	
	
	/*public PageData getPicturePath(String masterId,String type ) throws Exception{
		PageData pd = new PageData();
		pd.put("MASTER_ID", masterId);
		pd.put("TYPE", type);
		return picturesService.findPicByMasterAndType(pd);
	}*/
	
	
}
