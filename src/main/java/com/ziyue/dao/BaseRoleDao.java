package com.ziyue.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseRole;
import com.ziyue.util.BaseDao;
@Repository
public class BaseRoleDao extends BaseDao{
	public String addRole(BaseRole role){
		String sql = "insert into base_role(id,rolename,inlay,organ,sort) values(?,?,?,?,?)";
		Object args[] = new Object[]{role.getId(),role.getRolename(),role.getInlay(),role.getOrgan(),role.getSort()};
		jdbcTemplate.update(sql, args);
		return role.getId();
	}
	
	public void delRoleById(String id){
		String sql = "delete from  base_role where id =? ";
		Object args[] = new Object[]{id};
		jdbcTemplate.update(sql, args);
	}
	
	public void modifyRole(BaseRole role){
		String sql="update  base_role set rolename=? where id =? ";
		Object args[] = new Object[]{role.getRolename(),role.getId()};
		jdbcTemplate.update(sql, args);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseRole findRoleById(String id){
		try{
			String sql = "select * from base_role where id = ? ";
			return (BaseRole)jdbcTemplate.queryForObject(sql, new Object[]{id},new BeanPropertyRowMapper(BaseRole.class));
		}catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseRole> findRoles(){
		String sql = "select * from base_role order by sort";
		return (List<BaseRole>)jdbcTemplate.query(sql,new BeanPropertyRowMapper(BaseRole.class));
	}

}
