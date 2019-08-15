package com.ziyue.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ziyue.entity.BaseMenu;
import com.ziyue.entity.BaseRole;
import com.ziyue.service.BaseMenuService;
import com.ziyue.service.BaseRoleService;
import com.ziyue.util.HttpResult;
import com.ziyue.util.StringUtil;
import com.ziyue.util.Token;
@Controller
@RequestMapping("/role")
public class BaseRoleController  {
	@Autowired
	private BaseRoleService baseRoleService;
	@Autowired
	private BaseMenuService baseMenuService;
	
	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("roles", baseRoleService.findRoles());
		return "base/role/list";
	}
	
	@RequestMapping("/turnAdd")
	public String turnAdd(Model model) { 
		model.addAttribute("menus", baseMenuService.findMenus());
		return "base/role/add";
	}
	
	@RequestMapping("/add")
	public @ResponseBody HttpResult add(HttpServletRequest request, BaseRole role,String menuIds) {
		//验证角色编号是否已经存在
		BaseRole obr = baseRoleService.findRoleById(role.getId());
		if(null != obr){
			return HttpResult.error("角色代码已经存在");
		}
		role.setOrgan("n");
		role.setInlay("n");
		role.setSort("99");
		this.setRoleMenu(role,menuIds);
		
		if(Token.validToken(request)){
			baseRoleService.addRole(role);
		}
		return  HttpResult.success();
	}
	
	@RequestMapping("/turnModify/{id}")
	public String turnModify(Model model,@PathVariable("id") String id) {
		List<BaseMenu> menus = baseMenuService.findMenus();
		String menuIds = "";
		List<BaseMenu> checkedMenus =  baseMenuService.findMenuByRoleId(id);
		if(null != checkedMenus && checkedMenus.size() > 0 ) {
			for(BaseMenu checkedMenu : checkedMenus) {
				menuIds = menuIds + checkedMenu.getId() +",";
				for(BaseMenu menu : menus ) {
					if(menu.getId().equals(checkedMenu.getId())) {
						menu.setChecked("y");
					}
				}
			}
		}
		model.addAttribute("menus",menus);
		model.addAttribute("menuIds",menuIds);
		model.addAttribute("role", baseRoleService.findRoleById(id));
		return "base/role/modify";
	}
	
	@RequestMapping("/modify")
	public  @ResponseBody HttpResult modify(HttpServletRequest request,BaseRole role ,String menuIds) {
		if(!Token.validToken(request)){
			return HttpResult.error("请不要重复操作");
		}
		this.setRoleMenu(role,menuIds);
		baseRoleService.modifyRole(role);
		return HttpResult.success();
	}
	
	private void setRoleMenu(BaseRole role ,String menuIds) {
		if(StringUtil.notEmpty(menuIds)){
			String[] menuidArray = menuIds.split(",");
			if(null != menuidArray && menuidArray.length > 0){
				List<BaseMenu> menus = new ArrayList<BaseMenu>();
				for (String menuId : menuidArray) {
					if(StringUtil.notEmpty(menuId)) {
						menus.add(new BaseMenu(menuId));
					}
				}
				role.setMenus(menus);
			}
		}
	}
	
	@GetMapping("/delete/{id}")
	public  @ResponseBody HttpResult delete(@PathVariable("id") String id) {
		//系统内置不能删除
		BaseRole role = baseRoleService.findRoleById(id);
		if("y".equals(role.getInlay()) ){
			return HttpResult.error("系统内置角色不能删除");
		}
		baseRoleService.delRoleById(id);   
		return HttpResult.success();
	}
}