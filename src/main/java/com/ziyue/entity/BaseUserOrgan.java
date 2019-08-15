package com.ziyue.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户部门
 * @author 胡永强
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseUserOrgan implements java.io.Serializable {
	private String userid;//用户工号
	private String organid;//部门id
}