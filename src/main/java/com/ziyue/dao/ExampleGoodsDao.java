package com.ziyue.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.ExampleGoods;


@Repository
public class ExampleGoodsDao {
	@Autowired
    public JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExampleGoods findGoodsById(String id) {
		try{
			String sql = "select * from ex_jasper_goods where id= ? ";
			return  (ExampleGoods)jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(ExampleGoods.class));
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ExampleGoods> findGoods() {
		String sql ="select * from ex_jasper_goods  order by code desc ";
		return (List<ExampleGoods>)jdbcTemplate.query(sql, new BeanPropertyRowMapper(ExampleGoods.class));
	
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ExampleGoods> findGoodsByEnterId(String enterId) {
		String sql ="select * from ex_jasper_goods where enterid =? order by code desc ";
		return (List<ExampleGoods>)jdbcTemplate.query(sql,new Object[]{enterId}, new BeanPropertyRowMapper(ExampleGoods.class));
	}

	public int findGoodsCountByEnterId(String enterId) {
		String sql = "select count(*)  from ex_jasper_goods where enterid =? ";
		try{
			return (Integer)jdbcTemplate.queryForObject(sql,new Object[]{enterId},Integer.class);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		
	}

}
