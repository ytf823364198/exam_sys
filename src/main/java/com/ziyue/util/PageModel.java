package com.ziyue.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 用SQL分页
 * @author 胡永强
 *
 */
public class PageModel {
    private int curpage = 1;     //当前页数
    private int pagecount = 20;  //每页显示条数
    private int pagesize;        //总页数
    private int count;           //总记录数
    private Map<String,String> search = new HashMap<String,String>();//搜索条件
    private String sqlCount = "";    // 直接查询数量的语句
    private String sqlQuery = "";    //直接查询结果集的语句
    private String sqlJoin = "";     //拼接的SQL
    private String orderby = "";	 //排序
    @SuppressWarnings("rawtypes")
	private List data;             //查询出来的数据集
    private List<String> parmlist = new ArrayList<String>();         //查询参数
	@SuppressWarnings("rawtypes")
	private Class obj;//要封装的对象

	//关联统计业务表单
	private Integer quantity = null;//统计总数
	private Double total = null;//计算合计
	
	
	public  static PageModel pageModel( HttpServletRequest request){
		String curpage = request.getParameter("curpage");
		String pagecount = request.getParameter("pagecount");
		PageModel pagemodel = new PageModel(curpage, pagecount);
	    pagemodel.setSearch(request.getParameterMap());
		return pagemodel;
	}	
	
	@SuppressWarnings("rawtypes")
	public  static PageModel pageModel( HttpServletRequest request,Class obj){
		PageModel pagemodel = pageModel(request);
	    pagemodel.setObj(obj);
		return pagemodel;
	}	
	
	public PageModel(String curpage, String pagecount) {
		super();
		if(curpage != null){
			this.curpage = Integer.parseInt(curpage);	
		}
		if(pagecount != null){
		    this.pagecount = Integer.parseInt(pagecount);
		}
		this.search.clear();
	 }
	
	
	public void setSearch(Map<String, String[]> search) {
		if(null != search && search.size() > 0 ){
			for( String  key : search.keySet() ){
	            if(null != key && !"".equals(key) &&  null != search.get(key)  && search.get(key).length > 0  && !"".equals(search.get(key)[0])   ){
	            	if(search.get(key).length == 1){
	            		this.search.put(key,search.get(key)[0] );
	            	}else{
	            		this.search.put(key+"in",StringUtil.sqlArrayToInQuery(search.get(key)) );
	            	}
	            }
			}
		}
	}
	
	public void setQuery(String sqlCount,String sqlQuery,Integer quantity,Double total) {
		this.sqlCount = sqlCount;
		this.sqlQuery = sqlQuery;
		this.quantity = quantity;
		this.total = total;
	}

	//计算要查询多少次
	public void setPagesizeByCount(int count){
		if(count % this.pagecount != 0){
			this.pagesize = count / this.pagecount +1 ;
		}else{
			this.pagesize = count / this.pagecount;
		}
	}
			
	
	public int getCurpage() {
		return curpage;
	}
	
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	
	public int getPagesize() {
		return pagesize;
	}
	
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	
	public int getPagecount() {
		return pagecount;
	}
	
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	
	@SuppressWarnings("rawtypes")
	public List getData() {
		return data;
	}
	
	@SuppressWarnings("rawtypes")
	public void setData(List data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<String, String> getSearch() {
		return search;
	}

	public String getSqlCount() {
		return sqlCount;
	}

	public void setSqlCount(String sqlCount) {
		this.sqlCount = sqlCount;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
	public String getSqlJoin() {
		return sqlJoin;
	}

	public void setSqlJoin(String sqlJoin) {
		this.sqlJoin = sqlJoin;
	}

	@SuppressWarnings("rawtypes")
	public List getParmlist() {
		return parmlist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setParmlist(List parmlist) {
		this.parmlist = parmlist;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@SuppressWarnings("rawtypes")
	public Class getObj() {
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public void setObj(Class obj) {
		this.obj = obj;
	}

}
