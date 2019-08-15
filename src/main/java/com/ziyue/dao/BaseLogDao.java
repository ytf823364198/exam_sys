package com.ziyue.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseLog;
import com.ziyue.util.BaseDao;
import com.ziyue.util.StringUtil;
import com.ziyue.util.PageModel;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseLogDao extends BaseDao {
	
	public void addLog(BaseLog blog) {
		String sql="insert into base_log(id,userid,realname,action,ip,browser,opttime,result) ";
		Object args[]=new Object[]{StringUtil.UUID(),blog.getUserid(),blog.getRealname(),blog.getAction(),blog.getIp(),
				blog.getBrowser(),blog.getOpttime(),blog.getResult()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
	}
	
	public void delLog(String time) {
		String sql ="delete from base_log where opttime < ? ";
		jdbcTemplate.update(sql, new Object[]{time});
	}

	public PageModel findBaseSplitPage(PageModel pageModel) {
		String sqlCount = "select count(*) from base_log where 1=1 ";
		String sqlQuery = "select *  from base_log where 1=1  ";
		String sql = this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()); 
		sqlCount = sqlCount + sql;
		sqlQuery = sqlQuery + sql + " order by opttime desc ";
		pageModel.setSqlCount(sqlCount);
		pageModel.setSqlQuery(sqlQuery);
		return super.queryForPageModel(pageModel);
	}
	
	/**
	 * 拼接条件查询，数据库定义的字符串长度>16用like拼接，<=16用“=”拼接（id除外）
	 * @param parmlist 参数附值后的结合
	 * @param search 查询的条件，key是要查询的字段，value查询的值
	 * @return 拼接查询条件的值
	 */
	private String joinCondition(List parmlist,Map<String, String> search) {
		StringBuffer sql = new StringBuffer();
		if(null != search){
			for(String key : search.keySet() ){
				if(null == key || "".equals(key)){
					continue;
				}

				switch(key) { 
					case "userid":
						sql.append(" and ( userid like ? or realname like ? ) ");
		            	parmlist.add("%"+search.get(key).trim()+"%");
		            	parmlist.add("%"+search.get(key).trim()+"%");
					break;
					
					case "ip":
						sql.append(" and ip like ?  ");
		            	parmlist.add(search.get(key).trim()+"%" );
					break;
					
					case "action":
						sql.append(" and action like ?  ");
		            	parmlist.add("%" + search.get(key).trim() + "%");
					break;
					
					case "opttimege":
						sql.append(" and opttime >= ?  ");
		            	parmlist.add(search.get(key).trim());
					break;
					
					case "opttimele":
						sql.append(" and opttime <= ?  ");
		            	parmlist.add(search.get(key).trim());
					break;
					
					case "result":
						sql.append(" and result = ?  ");
		            	parmlist.add(search.get(key).trim());
					break;
				}
			}
		}
		return sql.toString();
	}

	public int countLogByPageModel(PageModel pageModel) {
		String sqlCount = "select count(*) from base_log where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		pageModel.setSqlCount(sqlCount+ pageModel.getSqlJoin());
		return super.queryForCount(pageModel);
	}

	public List<BaseLog> findLogByPageModel(PageModel pageModel){
		String sqlQuery = "select * from base_log  where 1=1 ";
		if(StringUtil.isEmpty(pageModel.getSqlJoin() )){
			pageModel.setSqlJoin(this.joinCondition(pageModel.getParmlist(),pageModel.getSearch()));
		}
		pageModel.setSqlQuery( sqlQuery + pageModel.getSqlJoin() +" order by opttime desc,id" );
		pageModel.setObj(BaseLog.class);
		return (List<BaseLog>)super.queryForList(pageModel);
	}

	public void delUserLog(String time) {
		String sql ="delete from base_user_log where opttime < ? ";
		jdbcTemplate.update(sql, new Object[]{time});
	}


}
