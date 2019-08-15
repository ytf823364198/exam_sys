package com.ziyue.component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;
@Component
public class ListenerHttpSession implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		 HttpSession session = httpSessionEvent.getSession();
		 HttpSessionHolder.addSession(session);
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
	    HttpSession session = httpSessionEvent.getSession();
	    HttpSessionHolder.dropSession(session);
	}
}
