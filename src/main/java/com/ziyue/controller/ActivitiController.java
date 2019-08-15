package com.ziyue.controller;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ziyue.service.ActivitiService;
import com.ziyue.util.FileLoad;
import com.ziyue.util.HttpResult;
import com.ziyue.util.Token;

@Controller
@RequestMapping("/activiti")
public class ActivitiController {
	@Autowired
	private ActivitiService activitiService;

	@RequestMapping("/listDeploy")
	public String listDeploy(Model model) {
		//1:查询部署对象信息，对应表（act_re_deployment）
		List<Deployment> depList = activitiService.findDeploymentList();
		//2:查询流程定义的信息，对应表（act_re_procdef）
		List<ProcessDefinition> pdList = activitiService.findProcessDefinitionList();
		//放置到上下文对象中
		model.addAttribute("depList", depList);
		model.addAttribute("pdList",pdList);
		return "activiti/list_deploy";
	}
	
	@RequestMapping("/turnAddDeploy")
	public String turnAddDeploy() {
		return "activiti/add_deploy";
	}
	
	@RequestMapping("/addDeploy")
	public @ResponseBody HttpResult addDeploy(HttpServletRequest request,String flowname,MultipartFile zipFile) {	
		if(Token.validToken(request)) {
			String zipPath = FileLoad.upLoadTemp(zipFile);
			File file =  new File(zipPath);
			activitiService.addDeploye(file, flowname);
		}
		return HttpResult.success();
	}
	
	//使用部署对象ID，删除流程定义
	@RequestMapping("/delDeploy")
	public @ResponseBody HttpResult delDeploy(String deploymentId) {
		activitiService.deleteProcessDefinitionByDeploymentId(deploymentId);
		return HttpResult.success();
	}
	
	//使用部署对象ID，删除流程定义
	@RequestMapping("/delInstance")
	public @ResponseBody HttpResult delInstance(String processInstanceId) {
		activitiService.deleteProcessInstanceByProcessInstanceId(processInstanceId);
		return HttpResult.success();	
	}
	
	
	@RequestMapping("/viewImage")
	public void  viewImage(String deploymentId,String imageName,HttpServletResponse response) {
			response.setContentType("image/png"); 
			//获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
			InputStream in = activitiService.findImageInputStream(deploymentId,imageName);
			//InputStream in = activitiService.findImageInputStream(processDefinitionId, processInstanceId,taskId);
			//InputStream in = activitiService.findImageInputStream(processDefinitionkey);
			//从response对象获取输出流
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				//4：将输入流中的数据读取出来，写到输出流中
				for(int b=-1;(b=in.read())!=-1;){
					out.write(b);
				}
				out.close();
				in.close();
				//将图写到页面上，用输出流写
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 查看当前流程图（查看当前活动节点，并使用红色的框标注）
	 */
	@RequestMapping("/viewCurrentImage")
	public String  viewCurrentImageByProcInstId(Model model,String procinstid) {
		model.addAllAttributes(activitiService.findTaskCoordingByInstanceId(procinstid));
		return "activiti/current_image";
	}

	/**
	 * 打开任务表单
	 */
	@RequestMapping("/turnTaskForm")
	public String turnTaskForm(String taskid,String id ) {
		//获取任务表单中任务节点的url连接
		String url = activitiService.findTaskFormKeyByTaskId(taskid);
		url = url+ "?taskid="+taskid+"&id="+id ;
		return "redirect:"+url;
	}
	
}
