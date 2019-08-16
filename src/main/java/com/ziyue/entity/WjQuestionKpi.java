package com.ziyue.entity;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 题目表
 * @author autoCode
 * @date 2019-8-15 16:26:53
 * @version V0.0.1
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WjQuestionKpi implements java.io.Serializable {
	// 属性
	private String oid; // 题库类型编号  
	private String content; // 题目内容  
	private String qtype; // 题目类型  
	private String seq; // 题目序号  
	private String remark; // 题目备注  
	private String score; // 题目分数  
	private String rightvalue; // 正确答案  
	private String id; // 编号   

}