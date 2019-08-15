package com.ziyue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ziyue.component.InterceptorLog;
import com.ziyue.component.InterceptorSession;

//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
//@EnableWebMvc   不要接管SpringMVC

@Configuration
@SuppressWarnings({ "unused"})
public class MvcConfig  extends WebMvcConfigurerAdapter {
	
	@Bean
    public InterceptorLog interceptorLog(){
        return new InterceptorLog();
    }
	
	@Bean
    public InterceptorSession interceptorSession(){
        return new InterceptorSession();
    }

	
//	@Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//       // super.addViewControllers(registry);
//        //浏览器发送 /ok 请求来到 success
//        registry.addViewController("/ok").setViewName("success");
//    }
	
    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    @Bean //将组件注册在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
//        	//视图转换器
//            @Override
//            public void addViewControllers(ViewControllerRegistry registry) {
//            	  super.addViewControllers(registry);
//                  registry.addViewController("/").setViewName("index/login.html");
//                  registry.addViewController("/index").setViewName("index/index");
//            }
            
            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
            	
            	registry.addInterceptor(interceptorLog())
	         	    	.addPathPatterns("/**");
               //Session 拦截器( 是否改用Shiro ?? )
               //registry.addInterceptor(interceptorSession())
               //         .addPathPatterns("/**")
               //         .excludePathPatterns("/","/index","/login","/error");
            }
            
            
        };
        return adapter;
    }
	
}
