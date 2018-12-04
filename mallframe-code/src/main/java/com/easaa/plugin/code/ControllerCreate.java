package com.easaa.plugin.code;
import java.util.List;

import org.aspectj.util.FileUtil;
import org.springframework.web.servlet.ModelAndView;

import com.easaa.core.util.EADate;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
public class ControllerCreate {

	private static final String LINE = "\r\n";
    private static final String TAB = "\t";
    StringBuffer sb = new StringBuffer();
    private TableModel table;
	public ControllerCreate(TableModel table)throws Exception{
		this.table=table;
    }
	public String doCreate() throws Exception {
		String serviceName=table.getLowBaseName()+TableModel.Service;
        String spaceName=table.getModuleName();
		
		
		sb.append("package com.easaa.controller."+table.getModuleName()+";").append(LINE).append(LINE);
		sb.append("import java.util.List;").append(LINE);
        sb.append("import javax.servlet.http.HttpServletRequest;").append(LINE);
        sb.append("import javax.servlet.http.HttpServletResponse;").append(LINE);
        sb.append("import com.easaa.controller.comm.BaseController;").append(LINE);
        sb.append("import com.easaa.entity.Page;").append(LINE);
        sb.append("import com.easaa.entity.PageData;").append(LINE);
        sb.append("import com.easaa.core.util.EAString;").append(LINE);
        sb.append("import com.easaa.core.util.EADate;").append(LINE);
        sb.append("import org.springframework.beans.factory.annotation.Autowired;").append(LINE);
        sb.append("import org.springframework.ui.ModelMap;").append(LINE);
        sb.append("import org.springframework.stereotype.Controller;").append(LINE);
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;").append(LINE);
        sb.append("import org.springframework.web.servlet.ModelAndView;").append(LINE);
        sb.append("import com.easaa."+table.getModuleName()+".service."+table.getBaseName()+TableModel.Service+";").append(LINE);
       
        sb.append("@Controller").append(LINE);
        sb.append("@RequestMapping(\"/sys/"+spaceName+"/"+table.getLowBaseName()+"/\")").append(LINE);
        
        sb.append("public class "+table.getBaseName()+TableModel.Controller+" extends BaseController{");
        sb.append(LINE).append(TAB);
        sb.append("@Autowired");
        sb.append(LINE).append(TAB);
        
        sb.append("private "+table.getBaseName()+TableModel.Service+"  "+serviceName+";").append(LINE);
        
        
        sb.append("	@RequestMapping(value = \"listPage\")").append(LINE);
        sb.append("	public ModelAndView listPage(Page page)throws Exception{").append(LINE);
        sb.append("		ModelAndView mv = this.getModelAndView();").append(LINE);
        sb.append("		PageData pd = this.getPageData();").append(LINE);
        sb.append("		page.setPd(pd);").append(LINE);
        
        
        sb.append("		List<PageData>	varList="+serviceName+".getByPage(page);").append(LINE);
        sb.append("		mv.addObject(\"varList\", varList);").append(LINE);
        sb.append("		mv.addObject(\"pd\", pd);").append(LINE);
        sb.append("		mv.addObject(\"page\", page);").append(LINE);
        sb.append("		return mv;").append(LINE);

        sb.append("	}").append(LINE);
        
        sb.append("	@RequestMapping(value = \"edit\")").append(LINE);
        sb.append("	public ModelAndView edit() {").append(LINE);
        sb.append("		ModelAndView mv = this.getModelAndView();").append(LINE);
        sb.append("		String id=getRequest().getParameter(\"id\");").append(LINE);
        sb.append("		mv.addObject(\"dataModel\", "+serviceName+".getById(EAString.stringToInt(id, 0)));").append(LINE);
        sb.append("		return mv;").append(LINE);
        sb.append("	}").append(LINE);
        sb.append("	@RequestMapping(value = \"save\")").append(LINE);
        sb.append("	public ModelAndView save() {").append(LINE);
        sb.append("		ModelAndView mv = this.getModelAndView();").append(LINE);
        sb.append("		PageData data=this.getPageData();").append(LINE);
       // sb.append("		data.put(\"creator\", getAdminUserName());").append(LINE);
        sb.append("		data.put(\"create_time\", EADate.getCurrentTime());").append(LINE);
        //sb.append("		"+serviceName+".save(data);").append(LINE);
        sb.append("		if(data==null||data.isEmpty()){").append(LINE);
        sb.append("			mv.setViewName(\"common/error\");").append(LINE);
        sb.append("		}").append(LINE);
        sb.append("		mv.setViewName(\"redirect:/sys/"+table.getModuleName()+"/"+table.getLowBaseName()+"/listPage\");").append(LINE);
        sb.append("		return mv;").append(LINE);
        sb.append("	}").append(LINE);
        
        sb.append(LINE);
        sb.append("}");
        return sb.toString();
    }
}
