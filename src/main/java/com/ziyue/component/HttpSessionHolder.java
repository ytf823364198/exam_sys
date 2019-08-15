package com.ziyue.component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.ziyue.entity.BaseUser;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class HttpSessionHolder {
	
	private static Map<String,HttpSession> sessionMap = new HashMap<String,HttpSession>();

	public static synchronized void addSession(HttpSession session) {
	    if (session != null) {
	    	//sessionMap.put(session.getId(), session);
	    	log.debug("create session ---->"+ session.getId()  );
	    }
	}
	
	public static synchronized void dropSession(HttpSession session) {
	    if (session != null) {
	    	log.debug("drop session ---->"+ session.getId()  );
	    	//sessionMap.remove(session.getId());
	    }
	}

	public static HttpSession getSession(String sessionId) {
	    if (sessionId == null){
	    	return null;
	    }
	    return (HttpSession) sessionMap.get(sessionId);
	}
	
	public static BaseUser getBaseUser(String sessionId) {
	    
		HttpSession session = getSession(sessionId);
		if(null != session){
			if( null != session.getAttribute("user")){
				return (BaseUser)session.getAttribute("user");
			}
		}
		return null;
	}
	
	public static void  printSessionMap(){
		if(null != sessionMap && sessionMap.size()>0){
			for(String key : sessionMap.keySet()){
				log.debug("printSession---->"+key  );
			}
		}
	}
	
	
}
