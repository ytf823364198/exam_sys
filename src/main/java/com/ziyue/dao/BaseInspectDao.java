package com.ziyue.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseInspect;
import com.ziyue.util.BaseDao;
import com.ziyue.util.StringUtil;

/***
 * 操作单据审批流程数据库接口实现
 * 继承操作数据库的基类BaseDao
 * @author 胡永强
 * @since 2018/08/04
 * @version 1.0.1
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public  class BaseInspectDao extends BaseDao{

	public void addInspect(BaseInspect ins){
		String sql = "insert into base_inspect(objectid,userid,realname,result,info,opttime,flag,taskdefkey,taskname,isbpm)";
		Object[]args={ins.getObjectid(),ins.getUserid(),ins.getRealname(),ins.getResult(),ins.getInfo(),ins.getOpttime(),ins.getFlag(),ins.getTaskdefkey(),ins.getTaskname(),ins.getIsbpm()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);		
	}
	
    public void delInspectByObjectId(String objectid,String flag){
    	String sql = "delete from base_inspect where objectid = ? and  flag = ? ";
	    jdbcTemplate.update(sql,new Object[]{objectid,flag});
    }
    
	public void delInspectByObjectId(String objectid) {
    	String sql = "delete from base_inspect where objectid = ?  ";
	    jdbcTemplate.update(sql,new Object[]{objectid});
	}
    
    public List<BaseInspect> findInspectByObjectId(String objectid){
    	String sql= "select * from base_inspect t where t.objectid =?  order by t.opttime  ";
		List <BaseInspect> inspects = (List<BaseInspect>)jdbcTemplate.query(sql, new Object[]{objectid},new BeanPropertyRowMapper(BaseInspect.class));
		return inspects;
    }
    
	public List<BaseInspect> findInspectByObjectId(String objectid,String flag){
    	String sql= "select * from base_inspect t where t.objectid =? and t.flag =? order by t.opttime desc  ";
		return (List <BaseInspect>) jdbcTemplate.query(	sql, new Object[]{objectid,flag},new BeanPropertyRowMapper(BaseInspect.class));
    }

	

}
