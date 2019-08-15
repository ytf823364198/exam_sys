package com.ziyue.activiti.listener;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ziyue.entity.BaseUser;
import com.ziyue.service.BaseUserService;
import com.ziyue.util.SpringContextHolder;
/**
 * 管理员
 * @author huyq
 * 行政
 */
@SuppressWarnings("serial")
public class GlobalXZHandler implements TaskListener {
	@Override
	public void notify(DelegateTask delegateTask) {
		//String organid = (String)delegateTask.getVariable("organid");
		BaseUserService baseUserService = SpringContextHolder.getBean("baseUserService");
		List<BaseUser>  boss = baseUserService.findUserByRoleId("xzjl");
		if(null != boss && boss.size() >0 ){
			for(BaseUser user : boss){
				delegateTask.addCandidateUser(user.getId());
			}
		}else if(null != boss && boss.size() == 0 ){
			delegateTask.setAssignee(boss.get(0).getId());
		}else {
			delegateTask.setAssignee("admin");
		}
	}

}
