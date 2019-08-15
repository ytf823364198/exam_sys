package com.ziyue.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseNotice;
import com.ziyue.util.BaseDao;
import com.ziyue.util.DBProductUtil;
import com.ziyue.util.DateUtil;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;
/***
 * 操作通知公告
 * @author 胡永强
 * @since 2011/07/21
 * @version 1.0.1
 */
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseNoticeDao extends BaseDao {

	/***
	 * 添加通知
	 * @param  notice 通知类
	 */
	public String addNotice(BaseNotice notice){
		String id = StringUtil.UUID();
		String sql = "insert into base_notice(id,title,type,adduserid,adduser,adddate,iswarn,warndate)";
		Object[]args={id,notice.getTitle(),notice.getType(),notice.getAdduserid(),notice.getAdduser(),
				DateUtil.fullTime(new Date()) ,notice.getIswarn(),notice.getWarndate()};
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql,args);
		return id;
	}
	
	/***
	 * 修改通知
	 * @param notice 通知类
	 */
	public void modifyNotice(BaseNotice notice){
		String sql="update base_notice set title=?,type=?,iswarn=?,warndate=? where id=? ";
		Object[]args={notice.getTitle(),notice.getType(),notice.getIswarn(),notice.getWarndate(),notice.getId()};
        jdbcTemplate.update(sql,args);
	}
	
	/***
	 * 批量删除通知
	 * @param ids 
	 */
	public void delNoticeById(String id){
		  String sql = "delete from base_notice where  id = ? ";
	       jdbcTemplate.update(sql,new Object[]{id});
	}
	
	/***
	 * 根据Id查找通知
	 * @param id 通知Id
	 * @return notice 通知实体
	 */
	public BaseNotice findNoticeById(String id){
		String sql="select * from base_notice where  id=? ";
		return (BaseNotice)jdbcTemplate.queryForObject(sql,new Object[]{id}, new BeanPropertyRowMapper(BaseNotice.class));
	}

	
	/**
	 * 分页条件查询
	 * @param pageModel 分页封装类
	 * @return 分页封装类
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		String sqlCount="select count(*) from base_notice n  where 1=1 ";
		String sqlQuery="select *  from base_notice n  where 1=1 ";

	    Map<String,String> search=pageModel.getSearch();//搜索条件
	    List parmlist=pageModel.getParmlist();          //查询参数
		
		StringBuffer sql=new StringBuffer("");
		if( null!=search ){
			for(String key : search.keySet()){
	            if("title".equals(key)){
	            	sql.append(" and n.title like ? ");
	            	parmlist.add("%"+search.get(key).trim()+"%");
	            }else if("type".equals(key)){
	            	sql.append(" and n.type = ? ");
	            	parmlist.add(search.get(key).trim());
	            }else if("typein".equals(key)){
	            	sql.append(" and n.type in ("+search.get(key)+") ");
	            }
		    }
		}
		sqlCount = sqlCount + sql;	
		sqlQuery = sqlQuery + sql + " order by n.iswarn  desc,n.adddate desc ";
		
		//设置分页统计总记录数sql
		pageModel.setSqlCount(sqlCount);
		//设置分页统计要查询结果集sql
		pageModel.setSqlQuery(sqlQuery);
		return super.queryForPageModel(pageModel);
	}
	
	/**
	 * 查最近number 条消息
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	public List<BaseNotice> findNoticeLimit(int number){
		String dbProductName = DBProductUtil.getInstance(jdbcTemplate).getProductName();
		String sql = "" ;
		if("Oracle".equals(dbProductName) ){
			sql = " select * from (select * from base_notice n  order by n.iswarn  desc,n.adddate desc )t  where rownum <=?   order by t.iswarn  desc,t.adddate desc ";
			return (List<BaseNotice>) jdbcTemplate.query(sql, new Object[]{number},new BeanPropertyRowMapper(BaseNotice.class));
		}else if("MySQL".equals(dbProductName)){
			sql=" SELECT * FROM  (SELECT * FROM base_notice n ORDER BY n.iswarn  DESC,n.adddate DESC ) ioa  LIMIT 0,? ";
		}
		return (List<BaseNotice>) jdbcTemplate.query(sql, new Object[]{number},new BeanPropertyRowMapper(BaseNotice.class));
	}
	

	public List<BaseNotice> findNoticeWarn(String iswarn){
		String sql = " select * from base_notice where iswarn =?  ";
		return (List<BaseNotice>) jdbcTemplate.query(sql, new Object[]{iswarn},new BeanPropertyRowMapper(BaseNotice.class));
	}
	
	public void modifyNoticeWarn(String iswarn,String id){
		String sql="update base_notice set iswarn=? where id=? ";
		Object[]args={iswarn,id};
        jdbcTemplate.update(sql,args);
	}

}
