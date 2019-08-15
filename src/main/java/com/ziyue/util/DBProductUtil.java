package com.ziyue.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
/**
 * 获取数据库类型
 * @author huyq
 */

public class DBProductUtil {
	private static DBProductUtil util;
	
	private static String productName ;
	
	private DBProductUtil() {}
	
	
	public static DBProductUtil getInstance(JdbcTemplate jdbcTemplate) {
		if(util == null) {
			try {
				
				/**
				DataSource.getConnection()总是从datasource或连接池返回一个新的连接。
				通过调用 jdbcTemplate.getDataSource().getConnection()显式获取一个连接，这个连接不是方法事务上下文线程绑定的连接，所以如果开发者如果没有手工释放这连接（显式调用 Connection#close() 方法），则这个连接将永久被占用（处于 active 状态），造成连接泄漏！
				
				Spring 提供了一个能从当前事务上下文中获取绑定的数据连接的工具类，那就是 DataSourceUtils。Spring 的 JdbcTemplate 内部也是通过 DataSourceUtils 来获取连接的。
				DataSourceUtils 提供了若干获取和释放数据连接的静态方法，说明如下：
				static Connection doGetConnection(DataSource dataSource)：首先尝试从事务上下文中获取连接，失败后再从数据源获取连接；
				static Connection getConnection(DataSource dataSource)：和 doGetConnection 方法的功能一样，实际上，它内部就是调用 doGetConnection 方法获取连接的；
				static void doReleaseConnection(Connection con, DataSource dataSource)：释放连接，放回到连接池中；
				static void releaseConnection(Connection con, DataSource dataSource)：和 doReleaseConnection 方法的功能一样，实际上，它内部就是调用 doReleaseConnection 方法获取连接的；
				
				DataSourceUtils.getConnection()它首先查看当前是否存在事务管理上下文，并尝试从事务管理上下文获取连接，如果获取失败，直接从数据源中获取连接。在获取连接后，如果当前拥有事务上下文，则将连接绑定到事务上下文中。
				如果处于事务上下文中，那么开发者不需要显示关闭或者释放连接，但是如果 DataSourceUtils 在没有事务上下文的方法中使用 getConnection() 获取连接，依然会造成数据连接泄漏，这个时候就需要显示release了 ！
				
				DataSourceUtils.releaseConnection(conn,jdbcTemplate.getDataSource());
				*/

				//jdbcTemplate.getDataSource().getConnection() 会重新打开一个新的连接
				//Connection con = 	jdbcTemplate.getDataSource().getConnection();
				//productName = con.getMetaData().getDatabaseProductName();
				//con.close();
				
				Connection con =  DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
				productName = con.getMetaData().getDatabaseProductName();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}  
			util = new DBProductUtil();
		}
		return util;
	}
	
	@SuppressWarnings("static-access")
	public static String getProductName(){
		return util.productName;
	}
	
}
