package com.ziyue.util;


import java.util.LinkedHashMap;
import java.util.Map;

public class Constant{
	/**本系统名称*/
	public static final String PROJECT_NAME = "紫越科技开发框架"; 
	/**应用版本号*/
	public static final String PROJECT_VERSON = "V2.0.0";
	/**建设本系统单位名称*/
	public static final String UNIT_NAME = "紫越科技";
	/**管理本系统所在单位名称*/
	public static final String UNIT_MANAGER_ORGAN = "紫越软件事业部";

	//经费科目 (系统全局参照变量 的例子)
	@SuppressWarnings("serial")
	public static final Map<String,String> EQU_FUND_CODE = new LinkedHashMap<String,String>(){
		{put("10","经常经费");} 
		{put("20","科研经费");}
		{put("30","基建经费");}
		{put("40","自筹资金");}
		{put("41","外事收入");}
		{put("50","贷款资金");}
		{put("60","捐赠资金");}
		{put("70","专项经费");}
		{put("71","外交专项");}
		{put("80","资助基金");}
		{put("90","其他经费");}	
	};


	
}

