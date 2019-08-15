package com.ziyue.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseClob;
import com.ziyue.util.BaseDao;



/***
 * 大字段
 * @author 胡永强
 * @since 2015/07/15
 * @version 1.0.1
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class BaseClobDao  extends BaseDao{

	public void addClob(BaseClob clob) {
		String sql="insert into base_clob(objectid,attrkey,attrval)values(?,?,?,?)";
		Object[]args={clob.getObjectid(),clob.getAttrkey(),clob.getAttrval()};
		jdbcTemplate.update(sql,args);
	}
	
	public void delClobByObjectId(String objectid) {
		String sql="delete from  base_clob where objectid =? ";
		jdbcTemplate.update(sql, new Object[]{objectid});
	}
	
	public void delClobByObjectIdAttrKey(String  objectid,String attrkey) {
		String sql="delete from  base_clob where objectid =?  and attrkey =?";
		jdbcTemplate.update(sql, new Object[]{objectid,attrkey});
	}

	public List<BaseClob> findClobByObjectId(String  objectid){
		try{
			String sql ="select * from base_clob where objectid =? ";
			return  (List<BaseClob>)jdbcTemplate.query(sql,new Object[]{objectid},new BeanPropertyRowMapper(BaseClob.class));
		}catch(Exception e){
			return null;
		}
	}
	
	public String findClobByObjectIdAttrKey(String  objectid,String attrkey) {
		try{
			String sql ="select attrval from base_clob where objectid =?  and attrkey =? ";
			return (String)jdbcTemplate.queryForObject(sql, new Object[]{objectid,attrkey}, String.class);
		}catch(Exception e){
			return null;
		}
	}

}
