package com.ziyue.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import com.ziyue.entity.ExampleLeave;
import com.ziyue.util.BaseDao;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;
/**
 * 实现对 请假申请 的数据库层操作
 * @author autoCode
 * @date 2018-4-16 15:09:03
 * @version V0.0.1
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class ExampleLeaveDao  extends BaseDao {

	public String addExampleLeave(ExampleLeave lea){
		if(null == lea.getId() || "".equals(lea.getId())){
			lea.setId(StringUtil.UUID());
		}
		String sql = "insert into example_leave(id,day,telphone,email,age,money,leavedate,remark,status,sex,appuserid,apptime,procinstid)";
		Object args[] = new Object[]{lea.getId(),lea.getDay(),lea.getTelphone(),lea.getEmail(),lea.getAge(),lea.getMoney(),lea.getLeavedate(),lea.getRemark(),lea.getStatus(),lea.getSex(),lea.getAppuserid(),lea.getApptime(),lea.getProcinstid()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
		return lea.getId();
	}

	public void delExampleLeaveById(String id){
		String sql = "delete from example_leave where id =? ";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	public void modifyExampleLeave(ExampleLeave lea){
		String sql = "update example_leave set day=?,telphone=?,email=?,age=?,money=?,leavedate=?,remark=?,status=?,sex=? where id = ? ";
		Object[]args = new Object[]{lea.getDay(),lea.getTelphone(),lea.getEmail(),lea.getAge(),lea.getMoney(),lea.getLeavedate(),lea.getRemark(),lea.getStatus(),lea.getSex(),lea.getId()};
		jdbcTemplate.update(sql,args);
	}
	
	public void modifyExampleLeaveByCols(Map<String, Object> cols, String id){
		this.modifyTable("example_leave", cols, null, id);
	}

	public ExampleLeave findExampleLeaveById(String id){
		try{
			String sql = "select * from example_leave where id= ? ";
			return  (ExampleLeave)jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(ExampleLeave.class));
		}catch(Exception e){
			return null;
		}
	}
	
	public List<ExampleLeave> findExampleLeaveByCols(Map<String, String> cols){
		String sql = "select * from example_leave lea where 1=1 ";
		List<String> parmlist = new ArrayList<String>(); 
		sql = sql + this.joinCondition(parmlist,cols) + " order by lea.id";
		return this.queryForList(sql, parmlist, ExampleLeave.class);
	}
	
	public int countExampleLeaveByPageModel(PageModel pageModel){
		String sqlCount = "select count(*) from example_leave lea where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		pageModel.setSqlCount(sqlCount + pageModel.getSqlJoin());
		return this.queryForCount(pageModel);
	}
	
	public List<ExampleLeave> findExampleLeaveByPageModel(PageModel pageModel){
		String sqlQuery = "select * from example_leave lea where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		pageModel.setSqlQuery( sqlQuery + pageModel.getSqlJoin() +" order by lea.id" );
		pageModel.setObj(ExampleLeave.class);
		return (List<ExampleLeave>)super.queryForList(pageModel);
	}
	 
	
	public PageModel findRunTaskSplitPage(PageModel pageModel){
		String joinsql = "from v_act_task task  left join example_leave lea on lea.procinstid= task.proc_inst_id_ where task.proc_inst_id_ = lea.procinstid ";
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
	
	
	public PageModel findHisTaskSplitPage(PageModel pageModel){
		return this.findBaseSplitPage(pageModel);
	}
	
	
	public PageModel findBaseSplitPage(PageModel pageModel){
		String sqlCount = "select count(*) from example_leave lea where 1=1 "; 
		String sqlQuery = "select * from example_leave lea where 1=1";
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
					
					case "leavedate"://请假时间  等于
						sql.append(" and lea.leavedate  = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "leavedatege"://请假时间 大于等于
						sql.append(" and lea.leavedate >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "leavedatele"://请假时间 小于等于
						sql.append(" and lea.leavedate <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "leavedategt"://请假时间大于
						sql.append(" and lea.leavedate > ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "adddatelt"://请假时间小于
						sql.append(" and lea.leavedate < ? ");
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
					
					case "procinstid"://流程实例ID
						sql.append(" and lea.procinstid = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "assignee"://任务的待办人
						sql.append(" and task.assignee = ? ");
						parmlist.add(search.get(key).trim());
					break;	
					
					case "insuserid"://已办理任务者
						sql.append(" and (lea.id in (select objectid from (select distinct i.objectid from base_inspect i where i.flag='ExampleLeave' and i.userid =? and i.isbpm='y' ) ioa ) ) ");
						parmlist.add(search.get(key).trim());
					break;
					
					
				}
			}
		}
		return sql.toString();
	}
}

