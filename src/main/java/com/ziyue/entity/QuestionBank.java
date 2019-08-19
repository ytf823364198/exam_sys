package com.ziyue.entity;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 题库信息
 * @author autoCode
 * @date 2019-8-15 13:56:02
 * @version V0.0.1
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionBank implements java.io.Serializable {
	// 属性
	private String id; // 题库序号  
	private String text; // 题库名称  
	private Date creattime; // 创建时间  
	private String remark; // 试卷备注  
	private String type; // 试卷类型  
	private String pid;
	private List<QuestionBank> nodes = new ArrayList<>();
	public QuestionBank(String id, String pid, String text) {
	        this.id = id;
	        this.pid = pid;
	        this.text = text;
	    }
}