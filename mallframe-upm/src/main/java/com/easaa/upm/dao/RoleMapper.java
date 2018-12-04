package com.easaa.upm.dao;

import java.util.List;

import com.easaa.core.model.dao.EADao;
import com.easaa.entity.PageData;
import com.easaa.upm.entity.Role;

public interface RoleMapper extends EADao{
	
	public List<Role> listAllRolesByPId(PageData pd);
	
	public void deleteRoleById(String  roleId);
	
	public void updateRoleRights(Role role);
	
	public void setAllRights(PageData pd);

	public void add_qx(PageData pd);
	
	public void del_qx(PageData pd);
	
	public void edit_qx(PageData pd);
	
	public void cha_qx(PageData pd);
	
	public Role getRoleById(String id);
	/**
	 * 查询系统管理员组角色
	 */
	public  List<Role> getSysRole();
	
}
