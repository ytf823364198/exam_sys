package com.ziyue.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
public class BaseDao{

	@Autowired
    public JdbcTemplate jdbcTemplate;
	
	public PageModel queryForPageModel(PageModel pageModel) {

		Object[] args = null;
		if(pageModel.getParmlist() != null && pageModel.getParmlist().size() > 0 ){
			args = pageModel.getParmlist().toArray();
		}
		int curpage = pageModel.getCurpage();

		int pagecount = pageModel.getPagecount();
		// 总记录数
		queryForCount(pageModel,args);

		int count =  pageModel.getCount();
		if (count != 0) {
			if(null == pageModel.getObj()){
				pageModel.setData(queryForList(pageModel.getSqlQuery(),args, pagecount,curpage));
			}else{
				pageModel.setData(queryForList(pageModel.getSqlQuery(),args, pagecount,curpage,pageModel.getObj()));
			}
		}
		if (pagecount == 0) {
			pageModel.setPagesize(count);
		} else if (count % pagecount != 0) {
			pageModel.setPagesize(count / pagecount + 1);
		} else {
			pageModel.setPagesize(count / pagecount);
		}
		
		return emptyQuery(pageModel);
	}


	private PageModel emptyQuery(PageModel pageModel) {
		pageModel.setSqlCount("");
		pageModel.setSqlJoin("");
		pageModel.setSqlQuery("");
		pageModel.setOrderby("");
		pageModel.setParmlist(null);
		return pageModel;
	}

	public int queryForCount(PageModel pageModel) {
		return queryForCount(pageModel.getSqlCount(),pageModel.getParmlist().toArray());
	}
	

	public int queryForCount(final String sql ,final Object args[]) {
		try {
			int count = 0;
			if( args != null && args.length > 0 ){
				count = (Integer) jdbcTemplate.queryForObject(sql, args, Integer.class);
			}
			else{
				count = (Integer) jdbcTemplate.queryForObject(sql, Integer.class);
			}
			return count;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public int queryForCount(PageModel pageModel,final Object args[]) {
		final String sql = pageModel.getSqlCount();
		try {
			if(null == pageModel.getQuantity() && null == pageModel.getTotal() ){
				pageModel.setCount(this.queryForCount(sql, args));
				return pageModel.getCount();
			}
			
			Map map = new HashMap();
			if( args != null && args.length > 0 ){
				map = jdbcTemplate.queryForMap(sql, args);
			}else{
				map = jdbcTemplate.queryForMap(sql);
			}
			if(null != pageModel.getQuantity()){
				try{
					pageModel.setQuantity(Integer.parseInt(map.get("quantity")+"")  );
				}catch(Exception e){
					pageModel.setQuantity(0);
				}
			}
			
			if(null != pageModel.getTotal()){
				try{
					pageModel.setTotal(Double.parseDouble(map.get("total")+"") );
				}catch(Exception e){
					pageModel.setTotal(0d);
				}
			}
			
			try{
				pageModel.setCount(Integer.parseInt(map.get("count")+"")  );
			}catch(Exception e){
				pageModel.setCount(0);
			}
			return pageModel.getCount();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public List queryForList(String sql,List parmlist ,Class c){
		Object[] args = null;
		if(parmlist != null && parmlist.size() > 0 ){
			args = parmlist.toArray();
		}
		try {
			if(args != null && args.length > 0 ){
				return jdbcTemplate.query(sql, args,new BeanPropertyRowMapper(c));
			}else{
				return jdbcTemplate.query(sql, new BeanPropertyRowMapper(c));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
	public List queryForList(String sql,Object args[], final int pagecount, final int curpage) {
		String dbProductName = DBProductUtil.getInstance(jdbcTemplate).getProductName();
		
		if (pagecount == 0 || curpage == 0) {
			return null;
		} else if (curpage == 1) {
			if("Oracle".equals(dbProductName) ){
				sql = "select * from ( " + sql + ") where rownum <= " + pagecount;
			}else if("MySQL".equals(dbProductName)){
				sql = sql +" limit 0," + pagecount;
			}
		} else if (curpage > 1) {
			if("Oracle".equals(dbProductName) ){
				sql = "select * from (select spt.*,rownum rn from ( " + sql
						+ ")spt  where rownum <= " + pagecount * curpage
						+ ") where rn > " + pagecount * (curpage - 1);
			}else if("MySQL".equals(dbProductName)){
				sql =  sql+ " limit " + (pagecount * (curpage-1) )+ "," + pagecount;
			}
		}
		try {
			if(args != null && args.length > 0){
				return (List) jdbcTemplate.queryForList(sql, args);
			}
			else{
				return (List) jdbcTemplate.queryForList(sql);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public List queryForList(PageModel pageModel) {
		return queryForList(pageModel.getSqlQuery(), pageModel.getParmlist().toArray(),pageModel.getPagecount() , pageModel.getCurpage(), pageModel.getObj());
	}

	public List queryForList(String sql,List parmlist, final int pagecount, final int curpage,Class c) {
		Object[] args = null;
		if(parmlist != null && parmlist.size() > 0 ){
			args = parmlist.toArray();
		}
		return this.queryForList(sql, args, pagecount, curpage, c);
	}
	
	public List queryForList(String sql,Object args[], final int pagecount, final int curpage,Class c) {
		String dbProductName = DBProductUtil.getInstance(jdbcTemplate).getProductName();
		if (pagecount == 0 || curpage == 0) {
			return null;
		} else if (curpage == 1) {
			if("Oracle".equals(dbProductName) ){
				sql = "select * from ( " + sql + ") where rownum <= " + pagecount;
			}else if("MySQL".equals(dbProductName)){
				sql = sql +" limit 0," + pagecount;
			}
		} else if (curpage > 1) {
			if("Oracle".equals(dbProductName) ){
				sql = "select * from (select spt.*,rownum rn from ( " + sql
						+ ")spt  where rownum <= " + pagecount * curpage
						+ ") where rn > " + pagecount * (curpage - 1);
			}else if("MySQL".equals(dbProductName)){
				sql =  sql+ " limit " + (pagecount * (curpage-1) )+ "," + pagecount;
			}
		}
		try {
			if(args != null && args.length > 0){
				return (List) jdbcTemplate.query(sql, args,new BeanPropertyRowMapper(c));
			}
			else{
				return (List) jdbcTemplate.query(sql,new BeanPropertyRowMapper(c));
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 更新表
	 * @param String table 表名
	 * @param cols 更新字段
	 * @param wheres
	 * @param id
	 */
	public void modifyTable(String table,Map<String, Object> fields, Map<String,Object> wheres,String id) {
		String sql = "";
		if( null != fields && fields.size()>0){
			List <Object> parmlist = new ArrayList<Object>();
			int i = 1;
			for(String key : fields.keySet()){
					if(i ==  fields.size() ){
						sql = sql + key + " = ? ";
					}else{
						sql = sql + key + " = ? ,";
					}
					parmlist.add(fields.get(key));
					i =  i + 1 ;
			}
			sql = "update " + table + " set "+ sql +" where 1=1 ";
			if(null != id && !"".equals(id)) {
				sql = sql + " and id = ?  ";
				parmlist.add( id );
			}
			if( null != wheres && wheres.size() > 0 ) {
				for(String key : wheres.keySet()){
					sql = sql + " and " + key + "= ? ";
					parmlist.add( id );
				}
			}
			
			jdbcTemplate.update(sql,parmlist.toArray());
		}	
	}


	
}
