package com.easaa.plugin.code;

public class DaoCreate {

	private static final String LINE = "\r\n";
    StringBuffer sb = new StringBuffer();
    private TableModel table;
	public DaoCreate(TableModel table)throws Exception{
		this.table=table;
    }
	public String doCreate() throws Exception {
        sb.append("package com.easaa."+table.getModuleName()+".dao;");
        
        sb.append(LINE);
        sb.append("import com.easaa.core.model.dao.EADao;");
        sb.append(LINE);
        sb.append("public interface "+table.getBaseName()+TableModel.Mapper+" extends EADao{");
        
        sb.append(LINE);
        sb.append("}");
     
        return sb.toString();
    }
}
