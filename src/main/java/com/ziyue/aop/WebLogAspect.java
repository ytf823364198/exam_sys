package com.ziyue.aop;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ziyue.entity.BaseUser;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Order(5)
@Component
@Slf4j
public class WebLogAspect {
    @Pointcut("execution(public * com.ziyue.controller.*.*(..))")
    public void webLog(){}
//    @Autowired
//	private BaseLogService baseUserLogService;
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        BaseUser user = (BaseUser) request.getSession().getAttribute("loginUser");
        //log.info("USER ： " + user);
        // 记录下请求内容
        //log.info("URL : " + request.getRequestURL().toString());
       // log.info("HTTP_METHOD : " + request.getMethod());
        //log.info("IP : " + request.getRemoteAddr());
       // log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        Enumeration<String> enu = request.getParameterNames();
        if(null != enu) {
        	while (enu.hasMoreElements()) {
        		String name = (String)enu.nextElement();
        		//log.info("name:{},value:{}",name,request.getParameter(name));
        	}
        }
        
//        //BaseUser user = (BaseUser) request.getSession().getAttribute("loginUser");
//		BaseLog blog = new BaseLog();
//		blog.setBrowser(request.getHeader("User-Agent"));
//		blog.setIp(request.getRemoteAddr());
//		blog.setOpttime(DateUtil.fullTime());
//		blog.setAction(request.getRequestURI());
//		blog.setResult("SUCCESSX");
//		blog.setUserid("Unknown");
//		if(null != user ){
//			blog.setUserid(user.getId());
//			blog.setRealname(user.getRealname());
//		}
//		if(null == blog.getRealname()){
//			blog.setRealname(blog.getUserid());
//		}
//		//log.debug(log.toString());
//		baseUserLogService.addLog(blog);
    }
 
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        //log.info("RESPONSE : " + ret);
    }
}
