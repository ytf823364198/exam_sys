package com.ziyue.util;

import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;
/***
 * String Util
 * @author 胡永强
 *
 */
public class StringUtil extends StringUtils {
	//获取32为的UUID  
	public static String UUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	//拼接SQL中多少个占位符
	public static String sqlField(int length){
		String field = "?";
		for(int i = 1 ; i < length; i ++ ){
			field = field + ",?";
		}
		return "values("+field+")"; 
	}
		
	//数组转IN查询
	public static String  sqlArrayToInQuery(String [] array){
		String str =  "";	
		if(null != array  && array.length >0 ){
			for(String s : array){
				if(null != s && !"".equals(s.trim())){
					if("".equals(str) ){
						str = "'"+s.trim()+"'";
					}else{
						str = str + ",'"+s.trim()+"'";
					}
				}
			}
		}
		return str;
	}
			
	//list转IN查询
	public static String  sqlListToInQuery(List<String> array){
		String str =  "";	
		if(null != array  && array.size() > 0 ){
			for(String s : array){
				if(null != s && !"".equals(s.trim())){
					if("".equals(str) ){
						str = "'"+s.trim()+"'";
					}else{
						str = str + ",'"+s.trim()+"'";
					}
				}
			}
		}
		return str;
	}
	
	//导出CSV,特殊字符转中文字符
	public static String csvHandlerStr(String str) {  
        //csv格式如果有逗号，整体用双引号括起来；如果里面还有双引号就替换成两个双引号      
        if(null == str ){
        	return "";
        }
        str = str.replaceAll("null", "");
        str = str.trim();
        str = str.replaceAll("\"", "“");
        str = str.replaceAll(",", "，");
        return str;
	}  
	
	public static Boolean  notEmpty(String str){
		if(null != str && !"".equals(str)){
			return true;
		}
		return false;
	}
	
	public static String nullReturnStar(String str){ 
		if(null != str && !"".equals(str)){
			return str;
		}
		return "*";
	}
	
	public static String nullReturnEmpty(String str){ 
		if(null != str){
			return str;
		}
		return "";
	}
}
