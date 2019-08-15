package com.ziyue.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
/***
 * 避免重复提交
 * @author 胡永强
 *
 */
public class Token {
	/***
	 * 设置令牌
	 * @param request
	 */
	public static void setToken(HttpServletRequest request){
		request.getSession().setAttribute("SesToken",UUID.randomUUID().toString() );
	}
	/***
	 * 验证是否为重复提交
	 * @param HttpServletRequest request
	 * @return String true非重复提交,false重复提交,error非法操作
	 */
	public static boolean validToken(HttpServletRequest request){		
		String sessionToken = (String)request.getSession().getAttribute("SesToken");
		String requestToken = request.getParameter("SesToken");
		if(null == sessionToken  || "null".equals(sessionToken)){
			sessionToken = "";
		}
		if(null == requestToken || "null".equals(requestToken) ){
			requestToken = "";
		}
		if(sessionToken.equals(requestToken)){
			//返回前一定要重置session中的SesToken
			request.getSession().setAttribute("SesToken",UUID.randomUUID().toString() );
			//非重复提交
			return true;
		}else{
			//返回前一定要重置session中的SesToken
			request.getSession().setAttribute("SesToken",UUID.randomUUID().toString() );
			//重复提交
			return false;
		}
	}
}
