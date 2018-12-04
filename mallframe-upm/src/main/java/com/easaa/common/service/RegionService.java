package com.easaa.common.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.common.dao.RegionMapper;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.entity.PageData;
@Service
public class RegionService extends EABaseService{
	@Autowired
	private RegionMapper regionMapper;
	
	private static PageData result = new PageData();  //省市区对象缓存
	
	/**
	 * 获取某一地区下所有子一级地区列表
	 * @param pid
	 * @return
	 */
	public List<PageData> getListByParentId(int pid){
		PageData pd=new PageData();
		pd.put("parent_id", pid);
		return getByMap(pd);
	}
	
    public List<PageData> selectByProvince(PageData pd){
    	return regionMapper.selectByProvince(pd);
    }
	public List<PageData> selectByCity(PageData pd){
		return regionMapper.selectByCity(pd);
	}
	
	public List<PageData> selectByArea(PageData pd){
		return regionMapper.selectByArea(pd);
	}
	/**
	 * 省市区按照一个规定的数据结构返回
	 */
	public PageData  selectAllPCA(){
		
		if(result.size() == 0){
			List<PageData> province =this.selectByProvince(new PageData());
			PageData cityPd = new PageData();
			PageData distPd = new PageData();
			PageData pdParam = new PageData();
			PageData distPdParam = new PageData();
			for(PageData tp : province){
				pdParam.put("parent_id", tp.getAsString("value"));
				List<PageData> citys = this.selectByCity(pdParam);
				cityPd.put(tp.getAsString("value"), citys);
				System.out.println("<><><><><>><"+tp.getAsString("value"));
				System.out.println("<><><><><><><>"+cityPd.get(tp.getAsString("value")));
				for(PageData city : citys){
					distPdParam.put("parent_id", city.getAsString("value"));
					distPd.put(city.getAsString("value"), this.selectByCity(distPdParam));
				}
			}
			result.put("province", province);
			result.put("dist", distPd);
			result.put("city", cityPd);
		}
		//查询省
		return result;
	}
	@Override
	public EADao getDao(){
		return regionMapper;
	}
}