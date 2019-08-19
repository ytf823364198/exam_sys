package com.ziyue.dao;

import java.util.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import com.ziyue.util.*;
import com.ziyue.entity.QuestionBank;

/**
 * 实现对 题库信息 的数据库层操作
 * @author autoCode
 * @date 2019-8-15 13:56:02
 * @version V0.0.1
 */

@SuppressWarnings({ "unchecked", "rawtypes" })  
@Repository
public class QuestionKpiDao  extends BaseDao {

	/**
	 * 添加题库信息
	 * @param question_kpi 题库信息对象
	 * @return String id  主键
	 */
	public String addQuestionBank(QuestionBank question_kpi){
		if(null == question_kpi.getId() || "".equals(question_kpi.getId())){
			question_kpi.setId(StringUtil.UUID());
		}
		String sql = "insert into question_bank(id,name,creattime,remark,type)";
		Object args[] = new Object[]{question_kpi.getId(),question_kpi.getText(),question_kpi.getCreattime(),question_kpi.getRemark(),question_kpi.getType()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
		return question_kpi.getId();
	}

	/**
	 * 删除题库信息
	 * @param id 题库信息 主键
	 */
	public void delQuestionBankById(String id){
		String sql = "delete from question_bank where id =? ";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	/**
	 * 修改题库信息
	 * @param user 题库信息对象
	 */
	public void modifyQuestionBank(QuestionBank question_kpi){
		String sql = "update question_bank set name=?,creattime=?,remark=?,type=? where id = ? ";
		Object[] args = new Object[]{question_kpi.getText(),question_kpi.getCreattime(),question_kpi.getRemark(),question_kpi.getType(),question_kpi.getId()};
		jdbcTemplate.update(sql,args);
	}

	/**
	 * 通过Map集合修改题库信息对象
	 * @param cols Map集合
	 * @param id 题库信息对象主键
	 */
	public void modifyQuestionBankByCols(Map<String, Object> cols, String id){
		this.modifyTable("question_bank", cols, null, id);
	}

	/**
	 * 通过ID查找题库信息对象
	 * @param id 题库信息主键
	 * @return 题库信息对象
	 */
	public QuestionBank findQuestionBankById(String id){
		try{
			String sql = "select * from question_bank where id= ? ";
			return  (QuestionBank)jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(QuestionBank.class));
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 通过Map集合查询题库信息对象集合
	 * @param cols Map条件集合
	 * @param 题库信息对象集合
	 */
	public List<QuestionBank> findQuestionBankByCols(Map<String, String> cols){
		String sql = "select * from question_bank question_kpi where 1=1 ";
		List<String> parmlist = new ArrayList<String>(); 
		sql = sql + this.joinCondition(parmlist,cols) + " order by question_kpi.id";
		return this.queryForList(sql, parmlist, QuestionBank.class);
	}
	
	/**
	 * 通过PageModel的search属性查符合条件的记录数（排除PageModel页数限制）
	 * @param pageModel  封装的分页对象
	 * @return int符合条件的记录数
	 */
	public int countQuestionBankByPageModel(PageModel pageModel){
		String sqlCount = "select count(*) from question_bank question_kpi where 1=1 ";
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
	public List<QuestionBank> findQuestionBankByPageModel(PageModel pageModel){
		String sqlQuery = "select * from question_bank question_kpi where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		//一定要跟Id,因为每次查询的结果排序都不一样
		pageModel.setSqlQuery( sqlQuery + pageModel.getSqlJoin() +" order by question_kpi.id" );
		pageModel.setObj(QuestionBank.class);
		return (List<QuestionBank>)super.queryForList(pageModel);
	}
	
	
	/**
	 * 分页查找题库信息对象
	 * @param pageModel 封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		String sqlCount = "select count(*) from question_bank question_kpi where 1=1 "; 
		String sqlQuery = "select * from question_bank question_kpi where 1=1 and question_kpi.type = '2' ";
		String sql = this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()); 
		sqlCount = sqlCount + sql;
		String orderby = pageModel.getSearch().get("orderby");
		if(null != orderby && !"".equals(orderby)){
			sqlQuery = sqlQuery + sql + " order by question_kpi." + orderby + " desc ";
		}else{
			sqlQuery = sqlQuery + sql + " order by question_kpi.id ";
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
					case "id"://题库序号
						sql.append(" and question_kpi.id = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "name"://题库名称
						sql.append(" and question_kpi.name like ? ");
						parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "creattime"://创建时间
						sql.append(" and question_kpi.creattime like ? ");
						parmlist.add(search.get(key).trim()+"%");
					break;
					
					case "creattimege"://创建时间 大于等于
						sql.append(" and question_kpi.creattime >= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "creattimele"://创建时间 小于等于
						sql.append(" and question_kpi.creattime <= ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					case "remark"://试卷备注
						sql.append(" and question_kpi.remark like ? ");
						parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "type"://试卷类型
						sql.append(" and question_kpi.type = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					
				}
			}
		}
		return sql.toString();
	}
}

