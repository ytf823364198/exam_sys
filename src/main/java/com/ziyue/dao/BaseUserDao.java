package com.ziyue.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseOrgan;
import com.ziyue.entity.BaseUser;
import com.ziyue.entity.BaseUserOrgan;
import com.ziyue.entity.BaseUserRole;
import com.ziyue.util.BaseDao;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;


@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseUserDao extends BaseDao{

	public BaseUser findUserById(String id){
		try {
			String sql = "select * from base_user where id = ? ";
			return (BaseUser)jdbcTemplate.queryForObject(sql,new Object[]{id}, new BeanPropertyRowMapper(BaseUser.class));
		}catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
    }

	public void addUser(BaseUser user) {
		String sql="insert into base_user(id,username,password,realname,telphone,mobile,email,putmail,putms,type,tutorid,status) ";
		Object[] args = new Object[]{user.getUsername(),user.getUsername(),user.getPassword(),user.getRealname(),
				user.getTelphone(),user.getMobile(),user.getEmail(),user.getPutmail(),
				user.getPutms(),user.getType(),user.getTutorid(),user.getStatus()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
	}
	
	

	public void modifyUser(BaseUser user) {
		String sql="update base_user  set realname=?,password=?,telphone=?,mobile=?,email=?,putmail=?,putms=?,type=?,tutorid=?,status=? where id =? ";
		Object args[] = new Object[]{user.getRealname(),user.getPassword(),user.getTelphone(),user.getMobile(),user.getEmail(),
				user.getPutmail(),user.getPutms(),user.getType(),user.getTutorid(),user.getStatus(),user.getId()};
		jdbcTemplate.update(sql, args);
		
	}

	public void delUserById(String id) {
		String sql = "delete from base_user where id =? ";
		jdbcTemplate.update(sql,new Object[]{id});
	}
	
	public List<BaseUser> findUserByCols(Map<String,String> cols){
		String sqls=" select * from base_user  where 1=1  ";
		List<String> parmlist = new ArrayList<String>();          //查询参数
		String sql = this.joinCondition(parmlist, cols);
		sqls = sqls +sql +" order by realname";
		return (List<BaseUser>)this.queryForList(sql,parmlist, BaseUser.class);
	}
	
	public List<BaseUser> findUserLimitByCols(Map<String,String> cols){
		String sqls=" select * from base_user  where 1=1  ";
		List<String> parmlist = new ArrayList<String>();          //查询参数
		String sql = this.joinCondition(parmlist, cols);
		sqls = sqls +sql +" order by realname";
		return (List<BaseUser>)this.queryForList(sql, parmlist, 10, 1, BaseUser.class);	
	}
	
	public PageModel findBaseSplitPage(PageModel pageModel){
		//定义统计总记录数的SQL
		String sqlCount="select count(*) from base_user u where 1=1 ";
		//定义查询结果集的SQL
		String sqlQuery="select u.* from base_user u where 1=1 ";
		//拼接查询条件
	    String sql = this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()); 
		sqlCount = sqlCount + sql;
		sqlQuery = sqlQuery + sql + " order by u.realname asc ";
		pageModel.setQuery(sqlCount, sqlQuery, null, null);
		return super.queryForPageModel(pageModel);
	}

	public void modifyUser(String mobilephone,String email,String id) {
		String sql="update base_user  set mobilephone=?,email=? where id =? ";
		Object args[]=new Object[]{mobilephone,email,id};
		jdbcTemplate.update(sql, args);
		
	}
	
	public  void addUserRole(BaseUserRole userRole){
		String sql="insert into base_user_role(userid,roleid) values (?,?) ";
		Object[] args= new Object[]{userRole.getUserid(),userRole.getRoleid()};
		jdbcTemplate.update(sql, args);
	}
	
	public void delUserRole(String userid,String roleid){
		String sql="delete from  base_user_role  where userid =? and roleid =? ";
		jdbcTemplate.update(sql, new Object[]{userid,roleid});
	}
	
	public void delOrganUserRole(String organid,String roleid){
		String sql="delete from  base_user_role  where userid in (select ioa.userid from (select uo.userid from base_user_organ uo where uo.organid=?  )  ioa ) and roleid =? ";
		jdbcTemplate.update(sql, new Object[]{organid,roleid});
	}
	
	public void delUserRoleByUserId(String userid){
		String sql="delete from  base_user_role  where userid =? ";
		jdbcTemplate.update(sql, new Object[]{userid});
	}
	
	public void delUserRoleByRoleId(String roleid){
		String sql="delete from  base_user_role  where roleid =? ";
		jdbcTemplate.update(sql, new Object[]{roleid});
	}
	
	public void delUserRoleNonOrganByUserId(String userid){
		String sql="delete from  base_user_role  where userid =? and roleid not in (select ioa.id from (select id from base_role where organ ='y')  ioa ) ";
		jdbcTemplate.update(sql, new Object[]{userid});
	}
	
	public List<String> findUserIdByRoleId(String roleId) {
		try{
			String sql = "select distinct userid from base_user_role  where roleid =? ";
			return (List<String>)jdbcTemplate.queryForList(sql,new Object[]{roleId},String.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<BaseUserRole> findUserRoleByUserId(String userid){
		String sql="select * from base_user_role  where userid =?  order by roleid  ";
		List<BaseUserRole> roles = (List<BaseUserRole>)jdbcTemplate.query(sql, new Object[]{userid},new BeanPropertyRowMapper(BaseUserRole.class));
	    return roles;
	}
	

	public String findRealNameById(String id){
		if(null != id && !"".equals(id.trim()) ){
			try{
				String sql=" select realname from base_user where id =? ";
				return (String)jdbcTemplate.queryForObject(sql, new Object[]{id},String.class);
			}catch(Exception e){
				return null;
			}
		}
		return "";
		
	}
	

	public BaseUser  findUserByNamePassword(String username,String password){
		String sql="select u.* from base_user u  where u.username =? and u.password =?  ";
		List<BaseUser> users = (List<BaseUser>)jdbcTemplate.query(sql, new Object[]{username,password},new BeanPropertyRowMapper(BaseUser.class));
	    if(null != users && users.size()>0){
	    	return users.get(0);
	    }
	    return null;
	}
	
	public List<BaseUser> findUserByOrganid(String organid){
		String sql="select * from base_user   where  id in (select ioa.userid from (select distinct(userid) from base_user_organ where organid  = ? )  ioa) and type='js'  order by realname  ";
		List<BaseUser> users = (List<BaseUser>)jdbcTemplate.query(sql, new Object[]{organid},new BeanPropertyRowMapper(BaseUser.class));
	    return users;
	}
	
	public void modifyUserByCols(Map<String, Object> cols, String id) {
		String sql = "";
		if( null != cols && cols.size()>0){
			List <Object> parmlist = new ArrayList<Object>();
			int i = 1;
			for(String key : cols.keySet()){
					if(i ==  cols.size() ){
						sql = sql + key+" = ? ";
					}else{
						sql = sql + key+" = ? ,";
					}
					parmlist.add(cols.get(key));
					i =  i + 1 ;
			}
			sql = "update base_user set "+ sql +" where  id = ? ";
			parmlist.add( id );
			jdbcTemplate.update(sql,parmlist.toArray());
		}	
	}
	
	//根据角色id 查找用户
	public List<BaseUser> findUserByRoleId(String id) {
		String sql = "select id,username,realname from base_user where id in (select ioa.userid from (select distinct(userid) from base_user_role where roleid = ? )  ioa)  order by realname";
		try{
			return (List<BaseUser>)jdbcTemplate.query(sql,new Object[]{id},new BeanPropertyRowMapper(BaseUser.class));
		}catch(Exception e){
			return null;
		}
	}

	public List<BaseOrgan> findUserOrgan(String userid){
		String sql = "select * from base_organ where id in (select ioa.organid from (select organid from base_user_organ where userid =? ) ioa) order by id ";
		return (List<BaseOrgan>)jdbcTemplate.query(sql,new Object[]{userid},new BeanPropertyRowMapper(BaseOrgan.class));
	}
	
	public void addUserOrgan(BaseUserOrgan userorgan){
		String sql="insert into base_user_organ(userid,organid) values(?,?)";
		jdbcTemplate.update(sql, new Object[]{userorgan.getUserid(),userorgan.getOrganid()});
	}
	
	public void delUserOrganByUserId(String userid){
		String sql = "delete from base_user_organ where userid =? ";
		jdbcTemplate.update(sql, new Object[]{userid});
	}
	
	public BaseUserOrgan findUserOrgan(String userid,String organid){
		try{
			String sql = "select * from base_user_organ where userid =? and organid=? ";
			List<BaseUserOrgan> uos = jdbcTemplate.query(sql, new Object[]{userid,organid},new BeanPropertyRowMapper(BaseUserOrgan.class));
			if(null != uos && uos.size()>0){
				return uos.get(0);
			}
			return null;
		}catch(Exception e){
			return null;		
		}
	}
	
	public List<BaseUser> findUserByRealName(String realName){
		String sql = "select id,username,realname from base_user where realname =? ";
		return jdbcTemplate.query(sql, new Object[]{realName},new BeanPropertyRowMapper(BaseUser.class));
	}

	public List<BaseUserOrgan> findUserOrganByUserId(String userid){
		try{
			String sql = "select * from base_user_organ where userid =? ";
			return jdbcTemplate.query(sql, new Object[]{userid},new BeanPropertyRowMapper(BaseUserOrgan.class));
		}catch(Exception e){
			return null;		
		}
	
	}
	
	public void modifyUser(String realname,String id) {
		String sql="update base_user  set realname=?  where id =? ";
		Object args[] = new Object[]{realname,id};
		jdbcTemplate.update(sql, args);
	}
	
	
	//查用户是否存在,排除ID 为 excludeId的用户
	public int findUserIsExit(String code,String excludeId){
		String sql = "select count(*) from base_user  where id=? ";
		Object[] args = {code};
		if(null != excludeId && !"".equals(excludeId)){
			sql = sql + " and id <> ?";
			args = new Object[]{code,excludeId};
		}		
		return jdbcTemplate.queryForObject(sql, args, Integer.class);
	}
	

	/**
	 * 拼接条件查询，数据库定义的字符串长度>16用like拼接，<=16用“=”拼接（id除外）
	 * @param List parmlist 参数附值后的结合
	 * @param Map<String, String>search 查询的条件，key是要查询的字段，value查询的值
	 * @return String 拼接查询条件的值
	 */
	private String joinCondition(List parmlist,Map<String, String> search) {
		StringBuffer sql = new StringBuffer();
		if(null != search){
			for(String key : search.keySet() ){
				if(null == key || "".equals(key)){
					continue;
				}
	
				switch(key) { 
					case "username":
						sql.append(" and username like ? ");
		            	parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "realname":
						sql.append(" and realname like ? ");
		            	parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "type":
						sql.append(" and type = ? ");
		            	parmlist.add(search.get(key).trim());
					break;
					
					case "status":
						sql.append(" and status = ? ");
		            	parmlist.add(search.get(key).trim());
					break;
					
					case "roleid":
						sql.append(" and id in (select userid from (select userid from base_user_role where roleid =? )  ioa ) ");
		            	parmlist.add(search.get(key).trim());
					break;
					
					case "organid":
						sql.append(" and id in (select userid from (select userid from base_user_organ where organid =? )  ioa ) ");
		            	parmlist.add(search.get(key).trim());
					break;
				}
			}
		}
		return sql.toString();
	}	
}
