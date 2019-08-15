package com.ziyue.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ziyue.activiti.ProcessInstanceDiagramCmd;
import com.ziyue.dao.ActivitiDao;
import com.ziyue.dao.BaseUserDao;
import com.ziyue.entity.BaseUser;
import com.ziyue.util.CollectUtil;

@SuppressWarnings("rawtypes")
@Service
public class ActivitiService  {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private ActivitiDao activitiDao;
	@Autowired
	private BaseUserDao baseUserDao;
	
	/**
	 * 部署流程定义
	 * @param file 部署文件
	 * @param filename 部署名称
	 */
	public void addDeploye(File file, String filename){
			try {
				//2：将File类型的文件转化成ZipInputStream流
				ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
				repositoryService.createDeployment()//创建部署对象
								.name(filename)//添加部署名称
								.addZipInputStream(zipInputStream)//
								.deploy();//完成部署
				zipInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	/**查询部署对象信息，对应表（act_re_deployment）*/
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery()//创建部署对象查询
							//.orderByDeploymenTime().asc()//
							.orderByDeploymentName().asc()
							.list();
		return list;
	}
	
	/**查询流程定义的信息，对应表（act_re_procdef）*/
	public List<ProcessDefinition> findProcessDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
							.orderByProcessDefinitionKey().asc()
							.orderByProcessDefinitionVersion().asc()//
							.list();
		return list;
	}
	
	/**使用部署对象ID和资源图片名称，获取图片的输入流*/
	public InputStream findImageInputStream(String deploymentId,String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}
	
	/**使用流程定义key，获取图片的输入流*/
	public InputStream findImageInputStream(String processDefinitionKey){
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
				.processDefinitionKey(processDefinitionKey)
				.orderByProcessDefinitionVersion().desc()
				.list();
		if(null != list && list.size()> 0){
			ProcessDefinition pd = list.get(0);
			return repositoryService.getResourceAsStream(pd.getDeploymentId(),pd.getDiagramResourceName());
		}
		return null;
	}
	
	public InputStream findImageInputStream(String processDefinitionId,String processInstanceId, String taskId){
		Command<InputStream> cmd = null;
		if(null != processDefinitionId ){
			cmd = new GetDeploymentProcessDiagramCmd(processDefinitionId);
		}
		if(null != processInstanceId ){
			cmd = new ProcessInstanceDiagramCmd(processInstanceId);
		}
		if(null != taskId ){
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult(); 
			cmd = new ProcessInstanceDiagramCmd(  task.getProcessInstanceId() );
		}
		InputStream is = null;
		if (cmd != null) {    
           is = managementService.executeCommand(cmd);
		}  
		return is;
		
	}
	
	/**使用部署对象ID，删除流程定义*/
	public void deleteProcessDefinitionByDeploymentId(String deploymentId) {
		//普通删除，如果当前规则下有正在执行的流程，则抛异常
		//repositoryService.deleteDeployment(deploymentId);
		// 级联删除,会删除和当前规则相关的所有信息，包括历史
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	/**使用流程实例ID，删除流程实例*/	
	public void deleteProcessInstanceByProcessInstanceId(String processInstanceId){
		try{
	
			ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//使用流程实例ID查询
					.singleResult();
			if(pi != null){//流程没有结束,结束了删除会报异常
				//删流程实例
				runtimeService.deleteProcessInstance(processInstanceId, "");
				//删任务
				List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
				if(null != tasks && tasks.size() > 0){
					for(Task task : tasks){
						taskService.deleteTask(task.getId());
					}
				}
			}
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}catch(Exception e){
			
		}
	}
	
	/**2：使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>*/
	public List<Task> findTaskListById(String userId,String processDefinitionKey) {
		List<Task> list = taskService.createTaskQuery()//
					.taskAssignee(userId)//指定个人任务查询
					.processDefinitionKey(processDefinitionKey)
					.orderByTaskCreateTime().asc()//
					.list();
		return list;
	}
	
	
	
	/**1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象*/
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//查询流程定义的对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef 
					.processDefinitionId(processDefinitionId)//使用流程定义ID查询
					.singleResult();
		return pd;
	}
	
	public ProcessDefinition findProcessDefinitionByProcessInstanceId(String processInstanceId){
		ProcessInstance ProcessInstance = runtimeService.createProcessInstanceQuery()
		.processInstanceId(processInstanceId)
		.singleResult();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef 
				.processDefinitionId(ProcessInstance.getProcessDefinitionId())//使用流程定义ID查询
				.singleResult();
		return pd;
	}
	
	public ProcessDefinition findProcessDefinitionById(String processDefinitionId){
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId)
				.singleResult();
		return pd;
	}
	
	public ProcessInstance findProcessInstanceById(String processInstanceId){
		return	runtimeService.createProcessInstanceQuery()
							.processInstanceId(processInstanceId)
							.singleResult();
	}
	
	public HistoricProcessInstance findHistoricProcessInstanceById(String processInstanceId){
		return	historyService.createHistoricProcessInstanceQuery()
							.processInstanceId(processInstanceId)
							.singleResult();
	}
	
	/**
	 * 二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中
		 map集合的key：表示坐标x,y,width,height
		 map集合的value：表示坐标对应的值
	 */
	public Map<String, Object> findCoordingByTask(String taskId) {
		//存放坐标
		Map<String, Object> map = new HashMap<String,Object>();
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		//获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = 
				(ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		
		//流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
					.processInstanceId(processInstanceId)//使用流程实例ID查询
					.singleResult();
		//获取当前活动的ID
		String activityId = pi.getActivityId();
		//获取当前活动对象
		@SuppressWarnings("unused")
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
		//获取坐标
		map.put("taskdefkey", activityId);
		
		List<String> assignees = new ArrayList<String>();;
		List<BaseUser> users = activitiDao.findAssigneeByDefKeyProcInstId(processInstanceId, activityId);
		if(null != users && users.size() >0 ){
			for(BaseUser user : users ){
				if(null != user.getRealname() && !"".equals(user.getRealname())){
					assignees.add(user.getRealname());
				}else if (null != user.getUsername() && !"".equals(user.getUsername()) ){
					if(user.getUsername().indexOf(",") > 0){
						String assigneet = "";  
						String [] userids = user.getUsername().split(",");
						if(null != userids && userids.length >0 ){
							for(String userid : userids ){
								String uname = baseUserDao.findRealNameById(userid);
								if(null == uname ){
									uname = "工号"+userid;
								}
								assigneet = assigneet + uname +",";
							}
						}
						assignees.add(assigneet.substring(0, assigneet.length()-1 ));
					}else{
						assignees.add("工号"+ user.getUsername() );
					}
				}
			}
		}
		
		
		String assignee = "";
		if(null != assignees && assignees.size() == 1){
			assignee = assignees.get(0);
			if(assignee.length() > 5 ){
				assignee = assignee.substring(0,5)+"...";
			}
			
			
		}else if(null != assignees && assignees.size() > 1){
			assignee = assignees.get(0);
			if(assignee.length() > 5 ){
				assignee = assignee.substring(0,5)+" 等";
			}else{
				assignee = assignee+" 等";
			}
		}
		map.put("assignee", assignee);
		
		String assigneetitle = CollectUtil.ListToString(assignees);
		if(null != assigneetitle && !"".equals(assigneetitle) ){
			map.put("assigneetitle", assigneetitle);
		}
		
		return map;
	}
	
	
	/**
	 * 二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中
		 map集合的key：表示坐标x,y,width,height
		 map集合的value：表示坐标对应的值
	 */
	public List<Map<String, Object>> findCoordingByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = this.findTaskByprocessInstanceId(processInstanceId);
		if(null == tasks || tasks.size() < 1 ){
			return null;
		}
		//获取流程定义的ID
		String processDefinitionId = tasks.get(0).getProcessDefinitionId();
		//获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = 
				(ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		//获取当前活动对象
		Set<String> activityIds = new HashSet<String>();
		Map<String,String> taskNames = new HashMap<String,String>();
		for(Task task : tasks){
			activityIds.add(task.getTaskDefinitionKey());
			taskNames.put(task.getTaskDefinitionKey(), task.getName());
		}
		if(null != activityIds && activityIds.size() >0 ){
			for(String activityId : activityIds){
				
				ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("x", activityImpl.getX());
				map.put("y", activityImpl.getY());
				map.put("width", activityImpl.getWidth());
				map.put("height", activityImpl.getHeight());
				map.put("taskdefkey", activityId);
				map.put("taskname", taskNames.get(activityId));

				List<String> assignees = new ArrayList<String>();;
				List<BaseUser> users = activitiDao.findAssigneeByDefKeyProcInstId(processInstanceId, activityId);
				if(null != users && users.size() >0 ){
					for(BaseUser user : users ){
						if(null != user.getRealname() && !"".equals(user.getRealname())){
							assignees.add(user.getRealname());
						}else if (null != user.getUsername() && !"".equals(user.getUsername()) ){
							if(user.getUsername().indexOf(",") > 0){
								String assigneet = "";  
								String [] userids = user.getUsername().split(",");
								if(null != userids && userids.length >0 ){
									for(String userid : userids ){
										String uname = baseUserDao.findRealNameById(userid);
										if(null == uname ){
											uname = "账号"+userid;
										}
										assigneet = assigneet + uname +",";
									}
								}
								assignees.add(assigneet.substring(0, assigneet.length()-1 ));
							}else{
								assignees.add("【账号"+ user.getUsername()+"】" );
							}
						}
					}
				}
				
				
				String assignee = "";
				if(null != assignees && assignees.size() == 1){
					assignee = assignees.get(0);
					if(assignee.length() > 5 ){
						assignee = assignee.substring(0,5)+"...";
					}
				}else if(null != assignees && assignees.size() > 1){
					assignee = assignees.get(0);
					if(assignee.length() > 5 ){
						assignee = assignee.substring(0,5)+" 等";
					}else{
						assignee = assignee+" 等";
					}
				}
				map.put("assignee", assignee);
				
				String assigneetitle = CollectUtil.ListToString(assignees);
				if(null != assigneetitle && !"".equals(assigneetitle) ){
					map.put("assigneetitle", assigneetitle);
				}
				
				maps.add(map);
			}
		}
		return maps;

	}
	
	
	/**使用任务ID，获取当前任务节点中对应的Form key中的连接的值*/
	public String findTaskFormKeyByTaskId(String taskId) {
		TaskFormData formData = formService.getTaskFormData(taskId);
		//获取Form key的值
		String url = formData.getFormKey();
		return url;
	}
	
	
	/**一：使用任务ID，查找单据ID*/
	public String findBillIdByTaskId(String taskId) {
		//1：使用任务ID，查询任务对象Task
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		return this.findBillIdByTask(task);
	}
	
	/**一：使用任务ID，查找单据ID*/
	public String findBillIdByTask(Task task) {
		//2：使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.singleResult();
		//4：使用流程实例对象获取BUSINESS_KEY
		return pi.getBusinessKey();
	}
	
	public Task findTaskById(String taskId) {
		//1：使用任务ID，查询任务对象Task
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		return task;
	}	
	
	/**二：已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中*/
	public List<String> findOutComeListByTaskId(Task task) {
		//返回存放连线的名称集合
		List<String> list = new ArrayList<String>();
		//2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//3：查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//使用流程实例ID查询
					.singleResult();
		//获取当前活动的id
		String activityId = pi.getActivityId();
		//4：获取当前的活动
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		//5：获取当前活动完成之后连线的名称
		List<PvmTransition> pvmList = null;
		try{		
			pvmList =  activityImpl.getOutgoingTransitions();
				if(pvmList!=null && pvmList.size()>0){
					for(PvmTransition pvm:pvmList){
						String name = (String) pvm.getProperty("name");
						if(StringUtils.isNotBlank(name)){
							list.add(name);
						}else{
							list.add("确定");
						}
					}
				}
		}catch(Exception e){
			
		}
		
		Collections.sort(list,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if(o1 == null || o2 == null){
                    return -1;
                }
                if(o1.compareTo(o2) > 0){
                    return 1;
                }
                if(o1.compareTo(o2) < 0){
                    return -1;
                }
                if(o1.compareTo(o2) == 0){
                    return 0;
                }
                return 0;
            }
        });
		
		return list;
	}

	
	/**获取批注信息，传递的是当前任务ID，获取历史任务ID对应的批注*/
	public List<Comment> findCommentByTaskId(String taskId) {
		List<Comment> list = new ArrayList<Comment>();
		//使用当前的任务ID，查询当前流程对应的历史任务ID
		//使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
//		//使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
//		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
//						.processInstanceId(processInstanceId)//使用流程实例ID查询
//						.list();
//		//遍历集合，获取每个任务ID
//		if(htiList!=null && htiList.size()>0){
//			for(HistoricTaskInstance hti:htiList){
//				//任务ID
//				String htaskId = hti.getId();
//				//获取批注信息
//				List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
//				list.addAll(taskList);
//			}
//		}
		list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}
	
	/**通过流程实例查任务*/
	public List<Task> findTaskByprocessInstanceId(String processInstanceId){
		return taskService.createTaskQuery()//
		.processInstanceId(processInstanceId)
		.list();
	}

	public List findLastVersionProcdefs() {
		return activitiDao.findLastVersionProcdefs();
	}

	public Map<String,Object> findTaskCoordingByInstanceId(String procinstid) {
		Map<String,Object> map = new HashMap<String,Object>();
		ProcessInstance pi = this.findProcessInstanceById(procinstid);
		if(null  == pi ){//流程已经结束
			 HistoricProcessInstance hpi = this.findHistoricProcessInstanceById(procinstid);
			 ProcessDefinition pd = this.findProcessDefinitionById(hpi.getProcessDefinitionId());
			 map.put("pd", pd);
		}else{
			//1：获取流程实例ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
			ProcessDefinition pd = this.findProcessDefinitionByProcessInstanceId(procinstid);
			map.put("pd", pd);
			map.put("coords", this.findCoordingByProcessInstanceId(procinstid));
		}
		return map;
	}
	
	
}
