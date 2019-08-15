package com.ziyue.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ziyue.component.ListenerShiroSession;
import com.ziyue.shiro.CustomRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * Shiro配置
 *
 */
@Configuration
@Slf4j
public class ShiroConfig {
	@Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //loginUrl认证提交地址，如果没有认证将会请求此地址进行认证，请求此地址将由formAuthenticationFilter进行表单认证
        //如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		//访问的是后端url地址为 /login的接口
        shiroFilter.setLoginUrl("/");
        //登录成功后要跳转的链接
        shiroFilter.setSuccessUrl("/index");
        //未授权界面
        shiroFilter.setUnauthorizedUrl("/unauthorized");

        Map<String, String> filterMap = new LinkedHashMap<>();
        //静态资源配置
        filterMap.put("/static/**", "anon");
        filterMap.put("/public/**", "anon");
		
        filterMap.put("/assets/**", "anon");
        filterMap.put("/libs/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/unauthorized", "anon");
        
        filterMap.put("/bar/**", "anon");
        filterMap.put("/example/**", "anon");
        

        //filterMap.put("/", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/error", "anon");
        filterMap.put("/kaptcha", "anon");
        
        //配置某个url需要某个权限码,部分过滤器可指定参数，如perms，roles
        //filterMap.put("/hello", "perms[how_are_you]");
        
		//配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterMap.put("/logout", "logout");
        //authc:所有url都必须认证通过才可以访问
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        
        log.debug("Shiro拦截器工厂类注入成功");

        return shiroFilter;
    }

    @Bean
    public SessionManager sessionManager(SimpleCookie simpleCookie){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        //是否开启会话验证器，默认是开启的
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //去掉 JSESSIONID，默认true
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //删除失效的session ,默认true
        sessionManager.setDeleteInvalidSessions(true);
        //设置Session监听
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(new ListenerShiroSession());
        sessionManager.setSessionListeners(listeners);
        //解决JSESSIONID失效，又创建新的Session
        sessionManager.setSessionIdCookie(simpleCookie);
        
        return sessionManager;
    }

  @Bean
   public SecurityManager securityManager(CustomRealm customRealm, SessionManager sessionManager,EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(ehCacheManager);
        return securityManager;
    }

 @Bean
   public EhCacheManager ehCacheManager() {
       EhCacheManager ehCacheManager = new EhCacheManager();
       //ehCacheManager.setCacheManagerConfigFile("classpath:encache.xml");
       return ehCacheManager;
   }

   @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

   @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

  @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
   
  @Bean
   public SimpleCookie simpleCookie() {
	   SimpleCookie cookie = new SimpleCookie("ziyue.session.id");
       return cookie;
   }

}
