package com.ziyue.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ziyue.entity.BaseUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 检查Session
 */
@Component
@Slf4j
public class InterceptorSession implements HandlerInterceptor {

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
        if(user == null){
            //未登陆，返回登陆页面
            request.setAttribute("msg","没有权限请先登陆");
            log.debug(" preHandle = " + request.getRequestURI() +  "  [user=]"+ user );
            request.getRequestDispatcher("/").forward(request,response);
            return true;
        }else{
            //已登陆，放行请求
            return true;
        }
    }

    //处理过程拦截
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    //后置拦截
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	log.debug(" afterCompletion  " + request.getRequestURI()   );
    }
}