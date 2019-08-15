package com.ziyue.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ziyue.dao.BaseRoleDao;
import com.ziyue.dao.BaseUserDao;
import com.ziyue.entity.BaseOrgan;
import com.ziyue.entity.BaseRole;
import com.ziyue.entity.BaseUser;
import com.ziyue.entity.BaseUserOrgan;
import com.ziyue.entity.BaseUserRole;
//import com.ziyue.shiro.CustomRealm;
import com.ziyue.util.PageModel;

@Service
public class  BaseUserService{
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private BaseRoleDao baseRoleDao;
	//注入realm
	//@Autowired
	//private CustomRealm customRealm;

	@Transactional
	public void addUser(BaseUser user){
		//添加用户
		baseUserDao.addUser(user);
		//添加用户部门
		List<BaseUserOrgan> userOrgans = user.getUorgans();
		if(null != userOrgans && userOrgans.size()>0 ){
			for(BaseUserOrgan userOrgan : userOrgans){
				baseUserDao.addUserOrgan(userOrgan);
			}
		}
		//添加用户角色
		List<BaseUserRole> userRoles = user.getRoles();
		if(null != userRoles && userRoles.size()>0 ){
			for(BaseUserRole userRole : userRoles){
				baseUserDao.addUserRole(userRole);
			}
		}
	}
	
	public BaseUser  findUserBaseById(String id){
		return baseUserDao.findUserById(id);// 可能返回null 
	}
	
	public BaseUser  findUserById(String id){
		BaseUser user = baseUserDao.findUserById(id);// 可能返回null 
		if(user != null){
			user.setRoles(baseUserDao.findUserRoleByUserId(id));
			user.setPorgans(baseUserDao.findUserOrgan(user.getId()));
		}
		return user;
	}
	
	@Transactional
	public void modifyUser(BaseUser user){
		baseUserDao.modifyUser(user);
		
		baseUserDao.delUserOrganByUserId(user.getId());
		List<BaseUserOrgan> userOrgans = user.getUorgans();
		if(null != userOrgans && userOrgans.size()>0 ){
			for(BaseUserOrgan userOrgan : userOrgans){
				baseUserDao.addUserOrgan(userOrgan);
			}
		}
		
		baseUserDao.delUserRoleByUserId(user.getId());
		List<BaseUserRole> userRoles = user.getRoles();
		if(null != userRoles && userRoles.size()>0 ){
			for(BaseUserRole userRole : userRoles){
				baseUserDao.addUserRole(userRole);
			}
		}
		//清除缓存
		//customRealm.clearCached();
		
	}
	
	public void addUserRole(BaseUserRole userRole){
		baseUserDao.addUserRole(userRole);
		//清除缓存
		//customRealm.clearCached();
	}

	public void delUserRoleByUserId(String userid){
		baseUserDao.delUserRoleByUserId(userid);
		//清除缓存
		//customRealm.clearCached();
	}

	public void delUserRole(String userid,String roleid){
		baseUserDao.delUserRole(userid, roleid);
		//清除缓存
		//customRealm.clearCached();
	}
	
	public void delUserById(String id) {
		baseUserDao.delUserById(id);
		baseUserDao.delUserRoleByUserId(id);
		baseUserDao.delUserOrganByUserId(id);
	}
	
	
	public List<BaseUserRole> findUserRoleByUserId(String userid){
		return baseUserDao.findUserRoleByUserId(userid);
	}
	
	public List<String> findUserIdByRoleId(String roleId){
		return baseUserDao.findUserIdByRoleId(roleId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BaseUser> findUserByCols(Map cols){
		return baseUserDao.findUserByCols(cols);
	}
	
	public List<BaseUser> findUserLimitByCols(Map<String,String> cols){
		return baseUserDao.findUserLimitByCols(cols);
	}
	
	public BaseUser  findUserByNamePassword(String username,String password){
		BaseUser user = baseUserDao.findUserByNamePassword(username, password);
		if(null != user && !"".equals(user.getId())){
			user.setRoles(baseUserDao.findUserRoleByUserId(user.getId()));
			user.setPorgans(baseUserDao.findUserOrgan(user.getId()));
			if(null != user.getTutorid() && !"".equals(user.getTutorid())){
				user.setTutor(baseUserDao.findRealNameById(user.getTutorid()));
			}
		}		
		return user;
	}
	
	public List<BaseUser> findUserByOrganid(String organid){
		return baseUserDao.findUserByOrganid(organid);
	}
	
	public String findNameById(String id){
		return baseUserDao.findRealNameById(id);
	}
	
	public List<BaseRole> findRoleNamesById(String id){
		List<BaseUserRole> userRoles = baseUserDao.findUserRoleByUserId(id);//得到Roleids
		
		List<BaseRole> roles =new  ArrayList<BaseRole>();
		if(null != userRoles && userRoles.size()>0){
			for(BaseUserRole userRole:userRoles){
				String roleid = userRole.getRoleid();
				BaseRole role = baseRoleDao.findRoleById(roleid);
				roles.add(role);
			}
		}
		return roles;
	
	}
	
	public void modifySelfInfo(BaseUser user){
		Map<String,Object> cols = new HashMap<String,Object>();
		cols.put("telphone", user.getTelphone());
		cols.put("email", user.getEmail());
		cols.put("mobile", user.getMobile());
		cols.put("putms", user.getPutms());
		cols.put("putmail", user.getPutmail());
		baseUserDao.modifyUserByCols(cols, user.getId());
	}
	
	public void modifyPassword(String id,String newpassword){
		Map<String,Object> cols = new HashMap<String,Object>();
		cols.put("password", newpassword);
		baseUserDao.modifyUserByCols(cols, id);
	}
	
	public List<BaseOrgan> findUserOrgan(String userid){
		return baseUserDao.findUserOrgan(userid);
	}
	
	public PageModel findBaseSplitPage(PageModel pageModel){
		return baseUserDao.findBaseSplitPage(pageModel);
	}

	public List<BaseUser> findUserByRoleId(String roleid) {
		return baseUserDao.findUserByRoleId(roleid);
	}
	
	//通过角色Id查唯一的用户
	public BaseUser findOnlyUserByRoleId(String roleid){
		List<BaseUser> users = baseUserDao.findUserByRoleId(roleid);
		if(null != users && users.size() >0){
			return users.get(0);
		}
		return null;
	}
	
	public List<BaseUserOrgan> findUserOrganByUserId(String userid){
		return baseUserDao.findUserOrganByUserId(userid);
	}
	
	public List<BaseUser> findUserByRealName(String realName){
		if(null != realName && !"".equals(realName)){
			return baseUserDao.findUserByRealName(realName.trim());
		}
		return null;
	}
	
	public int findUserIsExit(String code,String excludeId) {
		return baseUserDao.findUserIsExit(code, excludeId);
	}
}
