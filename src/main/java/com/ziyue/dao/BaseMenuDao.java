package com.ziyue.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseMenu;
import com.ziyue.util.BaseDao;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseMenuDao extends BaseDao {

	public List<BaseMenu> findMenuByRoleId(String roleId){
		String sql = " select menu.* from base_role_menu rm" +
					 " left join base_menu menu on rm.menuid = menu.id" +
					 " where rm.roleid = ?  order by sortno";
		return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{roleId},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	public List<BaseMenu> findTopMenuByUserId(String userId,String pid){
		/**注释掉的只支持ORACLE*/
		//String sql = " select menu.* from base_menu menu " +
		// " where menu.id in ( select distinct(menuid) from base_role_menu where roleid in (select roleid from base_user_role where userid =? )    )  " +
		// " and pid=? order by orders";
		String sql ="SELECT * FROM (" +
				"SELECT DISTINCT(menuid) FROM base_user_role ur  LEFT JOIN base_role_menu rm ON ur.roleid = rm.roleid   WHERE ur.userid =?  )md   " +
				"LEFT JOIN base_menu m  ON md.menuid = m.id  WHERE m.pid=? ORDER BY sortno";
		return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{userId,pid},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	public List<BaseMenu> findMenuByUserId(String userId,String pid){
		/**注释掉的只支持ORACLE*/
		//String sql = "select menu.* from base_menu menu" +
		//		" where menu.id <>?  and  menu.id in ( select distinct(menuid) from base_role_menu where roleid in(select roleid from base_user_role where userid =? )    )   " +
		//		" start with id= ? connect by pid =prior id  order by orders ";
		//return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{pid,userId,pid},new BeanPropertyRowMapper(BaseMenu.class));
		String sql ="SELECT * FROM ( "+
					"	SELECT * FROM (SELECT DISTINCT rm.MENUID FROM base_user_role ur LEFT JOIN base_role_menu rm ON rm.ROLEID = ur.ROLEID WHERE ur.USERID=? "+
					"			)md LEFT JOIN base_menu m ON md.MENUID = m.ID  ORDER BY id "+
					"	)rmt  WHERE rmt.pid IN "+
					"	(SELECT id FROM  base_menu WHERE pid =? OR id = ?) " +
					"ORDER BY  rmt.sortno ";
		
		return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{userId,pid,pid},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	public List<BaseMenu> findMenuByUserId(String userId){
		String sql ="SELECT * FROM ( "+
					"	SELECT * FROM (SELECT DISTINCT rm.MENUID FROM base_user_role ur LEFT JOIN base_role_menu rm ON rm.ROLEID = ur.ROLEID WHERE ur.USERID=? "+
					"			)md LEFT JOIN base_menu m ON md.MENUID = m.ID  ORDER BY id "+
					"	)rmt  " +
					"ORDER BY  rmt.sortno ";
		
		return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{userId},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	public List<BaseMenu> findMenuRemindMoblieByUserId(String userId){
		/**注释掉的只支持ORACLE*/
		//String sql = " select menu.* from base_menu menu" +
		// " where menu.id in ( select distinct(menuid) from base_role_menu where roleid in(select roleid from base_user_role where userid =? )    ) and remind ='y' and  murl like 'm_%'  order by remindorders";
		String sql = "SELECT * FROM ( " +
				 "		SELECT DISTINCT rm.MENUID FROM base_user_role ur LEFT JOIN base_role_menu rm ON rm.ROLEID = ur.ROLEID WHERE ur.USERID=?" +
				 "	)md LEFT JOIN base_menu m ON md.MENUID = m.ID  WHERE remind ='y' and  murl like 'm_%'  ORDER BY remindorders";
		return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{userId},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	public List<BaseMenu> findMenuRemindByUserId(String userId){
		/**注释掉的只支持ORACLE*/
		//String sql = " select menu.* from base_menu menu" +
		// " where menu.id in ( select distinct(menuid) from base_role_menu where roleid in(select roleid from base_user_role where userid =? )    ) and remind ='y' order by remindorders";
		String sql = "SELECT * FROM ( " +
					 "		SELECT DISTINCT rm.MENUID FROM base_user_role ur LEFT JOIN base_role_menu rm ON rm.ROLEID = ur.ROLEID WHERE ur.USERID=?" +
					 "	)md LEFT JOIN base_menu m ON md.MENUID = m.ID  WHERE remind ='y' ORDER BY remindorders";
		
		return (List<BaseMenu>)jdbcTemplate.query(sql,new Object[]{userId},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	/**
	 * 查用户所有的权限号
	 * @param  id 用户ID 
	 * @return 权限号集合
	 */
	public List<String> findMenuPermcodeByUserId(String id) {
		String sql = "SELECT distinct m.permcode FROM ( "+
					 "	SELECT DISTINCT rm.MENUID FROM base_user_role ur LEFT JOIN base_role_menu rm ON rm.ROLEID = ur.ROLEID WHERE ur.USERID=? "+
					 ")md LEFT JOIN base_menu m ON md.MENUID = m.ID  "+
					 "where length(m.permcode) > 0";
	
		return (List<String>)jdbcTemplate.queryForList(sql,new Object[]{id},String.class);
		
	}

	public List<BaseMenu> findMenus(){
		String sql = " select * from base_menu order by id" ;
		return (List<BaseMenu>)jdbcTemplate.query(sql,new BeanPropertyRowMapper(BaseMenu.class));
	}

	public BaseMenu findMenuById(String id){
		String sql = " select * from base_menu where id =? " ;
		return (BaseMenu)jdbcTemplate.queryForObject(sql,new Object[]{id},new BeanPropertyRowMapper(BaseMenu.class));
	}
	
	public void delMenuByRole(String roleId){
		String sql = " delete from base_role_menu where roleid = ? " ;
		jdbcTemplate.update(sql,new Object[]{roleId});
	}
	
	public void addRoleMenu(String roleId, String menuId){
		String sql = " insert into base_role_menu (roleid, menuid ) values (?, ?) " ;
		jdbcTemplate.update(sql,new Object[]{roleId, menuId});
	}
	

}
