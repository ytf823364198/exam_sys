package com.ziyue.component;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class ListenerShiroSession implements SessionListener {
	private final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        log.debug("登录+1=="+sessionCount.get());
    }

    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
        log.debug("登录退出-1=="+sessionCount.get());
    }

    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
        log.debug("登录过期-1=="+sessionCount.get());
        
    }

    public int getSessionCount() {
        return sessionCount.get();
    }

}
