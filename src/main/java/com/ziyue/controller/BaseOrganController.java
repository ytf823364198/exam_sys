package com.ziyue.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ziyue.entity.BaseOrgan;
import com.ziyue.service.BaseOrganService;
import com.ziyue.util.HttpResult;
import com.ziyue.util.Token;

@Controller
@RequestMapping("/organ")
public class BaseOrganController  {
	@Autowired
	private BaseOrganService baseOrganService;
	
	@RequestMapping("/list")
	public  String list(Model model) {
		model.addAttribute("organs",baseOrganService.findOrgans());
		return "base/organ/list";
	}
	
	@RequestMapping("/turnAdd")
	public String turnAdd(Model model) {
		model.addAttribute("porgans", baseOrganService.findPOrgans());
		return "base/organ/add";
	}
	
	@RequestMapping("/add")
	public @ResponseBody HttpResult add(HttpServletRequest request,BaseOrgan organ) {
		//校验编号
		if(null != organ.getId() ){
			if(baseOrganService.findCodeIsExit(organ.getId(),null) > 0) {
				HttpResult.build("error", "部门编号已经存在，请核查！");
			}
		}
		//验证重复提交
		if(Token.validToken(request)){
			baseOrganService.addOrgan(organ);
		}
		return HttpResult.success();
	}
	
	@RequestMapping("/modify")
	public @ResponseBody HttpResult modify(HttpServletRequest request,BaseOrgan organ) {
		//校验编号
		if(null != organ.getId() ){
			if(baseOrganService.findCodeIsExit(organ.getId(),organ.getId()) > 0) {
				HttpResult.build("error", "部门编号已经存在，请核查！");
			}
		}
		if(Token.validToken(request)){
			baseOrganService.modifyOrgan(organ);
		}
		return HttpResult.success();
	}
	
	@RequestMapping("/delete/{id}")
	public @ResponseBody HttpResult delete(@PathVariable("id") String id) {
		return HttpResult.build(baseOrganService.delOrganById(id),"");
	}
	
	@RequestMapping("/turnModify/{id}")
	public String turnModify(Model model,@PathVariable("id") String id){
		model.addAttribute("organ",  baseOrganService.findOrganById(id));
		model.addAttribute("porgans", baseOrganService.findPOrgans());
		return "base/organ/modify";
	}
	
	@RequestMapping("/findOrganByPid")
	public @ResponseBody List<BaseOrgan> findOrganByPid(String id){
		if(id != null && "".equals(id)){
			return baseOrganService.findOrgansByPid(id);
		}
		return null;
	}

	@RequestMapping("/findOrganById")
	public @ResponseBody BaseOrgan findOrganById(String id){
		if(id != null && !"".equals(id)){
			return baseOrganService.findOrganById(id);
		}
		return null;
	}
	
}