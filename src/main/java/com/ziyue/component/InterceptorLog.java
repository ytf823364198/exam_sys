package com.ziyue.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.ziyue.entity.BaseUser;
import com.ziyue.entity.BaseLog;
import com.ziyue.service.BaseLogService;
import com.ziyue.util.DateUtil;

import lombok.extern.slf4j.Slf4j; 


/**
 * 日志拦截
 */
@Component
@Slf4j
public class InterceptorLog implements HandlerInterceptor {
	@Autowired
	private BaseLogService baseUserLogService;
    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//log.debug("preHandle-->"+ request.getRequestURI() );
    	//log.debug(SecurityUtils.getSubject().getPrincipal().toString());
    	BaseUser user = (BaseUser) request.getSession().getAttribute("loginUser");
		BaseLog blog = new BaseLog();
		blog.setBrowser(request.getHeader("User-Agent"));
		blog.setIp(request.getRemoteAddr());
		blog.setOpttime(DateUtil.fullTime());
		blog.setAction(request.getRequestURI());
		blog.setResult("SUCCESS");
		blog.setUserid("Unknown");
		if(null != user ){
			blog.setUserid(user.getId());
			blog.setRealname(user.getRealname());
		}
		if(null == blog.getRealname()){
			blog.setRealname(blog.getUserid());
		}
		//log.debug(log.toString());
		baseUserLogService.addLog(blog);

    	return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	//log.debug("afterCompletion--->"+ request.getRequestURI() );
    }
}