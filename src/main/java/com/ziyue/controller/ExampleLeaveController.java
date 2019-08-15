package com.ziyue.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ziyue.entity.BaseUser;
import com.ziyue.entity.ExampleLeave;
import com.ziyue.service.ActivitiService;
import com.ziyue.service.ExampleLeaveService;
import com.ziyue.util.DateUtil;
import com.ziyue.util.FileLoad;
import com.ziyue.util.HttpResult;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;
import com.ziyue.util.Token;

@Controller
@RequestMapping("/leave")
public class ExampleLeaveController {
	@Autowired
	private ExampleLeaveService exampleLeaveService;
	@Autowired
	private ActivitiService activitiService;
	
	@ModelAttribute("statuss")
	public Map<String, String> getStatuss(){
		return ExampleLeave.EX_LEAVE_STATUS;
	}
	
	@ModelAttribute("sexs")
	public Map<String, String> getSexs(){
		return ExampleLeave.EX_LEAVE_SEX;
	}
	
	/**
	 * 转向添加请假申请界面
	 */
	@RequestMapping("/turnAdd")
	public String turnAddExampleLeave() {
		return "example/leave/add";
	}
	
	/**
	 * 添加请假申请
	 * @param  lea 请假申请对象
	 * @return HttpResult
	 */
	@RequestMapping("/add")
	public @ResponseBody HttpResult addExampleLeave(HttpServletRequest request,ExampleLeave lea){
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//添加请假申请
		exampleLeaveService.addExampleLeave(lea ,user);
		return HttpResult.success();
	}

	/**
	 * 转向修改请假申请界面
	 * @param id 请假申请Id
	 */
	@RequestMapping("/turnModify/{id}")
	public String turnModifyExampleLeave(Model model,@PathVariable("id") String id) {
		model.addAttribute("lea", exampleLeaveService.findExampleLeaveDetailById(id));
		return "example/leave/modify";
	}

	/**
	 * 修改请假申请
	 * @param lea 请假申请对象
	 * @return HttpResult
	 */
	@RequestMapping("/modify")
	public @ResponseBody HttpResult modifyExampleLeave(HttpServletRequest request,ExampleLeave lea){
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("重复提交");
		}
		//修改请假申请
		exampleLeaveService.modifyExampleLeave(lea);
		return HttpResult.success();
	}

	/**
	 * 通过Id删除请假申请
	 * @param id 请假申请Id
	 * @return HttpResult
	 */
	@RequestMapping("/delete/{id}")
	public @ResponseBody HttpResult deleteExampleLeave(@PathVariable("id") String id){
		exampleLeaveService.delExampleLeaveById(id);
		return HttpResult.success();
	}
	
	/**
	 * 查询请假申请
	 * @param id 请假申请对象Id
	 */
	@RequestMapping("/detail/{id}")
	public String detailExampleLeave(Model model,@PathVariable("id") String id){
		ExampleLeave leave = exampleLeaveService.findExampleLeaveDetailById(id);
		model.addAttribute("lea", leave);
		if(null != leave && StringUtil.notEmpty(leave.getProcinstid())) {
			model.addAllAttributes(activitiService.findTaskCoordingByInstanceId(leave.getProcinstid()));
		}
		return "example/leave/detail";
	}
	
	/**
	 * 提交请假申请
	 * @param id 请假申请对象Id
	 * @return (error:错误;ok:成功)
	 */
	@GetMapping("/commit/{id}")
	public @ResponseBody HttpResult commitExampleLeave(HttpServletRequest request,@PathVariable("id") String id){
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		exampleLeaveService.commitExampleLeave(id,user);
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
		if(null == id || "".equals(id)){		
			id = activitiService.findBillIdByTask(task);
		}
		//获取请假申请对象
		model.addAttribute("lea", exampleLeaveService.findExampleLeaveDetailById(id));
		model.addAttribute("task", task);
		//注意：有联合会签的节点 ,是不能获取出去的线
		if(!task.getTaskDefinitionKey().endsWith("JoinSign")   ){
			//根据任务ID，获取当前任务完成后的连线名称，在页面转成“按钮”
			model.addAttribute("outcomeList", activitiService.findOutComeListByTaskId(task));
		}
		
		//在这里判断,每个节点,此任务节点需要其他处理可在此编码
		if("TaskCommit".equals(task.getTaskDefinitionKey()) ){
			return "example/leave/do_task_commit";
		}
		
		return "example/leave/do_task";
	}
	
	/**
	 * 办理任务
	 * @param lea 请假申请对象
	 * @return HttpResult
	 */	
	@RequestMapping("/doTask")
	public @ResponseBody HttpResult doTask(HttpServletRequest request,ExampleLeave lea) {
		BaseUser user = (BaseUser) request.getSession().getAttribute("loginUser");
		if(!Token.validToken(request)){
			return HttpResult.error("重复提交");
		}
		exampleLeaveService.doTask(user,lea);
		return HttpResult.success();
	}
	
	/**
	 * 分页查询未提交的请假申请
	 */
	@RequestMapping("/listUnCommit")
	public String listUnCommit(Model model,HttpServletRequest request) {
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		pageModel.getSearch().put("appuserid", user.getId());
		pageModel.getSearch().put("status", "0");
		pageModel =  exampleLeaveService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "example/leave/list_uncommit";
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
		pageModel =  exampleLeaveService.findRunTaskSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "example/leave/list_run_task";
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
		pageModel =  exampleLeaveService.findHisTaskSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel); 
		return "example/leave/list_his_task";
	}	
	 
	/**
	 * 分页查询请假申请
	 */
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		PageModel pageModel = PageModel.pageModel(request);
		pageModel =  exampleLeaveService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "example/leave/list";
	}
	
	/**
	 * 按照条件导出请假申请
	 */	
	@RequestMapping("/exp")
	public void expExampleLeave(HttpServletRequest request,HttpServletResponse response) throws Exception{
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		//指定要导出的文件,导出文件放到/attached/temp文件夹下，系统会自动清除
		String expFile = "/attached/temp/" + ExampleLeave.class.getSimpleName()+"_" + DateUtil.getDateLong()+"_"+user.getId()+".xlsx";
		int total = exampleLeaveService.expExampleLeave(pageModel,expFile);
		if(total < 1){
			FileLoad.downFail(response, null,total);
		}else{
			FileLoad.download(expFile,"请假申请.xlsx",request,response);	
		}
	}
	
}
