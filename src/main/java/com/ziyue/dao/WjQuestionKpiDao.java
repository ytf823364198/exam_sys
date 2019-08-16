package com.ziyue.dao;

import java.util.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import com.ziyue.util.*;
import com.ziyue.entity.MenuTree;
import com.ziyue.entity.QuestionBank;
import com.ziyue.entity.WjQuestionKpi;

/**
 * 实现对 题目表 的数据库层操作
 * @author autoCode
 * @date 2019-8-15 16:26:53
 * @version V0.0.1
 */

@SuppressWarnings({ "unchecked", "rawtypes" })  
@Repository
public class WjQuestionKpiDao  extends BaseDao {

	/**
	 * 添加题目表
	 * @param wj_question 题目表对象
	 * @return String id  主键
	 */
	public String addWjQuestionKpi(QuestionBank questionBank){
		if(null == questionBank.getId() || "".equals(questionBank.getId())){
			questionBank.setId(StringUtil.UUID());
		}
		String sql = "insert into question_bank(pid,remark,name,createtime,type)";
		Object args[] = new Object[]{questionBank.getPid(),questionBank.getRemark(),questionBank.getName(),questionBank.getCreattime(),questionBank.getType()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
		return questionBank.getId();
	}

	/**
	 * 删除题目表
	 * @param id 题目表 主键
	 */
	public void delWjQuestionKpiById(String id){
		String sql = "delete from question_bank where id =? ";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	/**
	 * 修改题目表
	 * @param user 题目表对象
	 */
	public void modifyWjQuestionKpi(WjQuestionKpi wj_question){
		String sql = "update question_bank set oid=?content=?,qtype=?,seq=?,remark=?,score=?,rightvalue=?, where id = ? ";
		Object[] args = new Object[]{wj_question.getOid(),wj_question.getContent(),wj_question.getQtype(),wj_question.getSeq(),wj_question.getRemark(),wj_question.getScore(),wj_question.getRightvalue(),wj_question.getId()};
		jdbcTemplate.update(sql,args);
	}

	/**
	 * 通过Map集合修改题目表对象
	 * @param cols Map集合
	 * @param id 题目表对象主键
	 */
	public void modifyWjQuestionKpiByCols(Map<String, Object> cols, String id){
		this.modifyTable("question_bank", cols, null, id);
	}

	/**
	 * 通过ID查找题目表对象
	 * @param id 题目表主键
	 * @return 题目表对象
	 */
	public QuestionBank findWjQuestionKpiById(String id){
		try{
			String sql = "select * from question_bank where id= ? ";
			return  (QuestionBank)jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(QuestionBank.class));
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 通过Map集合查询题目表对象集合
	 * @param cols Map条件集合
	 * @param 题目表对象集合
	 */
	public List<QuestionBank> findWjQuestionKpiByCols(Map<String, String> cols){
		String sql = "select * from question_bank wj_question where 1=1 ";
		List<String> parmlist = new ArrayList<String>(); 
		sql = sql + this.joinCondition(parmlist,cols) + " order by wj_question.id";
		return this.queryForList(sql, parmlist, QuestionBank.class);
	}
	
	/**
	 * 通过PageModel的search属性查符合条件的记录数（排除PageModel页数限制）
	 * @param pageModel  封装的分页对象
	 * @return int符合条件的记录数
	 */
	public int countWjQuestionKpiByPageModel(PageModel pageModel){
		String sqlCount = "select count(*) from question_bank wj_question where 1=1 ";
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
	public List<QuestionBank> findWjQuestionKpiByPageModel(PageModel pageModel){
		String sqlQuery = "select * from question_bank wj_question where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		//一定要跟Id,因为每次查询的结果排序都不一样
		pageModel.setSqlQuery( sqlQuery + pageModel.getSqlJoin() +" order by wj_question.id" );
		pageModel.setObj(QuestionBank.class);
		return (List<QuestionBank>)super.queryForList(pageModel);
	}
	
	
	/**
	 * 分页查找题目表对象
	 * @param pageModel 封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		String sqlCount = "select count(*) from question_bank wj_question where 1=1 "; 
		String sqlQuery = "select * from question_bank wj_question where 1=1";
		String sql = this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()); 
		sqlCount = sqlCount + sql;
		String orderby = pageModel.getSearch().get("orderby");
		if(null != orderby && !"".equals(orderby)){
			sqlQuery = sqlQuery + sql + " order by wj_question." + orderby + " desc ";
		}else{
			sqlQuery = sqlQuery + sql + " order by wj_question.id ";
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
					
					case "name"://题目内容
						sql.append(" and wj_question.name like ? ");
						parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "id"://编号 
						sql.append(" and wj_question.id = ? ");
						parmlist.add(search.get(key).trim());
					break;
					
					
				}
			}
		}
		return sql.toString();
	}

	public List<MenuTree> getTreeList() {
		// TODO Auto-generated method stub
		String sql = "select id,pid,name,type from question_bank";
		List<MenuTree> lists = (List<MenuTree>) jdbcTemplate.query(sql, new Object[] {},new BeanPropertyRowMapper(MenuTree.class));
		return lists;
	}
}

