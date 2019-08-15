package com.ziyue.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.ziyue.service.ActivitiService;
import com.ziyue.service.ExLeaveService;
import com.ziyue.util.*;
import com.ziyue.entity.ExLeave;
import com.ziyue.entity.BaseUser;


@Controller
@RequestMapping("/lea")
public class ExLeaveController {
	@Autowired
	private ExLeaveService  exLeaveService;
	@Autowired
	private ActivitiService activitiService;
	
	@ModelAttribute("statuss")
	public Map<String, String> getStatuss(){
		return ExLeave.EX_LEAVE_STATUS;
	}
	
	@ModelAttribute("sexs")
	public Map<String, String> getSexs(){
		return ExLeave.EX_LEAVE_SEX;
	}
	
	/**
	 * 转向添加请假申请界面
	 */
	@RequestMapping("/turnAdd")
	public String turnAddExLeave() {
		return "lea/add";
	}
	
	/**
	 * 添加请假申请
	 * @param lea 请假申请对象
	 * @return HttpResult
	 */
	@RequestMapping("/add")
	public @ResponseBody HttpResult addExLeave(HttpServletRequest request,ExLeave lea){
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//添加请假申请
		exLeaveService.addExLeave(lea,user);
		return HttpResult.success();
	}

	/**
	 * 转向修改请假申请界面
	 * @param id 请假申请Id
	 */
	@RequestMapping("/turnModify/{id}")
	public String turnModifyExLeave(Model model,@PathVariable("id") String id) {
		model.addAttribute("lea", exLeaveService.findExLeaveDetailById(id));
		return "lea/modify";
	}
	
	/**
	 * 修改请假申请
	 * @param lea 请假申请对象
	 * @return HttpResult
	 */
	@RequestMapping("/modify")
	public @ResponseBody HttpResult modifyExLeave(HttpServletRequest request,ExLeave lea){
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//修改请假申请
		exLeaveService.modifyExLeave(lea);
		return HttpResult.success();
	}

	/**
	 * 通过Id删除请假申请
	 * @param id 请假申请Id
	 * @return HttpResult HttpResult
	 */
	@RequestMapping("/delete/{id}")
	public @ResponseBody HttpResult deleteExLeave(@PathVariable("id") String id){
		exLeaveService.delExLeaveById(id);
		return HttpResult.success();
	}

	/**
	 * 查询请假申请
	 * @param id 请假申请对象Id
	 */
	@RequestMapping("/detail/{id}")
	public String detailExLeaveById(Model model,@PathVariable("id") String id){
		ExLeave lea = exLeaveService.findExLeaveDetailById(id);
		model.addAttribute("lea", lea);
		if(null != lea && StringUtil.notEmpty(lea.getProcinstid())) {
			model.addAllAttributes(activitiService.findTaskCoordingByInstanceId(lea.getProcinstid()));
		}
		return "lea/detail";
	}

	/**
	 * 提交请假申请
	 * @param id 请假申请对象Id
	 * @return HttpResult
	 */
	@GetMapping("/commit/{id}")
	public @ResponseBody HttpResult commitExLeave(HttpServletRequest request,@PathVariable("id") String id){
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		exLeaveService.commitExLeave(id,user);
		return HttpResult.success();
	}

	/**
	 * 转向办理任务的界面
	 * @param taskId 任务Id
	 * @param id 请假申请对象Id
	 */	
	@RequestMapping("/turnTask")
	public String turnTask(Model model,String taskid,String id) {
		//获取工作流要处理的任务
		Task task = activitiService.findTaskById(taskid);
		//使用任务ID，查找请假申请的ID
		if(StringUtil.isEmpty(id)){		
			id = activitiService.findBillIdByTask(task);
		}
		//获取请假申请对象
		model.addAttribute("lea", exLeaveService.findExLeaveDetailById(id));
		model.addAttribute("task", task);
		//注意：有联合会签的节点 ,是不能获取出去的线
		if(!task.getTaskDefinitionKey().endsWith("JoinSign")   ){
			//根据任务ID，获取当前任务完成后的连线名称，在页面转成“按钮”
			model.addAttribute("outcomeList", activitiService.findOutComeListByTaskId(task));
		}
		
		//TODO 在这里判断,每个节点,此任务节点需要其他处理可在此编码
		if("TaskCommit".equals(task.getTaskDefinitionKey()) ){
			return "lea/do_task_commit";
		}
		
		return "lea/do_task";
	}
	
	/**
	 * 办理任务
	 * @param lea 请假申请对象
	 * @return HttpResult
	 */	
	@RequestMapping("/doTask")
	public @ResponseBody HttpResult doTask(HttpServletRequest request,ExLeave lea) {
		BaseUser user = (BaseUser) request.getSession().getAttribute("loginUser");
		if(!Token.validToken(request)){
			return HttpResult.error("请不要重复操作");
		}
		exLeaveService.doTask(user,lea);
		return HttpResult.success();
	}
	
	/**
	 * 分页查询未提交的请假申请
	 */
	@RequestMapping("/listUnCommit")
	public String listUnCommit(Model model,HttpServletRequest request) {
		//BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		//TODO 定义搜索条件
		//pagemodel.getSearch().put("appuserid", user.getId());
		pageModel.getSearch().put("status", "0");
		pageModel =  exLeaveService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "lea/list_uncommit";
	}
	
	/**
	 * 分页查询请假申请的待办事务
	 */
	@RequestMapping("/listRunTask")
	public String listRunTask(Model model,HttpServletRequest request) {
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		//"assignee"任务处理人，查询我需要处理的任务
		pageModel.getSearch().put("assignee",user.getId() );
		pageModel = exLeaveService.findRunTaskSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "lea/list_run_task";
	}
	
	/**
	 * 分页查询已办理的请假申请
	 */
	@RequestMapping("/listHisTask")
	public String listHisTask(Model model,HttpServletRequest request) {
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		//"insuserid"办理人，查询我历史办理的单据
		pageModel.getSearch().put("insuserid",user.getId() );
		pageModel = exLeaveService.findHisTaskSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel); 
		return "lea/list_his_task";
	}	

	/**
	 * 分页查询请假申请
	 */
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		PageModel pageModel = PageModel.pageModel(request);
		pageModel = exLeaveService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "lea/list";
	}
	
	/**
	 * 按照条件导出请假申请
	 */	
	@RequestMapping("/exp")
	public void expExLeave(HttpServletRequest request,HttpServletResponse response) throws Exception{
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		//指定要导出的文件,导出文件放到/attached/temp文件夹下，系统会自动清除
		String expFile = "/attached/temp/" + ExLeave.class.getSimpleName()+"_" + DateUtil.getDateLong()+"_"+user.getId()+".xlsx";
		int total = exLeaveService.expExLeave(pageModel,expFile);
		if(total < 1){
			FileLoad.downFail(response, null,total);
		}else{
			FileLoad.download(expFile,"请假申请.xlsx",request,response);	
		}
	}
}
