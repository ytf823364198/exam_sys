package com.ziyue.shiro;

import java.io.Serializable;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ziyue.util.SerializableUtil;

@Repository
public class CustomShiroSessionDAO extends EnterpriseCacheSessionDAO {
	@Autowired
	@Lazy
    public JdbcTemplate jdbcTemplate;
	
    @Override
    protected Serializable doCreate(Session session) {
        super.doCreate(session);
        System.out.println("doCreate session = " + session.getId());
    	String sql = "insert into base_session(id,jsession) values(?,?) ";
    	jdbcTemplate.update(sql,new Object[]{session.getId(),SerializableUtil.serialize(session)});
        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //Session session = super.doReadSession(sessionId);
        String sql = "select jsession from base_session where id = ? "; 
        List <String> sessions =  jdbcTemplate.queryForList(sql, new Object[]{sessionId} ,String.class);
        if(null == sessions  || sessions.size() == 0){
        	return null;
        }
        System.out.println("doReadSession session = " + sessionId);
        return  SerializableUtil.deserialize(sessions.get(0));
    }
	
    @Override
    protected void doUpdate(Session session) {
    	 System.out.println("doUpdate session = " + session.getId());
       // super.doUpdate(session);
    	if(session instanceof ValidatingSession 
    			&& !(( ValidatingSession) session ).isValid()){
    			return ;
    	}
    	String sql = "update base_session set jsession = ? where id =? ";
    			jdbcTemplate.update(sql,new Object[]{SerializableUtil.serialize(session),session.getId()});
    }
    
    @Override
    protected void doDelete(Session session) {
    	System.out.println("doDelete session = " + session.getId());
       // super.doDelete(session);
    	String sql = "delete from base_session where id = ? ";
    	jdbcTemplate.update(sql,new Object[]{session.getId()});
    }
    
}
