package com.ziyue.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * BaseNotice entity.
 * 系统通知公告表
 * @since 2012/06/22
 * @version 1.0.1
 * @author 胡永强
 */

@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseNotice implements java.io.Serializable {
	
	private String id;
	private String title;
	private String type;
	private String content;//内容
	private String adduserid;
	private String adduser;
	private String adddate;//(DateUtil.fullTime() )
	private String iswarn;//是否警示（y是，n否）
	private String warndate;//警示过期日期,过期后 iswarn=“n”

	public static Map<String,String> NOTICE_TYPE = new LinkedHashMap<String,String>(){
		{put("1","通知公告");}
		{put("2","最新消息");}
		{put("3","规章制度");}
		{put("4","下载专区");}
		{put("9","其他");}
	};

	
}