package com.ziyue.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 用户 entity.
 * @author 胡永强
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class BaseUser implements java.io.Serializable {
	// Fields
	private String id;		//唯一ID
	private String username;//用户工号
	private String password;//备用密码
	private String realname;//中文名
	private String telphone; //办公电话
	private String mobile; //手机
	private String email; //邮件
	private String type;//js教职工，xs学生
	private String tutorid;//导师工号
	private String tutor;//导师姓名
	private String putmail;//接收邮件 n不接收 y接收  默认接收
	private String putms;//接收短信 n不接收 y接收  默认不接收
	private String status;// y可用，n禁用
	
	private List<BaseUserOrgan> uorgans;//用户部门
	private List<BaseOrgan> porgans;//用户部门
	private List<BaseUserRole> roles;//用户角色集合

	public BaseUser(String id, String realname) {
		super();
		this.id = id;
		this.username = id ;
		this.realname = realname;
	}
	
	/** type类别的参照 */
	public static final Map<String,String> BASE_USER_TYPE = new LinkedHashMap<String,String>(){
		{put("js","教师");}
		{put("xs","学生");}
	};
	
	/** 接收邮件 */
	public static final Map<String,String> BASE_USER_PUTMAIL = new LinkedHashMap<String,String>(){
		{put("y","接收");}
		{put("n","不接收");}
	};
	
	/** 接收短信 */
	public static final Map<String,String> BASE_USER_PUTMS = new LinkedHashMap<String,String>(){
		{put("y","接收");}
		{put("n","不接收");}
	};
	
	/** 用户状态 */
	public static final Map<String,String> BASE_USER_STATUS = new LinkedHashMap<String,String>(){
		{put("y","可用");}
		{put("n","禁用");}
	};


	
}