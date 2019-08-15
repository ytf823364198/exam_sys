package com.ziyue.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.ExampleEnter;


@Repository
public class ExampleEnterDao  {
	@Autowired
    public JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExampleEnter findEnterById(String id) {
		try{
			String sql = "select * from ex_jasper_enter where id= ? ";
			return  (ExampleEnter) jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(ExampleEnter.class));
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ExampleEnter> findEnters() {
		String sql ="select * from ex_jasper_enter  order by code desc ";
		return (List<ExampleEnter>)jdbcTemplate.query(sql, new BeanPropertyRowMapper(ExampleEnter.class));
	}

}
