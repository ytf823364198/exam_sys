package com.ziyue.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollectUtil {
	
	public static  List<String>  setToList(Set<String> sets){ 
		if(null == sets || sets.size() < 1){
			return null;
		}
		List<String> lists = new ArrayList<String>();
		for(String str : sets){
			lists.add(str);
		}
		return lists;
	}
	
	
	public static  List<String>  arrayToList(String[] arrays){ 
		if(null == arrays || arrays.length < 1){
			return null;
		}
		List<String> lists = new ArrayList<String>();
		for(String str : arrays){
			if(null != str && !"".equals(str)){
				lists.add(str);
			}
		}
		return lists;
	}

	
	public static String ListToString(List<String> lists){ 
		if(null == lists || lists.size() < 1){
			return null;
		}
		String reStr = "";
		for(String str : lists){
			if(null != str && !"".equals(str)){
				reStr = reStr + str +" ";
			}
		}
		return reStr.trim();
	}
	
	public static String listToStringComma(List<String> lists){ 
		if(null == lists || lists.size() < 1){
			return null;
		}
		String reStr = "";
		int i = 0;
		for(String str : lists){
			if(null != str && !"".equals(str)){
				if(i == 0){
					reStr = str ;
					i = 1;
				}else{
					reStr = reStr +","+ str ;
				}
				
			}
		}
		return reStr.trim();
	}
	
	
	public static String setToStringComma(Set<String> sets){ 
		if(null == sets || sets.size() < 1){
			return null;
		}
		String reStr = "";
		int i = 0;
		for(String str : sets){
			if(null != str && !"".equals(str)){
				if(i == 0){
					reStr = str ;
					i = 1;
				}else{
					reStr = reStr +","+ str ;
				}
				
			}
		}
		return reStr.trim();
	}
}
