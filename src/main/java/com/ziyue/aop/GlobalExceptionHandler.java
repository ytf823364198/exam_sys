package com.ziyue.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.ziyue.util.DateUtil;
import com.ziyue.util.HttpResult;
import com.ziyue.util.JacksonJsonUtil;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice(basePackages = "com.ziyue.controller")
@Slf4j
public class GlobalExceptionHandler {
	public static final String ERROR_VIEW = "/public/error";
/*	@ExceptionHandler(RuntimeException.class)
	public @ResponseBody HttpResult errorResult(HttpServletRequest request, HttpServletResponse response, Exception e) {
		e.printStackTrace();
		return HttpResult.error(e.getMessage());
	}*/
	
	@ExceptionHandler(Exception.class)
	public Object errorResult(HttpServletRequest request, HttpServletResponse response, Exception e) {
		 e.printStackTrace();
		 if (isAjax(request)) {
	            return JacksonJsonUtil.beanToJson(HttpResult.error(e.getMessage()));
	     } else {
	    	 	log.debug(request.getRequestURL().toString() + e);
	            ModelAndView mav = new ModelAndView();
	            mav.addObject("exception", e.getMessage());
	            mav.addObject("nowtime", DateUtil.fullTime());
	            mav.addObject("url", request.getRequestURL());
	            mav.setViewName(ERROR_VIEW);
	            return mav;
	     }
	}
	
	 // 判断是否是ajax请求
    public static boolean isAjax(HttpServletRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        return (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith));
    }
}
