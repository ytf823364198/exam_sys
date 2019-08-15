package com.ziyue.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseOrgan;
import com.ziyue.util.BaseDao;

@Repository
public class  BaseOrganDao extends BaseDao{
	public void addOrgan(BaseOrgan organ){
		String sql = "insert into base_organ(id,organname,pid) values(?,?,?)";
		Object args[] = new Object[]{organ.getId(),organ.getOrganname(),organ.getPid()};
		jdbcTemplate.update(sql, args);
	}
	
	public void delOrganById(String id){
		String sql = "delete from  base_organ where id =? " ;
		jdbcTemplate.update(sql, new Object[]{id});
	}
	
	public void modifyOrgan(BaseOrgan organ){
		String sql = "update base_organ set  organname=?,pid=? where id = ?";
		jdbcTemplate.update(sql,new Object[]{organ.getOrganname(),organ.getPid(),organ.getId()});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseOrgan findOrganById(String id){
		try {
			String sql = " select * from base_organ  where id = ? ";
			return (BaseOrgan)jdbcTemplate.queryForObject(sql, new Object[]{id},new BeanPropertyRowMapper(BaseOrgan.class));
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	public String findOrganNameById(String id){
		if(null != id && !"".equals(id )){
			try {
				String sql = " select organname from base_organ  where id = ? ";
				return (String)jdbcTemplate.queryForObject(sql, new Object[]{id},String.class);
			} catch (Exception e) {
				return "";
			}	
		}
		return "";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseOrgan> findOrgans(){
		String sql = " select * from base_organ order by id";
		return  (List<BaseOrgan>)jdbcTemplate.query(sql,new BeanPropertyRowMapper(BaseOrgan.class));
	}
	
	//查所有的大部门
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseOrgan> findPOrgans(){
		String sql = " select * from base_organ where pid ='0' order by id";
		return  (List<BaseOrgan>)jdbcTemplate.query(sql,new BeanPropertyRowMapper(BaseOrgan.class));
	}
	
	//根据大部门id，查大部门下的所有小部门
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseOrgan> findOrgansByPid(String pid){
		String sql = "select * from base_organ where pid=? order by id  ";
		return (List<BaseOrgan>)jdbcTemplate.query(sql,new Object[]{pid},new BeanPropertyRowMapper(BaseOrgan.class));
	}
	
	public int countOrgansByPid(String pid){//查下属部门数量
		String sql = "select count(*) from base_organ where pid=? ";
		return (Integer)jdbcTemplate.queryForObject(sql,new Object[]{pid},Integer.class);
	}
	
	//查部门编号(code)是否存在,排除ID 为 excludeId的部门
	public int findCodeIsExit(String code,String excludeId){
		String sql = "select count(*) from base_organ  where id=? ";
		Object[] args = {code};
		if(null != excludeId && !"".equals(excludeId)){
			sql = sql + " and id <> ?";
			args = new Object[]{code,excludeId};
		}		
		return jdbcTemplate.queryForObject(sql, args, Integer.class);
	}

}
