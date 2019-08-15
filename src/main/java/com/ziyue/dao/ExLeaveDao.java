package com.ziyue.dao;

import java.util.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import com.ziyue.util.*;
import com.ziyue.entity.ExLeave;

/**
 * 实现对 请假申请 的数据库层操作
 * @author autoCode
 * @date 2018-9-8 21:44:27
 * @version V0.0.1
 */

@SuppressWarnings({ "unchecked", "rawtypes" })  
@Repository
public class ExLeaveDao  extends BaseDao {

	/**
	 * 添加请假申请
	 * @param lea 请假申请对象
	 * @return String id  主键
	 */
	public String addExLeave(ExLeave lea){
		if(null == lea.getId() || "".equals(lea.getId())){
			lea.setId(StringUtil.UUID());
		}
		String sql = "insert into ex_leave(id,day,telphone,email,age,money,leavedate,remark,status,sex,appuserid,apptime,procinstid)";
		Object args[] = new Object[]{lea.getId(),lea.getDay(),lea.getTelphone(),lea.getEmail(),lea.getAge(),lea.getMoney(),lea.getLeavedate(),lea.getRemark(),lea.getStatus(),lea.getSex(),lea.getAppuserid(),lea.getApptime(),lea.getProcinstid()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
		return lea.getId();
	}

	/**
	 * 删除请假申请
	 * @param id 请假申请 主键
	 */
	public void delExLeaveById(String id){
		String sql = "delete from ex_leave where id =? ";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	/**
	 * 修改请假申请
	 * @param user 请假申请对象
	 */
	public void modifyExLeave(ExLeave lea){
		String sql = "update ex_leave set day=?,telphone=?,email=?,age=?,money=?,leavedate=?,remark=?,status=?,sex=?,appuserid=?,apptime=?,procinstid=? where id = ? ";
		Object[] args = new Object[]{lea.getDay(),lea.getTelphone(),lea.getEmail(),lea.getAge(),lea.getMoney(),lea.getLeavedate(),lea.getRemark(),lea.getStatus(),lea.getSex(),lea.getAppuserid(),lea.getApptime(),lea.getProcinstid(),lea.getId()};
		jdbcTemplate.update(sql,args);
	}

	/**
	 * 通过Map集合修改请假申请对象
	 * @param cols Map集合
	 * @param id 请假申请对象主键
	 */
	public void modifyExLeaveByCols(Map<String, Object> cols, String id){
		this.modifyTable("ex_leave", cols, null, id);
	}

	/**
	 * 通过ID查找请假申请对象
	 * @param id 请假申请主键
	 * @return 请假申请对象
	 */
	public ExLeave findExLeaveById(String id){
		try{
			String sql = "select * from ex_leave where id= ? ";
			return  (ExLeave)jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(ExLeave.class));
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 通过Map集合查询请假申请对象集合
	 * @param cols Map条件集合
	 * @param 请假申请对象集合
	 */
	public List<ExLeave> findExLeaveByCols(Map<String, String> cols){
		String sql = "select * from ex_leave lea where 1=1 ";
		List<String> parmlist = new ArrayList<String>(); 
		sql = sql + this.joinCondition(parmlist,cols) + " order by lea.id";
		return this.queryForList(sql, parmlist, ExLeave.class);
	}
	
	/**
	 * 通过PageModel的search属性查符合条件的记录数（排除PageModel页数限制）
	 * @param pageModel  封装的分页对象
	 * @return int符合条件的记录数
	 */
	public int countExLeaveByPageModel(PageModel pageModel){
		String sqlCount = "select count(*) from ex_leave lea where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		pageModel.setSqlCount(sqlCount + pageModel.getSqlJoin());
		return this.queryForCount(pageModel);
	}
	
	/**
	 * 通过PageModel的search属性查符合条件的数据
	 * @param pageModel  封装的分页对象
	 * @return List符合条件的数据
	 */
	public List<ExLeave> findExLeaveByPageModel(PageModel pageModel){
		String sqlQuery = "select * from ex_leave lea where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		//一定要跟Id,因为每次查询的结果排序都不一样
		pageModel.setSqlQuery( sqlQuery + pageModel.getSqlJoin() +" order by lea.id" );
		pageModel.setObj(ExLeave.class);
		return (List<ExLeave>)super.queryForList(pageModel);
	}
	
	/**
	 * 分页查找待办理任务的 请假申请 对象的封装对象
	 * @param pageModel 封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findRunTaskSplitPage(PageModel pageModel){
		String joinsql = "from v_act_task task  left join ex_leave lea on lea.procinstid= task.proc_inst_id_ where task.proc_inst_id_ = lea.procinstid ";
		String sqlCount = "select count(*)  " + joinsql ;
		String sqlQuery = "select task.id_ as taskid, task.name_ as taskname ,lea.* " + joinsql ;
		String sql = this.joinCondition(pageModel.getParmlist(),pageModel.getSearch());
		sqlCount = sqlCount + sql;
		String orderby = pageModel.getSearch().get("orderby");
		if(null != orderby && !"".equals(orderby)){
			sqlQuery = sqlQuery + sql + " order by lea." + orderby + " desc ";
		}else{
			sqlQuery = sqlQuery + sql + " order by lea.id ";
		}
		pageModel.setQuery(sqlCount, sqlQuery, null, null); 
		return super.queryForPageModel(pageModel);
	}
	
	/**
	 * 分页查找已经办理任务的 请假申请 对象
	 * @param pageModel 封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findHisTaskSplitPage(PageModel pageModel){
		return this.findBaseSplitPage(pageModel);
	}
	
	/**
	 * 分页查找请假申请对象
	 * @param pageModel 封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		String sqlCount = "select count(*) from ex_leave lea where 1=1 "; 
		String sqlQuery = "select * from ex_leave lea where 1=1";
		String sql = this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()); 
		sqlCount = sqlCount + sql;
		String orderby = pageModel.getSearch().get("orderby");
		if(null != orderby && !"".equals(orderby)){
			sqlQuery = sqlQuery + sql + " order by lea." + orderby + " desc ";
		}else{
			sqlQuery = sqlQuery + sql + " order by lea.id ";
		}
		pageModel.setQuery(sqlCount, sqlQuery, null, null); 
		return super.queryForPageModel(pageModel);
	}
	
	/**
	 * 拼接条件查询，数据库定义的字符串长度>16用like拼接，<=16用“=”拼接（id除外）
	 * @param parmlist 参数附值后的结合
	 * @param search 查询的条件，key是要查询的字段，value查询的值
	 * @return String拼接查询条件的值
	 */
	private String joinCondition(List parmlist,Map<String, String> search) {
		StringBuffer sql = new StringBuffer();
		if(null != search){
			for(String key : search.keySet() ){
				if(null == key || "".equals(key)){
					continue;
				}

				switch(key) { 
					case "id"://主键
						sql.append(" and lea.id = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "day"://请假天数 等于
						sql.append(" and lea.day = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "dayge"://请假天数 大于等于
						sql.append(" and lea.day >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "dayle"://请假天数 小于等于
						sql.append(" and lea.day <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "daygt"://请假天数 大于
						sql.append(" and lea.day > ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "daylt"://请假天数 小于
						sql.append(" and lea.day < ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "telphone"://电话
						sql.append(" and lea.telphone = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "email"://邮件
						sql.append(" and lea.email like ? ");
						parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "age"://年龄 等于
						sql.append(" and lea.age = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "agege"://年龄 大于等于
						sql.append(" and lea.age >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "agele"://年龄 小于等于
						sql.append(" and lea.age <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "agegt"://年龄 大于
						sql.append(" and lea.age > ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "agelt"://年龄 小于
						sql.append(" and lea.age < ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "money"://扣除薪水 等于
						sql.append(" and lea.money = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "moneyge"://扣除薪水 大于等于
						sql.append(" and lea.money >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "moneyle"://扣除薪水 小于等于
						sql.append(" and lea.money <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "moneygt"://扣除薪水 大于
						sql.append(" and lea.money > ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "moneylt"://扣除薪水 小于
						sql.append(" and lea.money < ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "leavedate"://请假日期
						sql.append(" and lea.leavedate like ? ");
						parmlist.add(search.get(key).trim()+"%");
					break;
					
					case "leavedatege"://请假日期 大于等于
						sql.append(" and lea.leavedate >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "leavedatele"://请假日期 小于等于
						sql.append(" and lea.leavedate <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "remark"://备注说明
						sql.append(" and lea.remark like ? ");
						parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "status"://状态
						sql.append(" and lea.status = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "sex"://性别
						sql.append(" and lea.sex = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "appuserid"://申请人
						sql.append(" and lea.appuserid like ? ");
						parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "apptime"://申请时间
						sql.append(" and lea.apptime like ? ");
						parmlist.add(search.get(key).trim()+"%");
					break;
					
					case "apptimege"://申请时间 大于等于
						sql.append(" and lea.apptime >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "apptimele"://申请时间 小于等于
						sql.append(" and lea.apptime <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "procinstid"://流程实例ID
						sql.append(" and lea.procinstid = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "assignee"://任务的待办人,根据实际情况可修改为like查询
						sql.append(" and task.assignee = ? ");
						parmlist.add(search.get(key).trim());
					break;	
					
					case "insuserid"://已办理任务者，兼容MYSQL多嵌套一个查询
						sql.append(" and (lea.id in (select objectid from (select distinct i.objectid from base_inspect i where i.flag='ExLeave' and i.userid =? and i.isbpm = 'y' ) ioa ) ) ");
						parmlist.add(search.get(key).trim());
					break;
					
				}
			}
		}
		return sql.toString();
	}
}

