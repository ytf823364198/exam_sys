package com.ziyue.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ziyue.service.WjQuestionKpiService;
import com.ziyue.util.*;

import net.sf.json.JSONArray;

import com.ziyue.entity.WjQuestionKpi;
import com.ziyue.entity.BaseUser;
import com.ziyue.entity.MenuTree;
import com.ziyue.entity.QuestionBank;


@Controller
@RequestMapping("/wj_question")
public class WjQuestionKpiController {
	@Autowired
	private WjQuestionKpiService  wjQuestionKpiService;
	
	/**
	 * 转向添加题目表界面
	 */
	@RequestMapping("/turnAdd")
	public String turnAddWjQuestionKpi() {
		return "wj_question/add";
	}
	
	/**
	 * 添加题目表
	 * @param wj_question 题目表对象
	 * @return HttpResult
	 */
	@RequestMapping("/add")
	public @ResponseBody HttpResult addWjQuestionKpi(HttpServletRequest request,QuestionBank questionBank){
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//添加题目表
		wjQuestionKpiService.addWjQuestionKpi(questionBank);
		return HttpResult.success();
	}

	/**
	 * 转向修改题目表界面
	 * @param id 题目表Id
	 */
	@RequestMapping("/turnModify/{id}")
	public String turnModifyWjQuestionKpi(Model model,@PathVariable("id") String id) {
		model.addAttribute("wj_question", wjQuestionKpiService.findWjQuestionKpiById(id));
		return "wj_question/modify";
	}
	
	/**
	 * 修改题目表
	 * @param wj_question 题目表对象
	 * @return HttpResult
	 */
	@RequestMapping("/modify")
	public @ResponseBody HttpResult modifyWjQuestionKpi(HttpServletRequest request,WjQuestionKpi wj_question){
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//修改题目表
		wjQuestionKpiService.modifyWjQuestionKpi(wj_question);
		return HttpResult.success();
	}

	/**
	 * 通过Id删除题目表
	 * @param id 题目表Id
	 * @return HttpResult HttpResult
	 */
	@RequestMapping("/delete/{id}")
	public @ResponseBody HttpResult deleteWjQuestionKpi(@PathVariable("id") String id){
		wjQuestionKpiService.delWjQuestionKpiById(id);
		return HttpResult.success();
	}

	/**
	 * 查询题目表
	 * @param id 题目表对象Id
	 */
	@RequestMapping("/detail/{id}")
	public String detailWjQuestionKpiById(Model model,@PathVariable("id") String id){
		model.addAttribute("wj_question", wjQuestionKpiService.findWjQuestionKpiById(id));
		return "wj_question/detail";
	}


	/**
	 * 分页查询题目表
	 */
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		PageModel pageModel = PageModel.pageModel(request);
		pageModel = wjQuestionKpiService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "wj_question/list";    
	}

	@RequestMapping("/views")
	public String getView(Model model,HttpServletRequest request) {
		PageModel pageModel = PageModel.pageModel(request);
		pageModel = wjQuestionKpiService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "wj_question/list";
	}
	@RequestMapping("/getTreeList")
	@ResponseBody
	public List<MenuTree> getTreeList(Model model,HttpServletRequest request) {
		List<MenuTree> nodeList = new ArrayList<MenuTree>();
		nodeList = wjQuestionKpiService.getTreeList();
		return nodeList;
    }
 
	public static MenuTree recursiveTree(MenuTree parent, List<MenuTree> list) {
	    for (MenuTree menu : list) {
	        if(parent.getId().equals(menu.getPid())) {
		    menu = recursiveTree(menu, list);
		    parent.getChildren().add(menu);
	        }
	    }
	    return parent;
	}
	/*//节点点击事件触发方法
		@RequestMapping("/asyncGetNodes")
		@ResponseBody
		public List<MenuTree> asyncGetNodes(String id, String pid, String name) throws Exception{
			List<MenuTree> nodeList = new ArrayList<MenuTree>();
			
			//根据节点id查询下级菜单
			if(id.equals("10")){
				nodeList.add(new MenuTree("100",id,"单板_00"));
				nodeList.add(new MenuTree("101",id,"单板_01"));
			}
			Thread.sleep(3000);
			return nodeList;
		}*/
}
