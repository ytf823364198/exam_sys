package com.ziyue.entity;

import com.ziyue.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统操作记录
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseInspect implements java.io.Serializable {
	// Fields
	private String objectid;//关联业务id
	private String userid;	//操作用户ID
	private String realname; //操作用户
	private String result;		//审核状态
	private String info;		//审核意见
	private String opttime;		//操作时间
	private String flag;		//单据的分类标志  
	private String taskdefkey;  //流程定义key， 也就是任务节点名称
	private String taskname;	//任务节点
	private String isbpm;		//是否是工作流节点

	/**
	 * 构建流程节点记录
	 * @param String objectid 关联业务id
	 * @param BaseUser user 操作用户
	 * @param result 审核状态（通过、否决、提交）
	 * @param info 审核意见
	 * @param flag 单据的分类标志 （一般是类名）
	 * @param taskname 任务节点  
	 * @param taskdefkey 流程定义key， 也就是任务节点名称
	 */
	public BaseInspect(String objectid, BaseUser user, String result, String info, String flag, String taskname,String taskdefkey) {
		super();
		this.isbpm = "n";
		if(null != taskdefkey && !"".equals(taskdefkey)) {
			this.isbpm = "y";
		}
		this.objectid = objectid;
		this.userid = user.getId();
		this.realname = user.getRealname();
		this.result = result;
		this.info = info;
		this.opttime = DateUtil.fullTime();
		this.flag = flag;
		this.taskname = taskname;
		System.out.println(this);
	}

	
	
}