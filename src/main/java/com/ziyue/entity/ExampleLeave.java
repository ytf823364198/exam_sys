package com.ziyue.entity;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 请假申请
 * @author autoCode
 * @date 2018-4-16 15:09:02
 * @version V0.0.1
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExampleLeave implements java.io.Serializable {
	// 属性
	private String id; // 主键  
	private Integer day; // 请假天数  
	private String telphone; // 电话  
	private String email; // 邮件  
	private Integer age; // 年龄  
	private Double money; // 扣除薪水  
	private String leavedate; // 请假日期
	private String remark; // 备注说明  
	private String status; // 状态  
	private String sex; // 性别  
	private String appuserid; // 申请人  
	private String apptime;//申请时间
	
	/**工作流部分*/
	private String procinstid; // 流程实例ID  
	private String  taskname;//处理历史任务的名称
	private Date  instime;//处理历史任务的时间
	private String taskid;//当前任务ID
	private String outcome;//任务办理后流程走向
	private String info;//批注信息

	private List<BaseInspect> inspects; // 历史操作记录

	/** status状态的参照 */
	public static final Map<String,String> EX_LEAVE_STATUS = new LinkedHashMap<String,String>(){
		{put("0","未提交");}
		{put("1","审批中");}
		{put("-1","已否决");}
		{put("2","已审批");}
		{put("9","已作废");}
	};
	
	/** sex性别的参照 */
	public static final Map<String,String> EX_LEAVE_SEX = new LinkedHashMap<String,String>(){
		{put("F","女的");}
		{put("M","男的");}
	};
		
}