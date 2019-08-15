package com.ziyue.component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.stereotype.Component;

import com.ziyue.util.Constant;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class ListenerServlet implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	ServletContext sc = sce.getServletContext();
    	sc.setAttribute("const",new Constant());
    	log.debug("contextInitialized...web应用启动" );
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	log.debug("contextDestroyed...当前web项目销毁");
    }

}
