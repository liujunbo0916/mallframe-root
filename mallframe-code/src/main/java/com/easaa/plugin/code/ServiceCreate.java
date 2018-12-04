package com.easaa.plugin.code;

public class ServiceCreate {

	private static final String LINE = "\r\n";
    private static final String TAB = "\t";
    StringBuffer sb = new StringBuffer();
    private TableModel table;
    public ServiceCreate(TableModel table)throws Exception{
		this.table=table;
    }
	public String doCreate() throws Exception {
		 sb.append("package com.easaa."+table.getModuleName()+".service;");
        sb.append(LINE);
        
        sb.append("import org.springframework.beans.factory.annotation.Autowired;").append(LINE);
        sb.append("import org.springframework.stereotype.Service;").append(LINE);
        sb.append("import com.easaa.core.model.dao.EADao;").append(LINE);
        sb.append("import com.easaa.core.model.service.impl.EABaseService;").append(LINE);
        sb.append("import com.easaa."+table.getModuleName()+".dao."+table.getBaseName()+"Mapper;").append(LINE);
        
        
        sb.append("@Service").append(LINE);
        sb.append("public class "+table.getBaseName()+TableModel.Service+" extends EABaseService{");
        sb.append(LINE).append(TAB);
        sb.append("@Autowired");
        sb.append(LINE).append(TAB);
        String daoName=table.getBaseName()+TableModel.Mapper;
        sb.append("private "+daoName+" "+table.getLowBaseName()+TableModel.Mapper+";");
        
        sb.append(LINE).append(TAB);
        sb.append("@Override");
        sb.append(LINE).append(TAB);
        sb.append("public EADao getDao(){");
        sb.append(LINE).append(TAB).append(TAB);
        sb.append("return "+daoName+";");
        sb.append(LINE).append(TAB);
        sb.append("}");
        sb.append(LINE);
        sb.append("}");
        return sb.toString();
    }
}
