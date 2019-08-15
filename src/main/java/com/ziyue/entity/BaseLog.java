package com.ziyue.entity;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Log 用户日志
 * @since 2015/5/31
 * @version 1.0.1
 * @author 胡永强
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseLog implements java.io.Serializable {

	private String id;
	private String userid; //操作人工号
	private String realname; //操作人姓名
	private String action; //执行动作 (URL路径)
	private String ip; //客户端IP地址
	private String browser; //浏览器版本
	private String opttime;//操作时间
	private String result;  //执行结果( SUCCESS成功 ,FAILURE失败)
	
	public static final Map<String,String> FIELD = new LinkedHashMap<String,String>(){
		{put("id","id");}
		{put("userid","操作人工号");}
		{put("realname","操作人姓名");}
		{put("action","执行动作");}
		{put("ip","客户端IP");}
		{put("browser","浏览器版本");}
		{put("opttime","操作时间");}
		{put("result","执行结果");}

	};

}