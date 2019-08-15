package com.ziyue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ziyue.dao.BaseMenuDao;
import com.ziyue.dao.BaseRoleDao;
import com.ziyue.dao.BaseUserDao;
import com.ziyue.entity.BaseMenu;
import com.ziyue.entity.BaseRole;
import com.ziyue.shiro.CustomRealm;
@Service
public class BaseRoleService{
	@Autowired
	private BaseRoleDao baseRoleDao;
	@Autowired
	private BaseMenuDao baseMenuDao;
	@Autowired
	private BaseUserDao baseUserDao;;
	//注入realm
	@Autowired
	private CustomRealm customRealm;
	
	@Transactional
	public void addRole(BaseRole role) {
		String roleId = baseRoleDao.addRole(role);
		List<BaseMenu> menus = role.getMenus();
		if(null != menus && menus.size() > 0 ){
			for(BaseMenu menu : menus ){
				baseMenuDao.addRoleMenu(roleId, menu.getId());
			}
		}
	}

	@Transactional
	public void delRoleById(String id) {
		baseRoleDao.delRoleById(id);
		baseUserDao.delUserRoleByRoleId(id);
		baseMenuDao.delMenuByRole(id);
		//清除缓存
		customRealm.clearCached();
	}

	@Transactional
	public void modifyRole(BaseRole role) {
		baseRoleDao.modifyRole(role);
		baseMenuDao.delMenuByRole(role.getId());
		List<BaseMenu> menus = role.getMenus();
		if(null != menus && menus.size() > 0 ){
			for(BaseMenu menu : menus ){
				baseMenuDao.addRoleMenu(role.getId(), menu.getId());
			}
		}
		//清除缓存
		customRealm.clearCached();
	}

	
	public BaseRole findRoleById(String id) {
		return baseRoleDao.findRoleById(id);
	}

	
	public List<BaseRole> findRoles() {
		return baseRoleDao.findRoles();
	}

	
	public void delMenuByRole(String roleId){
		baseMenuDao.delMenuByRole(roleId);
		//清除缓存
		customRealm.clearCached();
	}
	
	
}
