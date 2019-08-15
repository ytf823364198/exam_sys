package com.ziyue.activiti;
/**
 * Activiti 自定义方法
 * @author huyq
 *
 */
public class ActivitiMethod {
	
	public int judgeIndexOf(String str ,String sub){ 
		if(null == str || null  ==  sub){
			return -1;
		}
		
		if(sub.indexOf(",") >0 ){
			
			String subs[] = sub.split(",");
			for(String s : subs){
				if(str.indexOf(s) >=0 ){
					return str.indexOf(s);
				}
			}
		}
		
		return str.indexOf(sub);
		
	}
	
}
