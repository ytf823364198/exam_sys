package com.ziyue.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HttpUtil {

	public static void dialogHref(HttpServletResponse response,String url){
		PrintWriter pw = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			pw = response.getWriter();
		    pw.write("<script  type=\"text/javascript\" >frameElement.api.opener.location.href = '"+url+"';</script>");
		}catch (IOException e) {
			//e.printStackTrace();
		}finally{
			if(null!=pw){
			  pw.close();
			}
		}
	}

	public static void dialogForm(HttpServletResponse response){
		PrintWriter pw = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			pw = response.getWriter();
		    pw.write("<script  type=\"text/javascript\" >frameElement.api.opener.splitpageform.submit();</script>");
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null!=pw){
			  pw.close();
			}
		}
	}
	
	public static void dialogError(HttpServletResponse response){
		String script = "$.dialog.tips(\"对不起,操作失败,请重试 !\");";
		PrintWriter pw = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			pw = response.getWriter();
		    pw.write("<script  type=\"text/javascript\" >"+script+"</script>");
		}catch (IOException e) {
			//e.printStackTrace();
		}finally{
			if(null!=pw){
			  pw.close();
			}
		}
	}
	
	
	public static void dialogError(HttpServletResponse response,String error){
		String script = "alert(\""+error+"\");history.go(-1);";
		PrintWriter pw = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			pw = response.getWriter();
		    pw.write("<script type=\"text/javascript\" >"+script+"</script>");
		}catch (IOException e) {
			//e.printStackTrace();
		}finally{
			if(null!=pw){
				pw.close();
			}
		}
	}
}
