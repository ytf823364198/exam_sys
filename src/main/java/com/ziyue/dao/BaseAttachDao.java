package com.ziyue.dao;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseAttach;
import com.ziyue.util.BaseDao;
import com.ziyue.util.StringUtil;

@Repository
public class BaseAttachDao extends BaseDao{

	public String addAttach(BaseAttach attach) {
		String sql="insert into base_attach(id,path,name,format,length,filesize,flag,objectid,md5,uploadtime)";
		String id = StringUtil.UUID();
		Object[] args = new Object[]{
					id,attach.getPath(),attach.getName(),attach.getFormat(),
					attach.getLength(),attach.getFilesize(),attach.getFlag(),attach.getObjectid(),attach.getMd5(),attach.getUploadtime()
				};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
		return id;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseAttach findAttachById(String id) {
		try{
			String sql= "select * from base_attach where id=? ";
			BaseAttach attach= (BaseAttach) jdbcTemplate.queryForObject(sql,new Object[]{id}, new BeanPropertyRowMapper(BaseAttach.class));	
			return attach;
		}catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseAttach> findAttachsByObjectId(String objectId){
		try{
			String sql="select * from base_attach where objectid = ? order by uploadtime desc ";
			return (List<BaseAttach>)jdbcTemplate.query(sql, new Object[]{objectId},
				new BeanPropertyRowMapper(BaseAttach.class));
		}catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseAttach> findAttachByObjectFlag(String objectId,String flag){
		try{
			String sql="select * from base_attach where objectid = ? and flag =?  order by uploadtime desc ";
			return (List<BaseAttach>)jdbcTemplate.query(sql, new Object[]{objectId,flag},
				new BeanPropertyRowMapper(BaseAttach.class));
		}catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseAttach> findAttachByObjectFormat(String objectId,String format){
		try{
			String sql="select * from base_attach where objectid = ? and format =?  order by uploadtime desc ";
			return (List<BaseAttach>)jdbcTemplate.query(sql, new Object[]{objectId,format},
				new BeanPropertyRowMapper(BaseAttach.class));
		}catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseAttach> findAttachByObject(String objectId,String flag,String format){
		try{
			String sql="select * from base_attach where objectid = ? and flag =? and format =? order by uploadtime desc ";
			return (List<BaseAttach>)jdbcTemplate.query(sql, new Object[]{objectId,flag,format},
				new BeanPropertyRowMapper(BaseAttach.class));
		}catch(Exception e){
			return null;
		}
	}

	public void delAttachById(String id){
		String sql = "delete from base_attach where  id = ? ";
	    jdbcTemplate.update(sql,new Object[]{id});
	}

	
	public void delAttachByObjectId(String objectId){
		String sql = "delete from base_attach where  objectid = ? ";
	    jdbcTemplate.update(sql,new Object[]{objectId});
	}
	
	
	public void delAttachByObject(String objectid,String flag){
		String sql = "delete from base_attach where  objectid = ?  and flag =? ";
	    jdbcTemplate.update(sql,new Object[]{objectid,flag});
	}
	
	public void delAttachByObjectIdAndIds(String objectid,List<String> ids){
		String sql = "delete from base_attach where  objectid = ?  and id in ("+ StringUtil.sqlListToInQuery(ids)+") ";
	    jdbcTemplate.update(sql,new Object[]{objectid});
	}
	
	public void delAttachByObjectIdExclusiveIds(String objectid,List<String> ids){
		String sql = "delete from base_attach where  objectid = ?  and id not in ("+ StringUtil.sqlListToInQuery(ids)+") ";
	    jdbcTemplate.update(sql,new Object[]{objectid});
	}
	

}
