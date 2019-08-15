package com.ziyue.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 大文本字段
 * @since 2018/09/02
 * @version 1.0.1
 * @author 胡永强
 */

@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseClob implements java.io.Serializable {
	private String objectid;//类id
	private String attrkey;//变量 格式：类名.字段名 Notice.content
	private String attrval;//变量值 clob 存的值
}