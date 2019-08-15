package com.ziyue.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ziyue.component.ListenerServlet;
import com.ziyue.component.ListenerHttpSession;

//WebServer 三大组件Servlet、Filter、Listener的配置
@Configuration
public class ServerConfig {
	
	//监听容器的启动和关闭
	@SuppressWarnings("rawtypes")
	@Bean
    public ServletListenerRegistrationBean listenerServlet(){
        ServletListenerRegistrationBean<ListenerServlet> registrationBean = new ServletListenerRegistrationBean<>(new ListenerServlet());
        return registrationBean;
    }
	
	
	//监听Session的启动和关闭
	@SuppressWarnings("rawtypes")
	//@Bean
    public ServletListenerRegistrationBean listenerSession(){
        ServletListenerRegistrationBean<ListenerHttpSession> registrationBean = new ServletListenerRegistrationBean<>(new ListenerHttpSession());
        return registrationBean;
    }
}
