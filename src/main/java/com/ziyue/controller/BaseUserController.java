package com.ziyue.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ziyue.entity.BaseUser;
import com.ziyue.entity.BaseUserOrgan;
import com.ziyue.entity.BaseUserRole;
import com.ziyue.service.BaseOrganService;
import com.ziyue.service.BaseRoleService;
import com.ziyue.service.BaseUserService;
import com.ziyue.util.HttpResult;
import com.ziyue.util.PageModel;
@Controller
public class BaseUserController {
    @Autowired
    BaseUserService baseUserService;
    @Autowired
    BaseRoleService baseRoleService;
    @Autowired
    BaseOrganService baseOrganService;
    
    @ModelAttribute("statuss")
	public Map<String, String> getStatuss(){
		return BaseUser.BASE_USER_STATUS;
	}
    
    @ModelAttribute("types")
 	public Map<String, String> getType(){
 		return BaseUser.BASE_USER_TYPE;
 	}
    
    @ModelAttribute("putmails")
 	public Map<String, String> getPutmail(){
 		return BaseUser.BASE_USER_PUTMAIL;
 	}
    
    @ModelAttribute("putmss")
   	public Map<String, String> getPutms(){
   		return BaseUser.BASE_USER_PUTMS;
   	}

    
	@GetMapping(value = "/user/turnAdd")
	@RequiresPermissions("user:list")
	public  String turnAddUser(Model model){
		model.addAttribute("roles",baseRoleService.findRoles());
		model.addAttribute("porgans",baseOrganService.findPOrgans());
		return "base/user/add";	
	}
    
	@GetMapping(value = "/user/turnModify/{id}")
	public  String turnModifyUser(@PathVariable("id") String id,Model model){
		model.addAttribute("user", baseUserService.findUserById(id));
		model.addAttribute("roles",baseRoleService.findRoles());
		model.addAttribute("porgans",baseOrganService.findPOrgans());
		return "base/user/modify";	
	}
	
	@GetMapping(value = "/user/find/{id}")
	@ResponseBody
	@RequiresPermissions("user:list")
    public  HttpResult findUserById(@PathVariable("id") String id,Model model){
		model.addAttribute("user", baseUserService.findUserById(id));
		return HttpResult.success(model);
    }
	
	
	@RequestMapping(value = "/user/list")
	@RequiresPermissions("user:list")
    public  String list(HttpServletRequest request,Model model){
		PageModel pagemodel = PageModel.pageModel(request);
		pagemodel = baseUserService.findBaseSplitPage(pagemodel);	
		model.addAttribute("porgans", baseOrganService.findPOrgans());
		model.addAttribute("roles", baseRoleService.findRoles());
		model.addAttribute("pageModel", pagemodel);
		return "base/user/list";
    }
	

	@PostMapping(value = "/user/add")
	@ResponseBody
	public  HttpResult addUser(HttpServletRequest request,BaseUser user){
		user.setId(user.getUsername());
		if(baseUserService.findUserIsExit(user.getId(), user.getId()) > 0 ) {
			return HttpResult.error("用户账号"+user.getId()+ "已经存在");
		}
		this.setUserOrganAndRole(request, user);
		baseUserService.addUser(user);
		return HttpResult.success();
	}
	
	@PostMapping(value = "/user/modify")
	@ResponseBody
	public  HttpResult modifyUser(HttpServletRequest request,BaseUser user){
		user.setId(user.getUsername());
		if(baseUserService.findUserIsExit(user.getId(), user.getId()) > 0 ) {
			return HttpResult.error("用户账号"+user.getId()+ "已经存在");
		}
		this.setUserOrganAndRole(request, user);
		baseUserService.modifyUser(user);
		return HttpResult.success();
	}
	
	@GetMapping(value = "/user/delete/{id}")
	@ResponseBody
	public  HttpResult delUser(@PathVariable("id") String id){
		baseUserService.delUserById(id);
		return HttpResult.success();
	}
	
	private void setUserOrganAndRole(HttpServletRequest request,BaseUser user) {
		String roleids[] = request.getParameterValues("roleid");
		String organids[] = request.getParameterValues("organid");
		if(null != roleids && roleids.length > 0 ) {
			List<BaseUserRole> userRoles = new ArrayList<BaseUserRole>();
			for(String roleid : roleids) {
				userRoles.add(new BaseUserRole(user.getId(),roleid) );
			}
			user.setRoles(userRoles);
		}
		
		if(null != organids && organids.length > 0 ) {
			List<BaseUserOrgan> userOrgans = new ArrayList<BaseUserOrgan>();
			for(String organid : organids) {
				userOrgans.add(new BaseUserOrgan(user.getId(),organid) );
			}
			user.setUorgans(userOrgans);
		}
	}

}
