package com.ziyue.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 组织机构 (部门)entity.
 * 
 * @author 胡永强
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class BaseOrgan implements java.io.Serializable {
	// Fields
	private String id;
	private String organname;
	private String pid;//0一级部门  非0二级部门
	
}