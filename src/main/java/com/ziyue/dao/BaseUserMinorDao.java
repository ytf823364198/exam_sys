package com.ziyue.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseUser;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseUserMinorDao {
	@Autowired
	@Qualifier("minorJdbcTemplate")
    public JdbcTemplate jdbcTemplate;
	
	public BaseUser findUserById(String id){
		try {
			String sql = "select * from base_user where id = ? ";
			return (BaseUser)jdbcTemplate.queryForObject(sql,new Object[]{id}, new BeanPropertyRowMapper(BaseUser.class));
		}catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
		
    }

	

	public void modifyUser(String realname,String id) {
		String sql="update base_user  set realname=?  where id =? ";
		Object args[] = new Object[]{realname,id};
		jdbcTemplate.update(sql, args);
	}

	
	
}
