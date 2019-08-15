package com.ziyue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ziyue.dao.BaseMenuDao;
import com.ziyue.entity.BaseMenu;

@Service
public class BaseMenuService{
	@Autowired
	private BaseMenuDao baseMenuDao;

	public List<BaseMenu> findTopMenuByUserId(String userId,String pid){
		return baseMenuDao.findTopMenuByUserId(userId, pid);
	}

	public List<BaseMenu> findMenuRemindByUserId(String userId){
		return baseMenuDao.findMenuRemindByUserId(userId);
	}

	public List<BaseMenu> findMenuByUserId(String userid,String pid){
		return baseMenuDao.findMenuByUserId(userid,pid);
	}
	
	public List<BaseMenu> findMenuByUserId(String userid){
		return baseMenuDao.findMenuByUserId(userid);
	}
	
	/**
	 * 查用户所有的权限号
	 * @param  id 用户ID 
	 * @return List<String> 权限号集合
	 */
	public List<String> findMenuPermcodeByUserId(String id) {
		return baseMenuDao.findMenuPermcodeByUserId(id);
	}
	
	public List<BaseMenu> findMenuByRoleId(String roleid){
		return baseMenuDao.findMenuByRoleId(roleid);
	}

	public List<BaseMenu> findMenus(){
		return baseMenuDao.findMenus();
	}

	public BaseMenu findMenuById(String id){
		return baseMenuDao.findMenuById(id);
	}



}
