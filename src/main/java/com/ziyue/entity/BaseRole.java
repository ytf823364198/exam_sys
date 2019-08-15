package com.ziyue.entity;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
 * @author 胡永强
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class BaseRole implements java.io.Serializable {
	// Fields
	private String id;
	private String rolename;
	private String inlay;//是否系统内置(y是，n否)
	private String organ;//是否关联部门 (y是，n否 )
	private String sort;//排序号
	private List<BaseMenu> menus; //角色关联的菜单

}