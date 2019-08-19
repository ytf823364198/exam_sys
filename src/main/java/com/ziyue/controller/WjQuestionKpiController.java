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
		/*PageModel pageModel = PageModel.pageModel(request);
		pageModel = wjQuestionKpiService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);*/
		return "wj_question/list";
	}
	@RequestMapping("/getTreeList")
	@ResponseBody
	public ArrayList<QuestionBank> getTreeList(Model model,HttpServletRequest request) {
		ArrayList<QuestionBank> lists = new ArrayList<>();
		ArrayList<QuestionBank> treeList = wjQuestionKpiService.getTreeList();
		ArrayList<QuestionBank> trees = (ArrayList<QuestionBank>) treeList.clone();
		ArrayList<QuestionBank> roots= new ArrayList<>();
        for(QuestionBank questionBank :treeList){
            if(questionBank.getPid().equals("0")){
                roots.add(questionBank);
                trees.remove(questionBank);
            }
        }
        treeList = convertTree(roots,trees);
        lists.addAll(treeList);
        return treeList;

    }
 
	public static QuestionBank recursiveTree(QuestionBank parent, List<QuestionBank> list) {
	    for (QuestionBank menu : list) {
	        if(parent.getId().equals(menu.getPid())) {
		    menu = recursiveTree(menu, list);
		    parent.getNodes().add(menu);
	        }
	    }
	    return parent;
	}
	 public ArrayList<QuestionBank> convertTree(ArrayList<QuestionBank> roots,ArrayList<QuestionBank> trees) {
	        if(trees.size()==0)return roots;
	        ArrayList<QuestionBank> leafs = new ArrayList<>();
	        ArrayList<QuestionBank> remains = new ArrayList<>();
	        remains = (ArrayList<QuestionBank>) trees.clone();
	        for (QuestionBank root : roots) {
	            for (QuestionBank leaf : trees) {
	                if (root.getId().equals(leaf.getPid())) {
	                    root.getNodes().add(leaf);
	                    leafs.add(leaf);
	                    remains.remove(leaf);
	                }
	            }
	        }
	        convertTree(leafs,remains);
	        return roots;
	    }
	//节点点击事件触发方法
		@RequestMapping("/asyncGetNodes")
		@ResponseBody
		public ArrayList<QuestionBank> asyncGetNodes(String id, String pid, String name) throws Exception{
			ArrayList<QuestionBank> nodeList = new ArrayList<QuestionBank>();
			
			//根据节点id查询下级菜单
			if(id.equals("10")){
				nodeList.add(new QuestionBank("100",id,"单板_00"));
				nodeList.add(new QuestionBank("101",id,"单板_01"));
			}
			Thread.sleep(3000);
			return nodeList;
		}
	@RequestMapping("/getAllQuestionList")
	public @ResponseBody List<QuestionBank> getAllGoodsList(HttpServletRequest request){
		List<QuestionBank> lists = wjQuestionKpiService.getAllQuestionList();
		return lists;
	}
	@RequestMapping(value="/findQuestionById")
	public @ResponseBody QuestionBank findQuestionById(String id,HttpServletRequest request) {
		QuestionBank questionBank = wjQuestionKpiService.findQuestionById(id);
		return questionBank;
	}
	@RequestMapping(value="/modify")
	public @ResponseBody String modifyQuestion(QuestionBank questionBank) {
		int count = wjQuestionKpiService.modify(questionBank);
		if(count>0) {
			return "success";
		}else {
			return "error";
		}
	}
	@RequestMapping(value="/delete")
	public @ResponseBody String deleteQuestionById(HttpServletRequest request) {
		String id = request.getParameter("id");
		System.out.println(id);
		int count = wjQuestionKpiService.delete(id);
		if(count>0) {
			return "success";
		}else {
			return "error";
		}
	}
	@RequestMapping("/findQuestTypeById")
	public @ResponseBody List<QuestionBank> findQuestTypeById(String id,HttpServletRequest request){
		System.out.println(id);
		String ids = request.getParameter("id");
		System.out.println(ids);
		List<QuestionBank> lists = wjQuestionKpiService.findChildTypeById(ids);
		return lists;
	}
}
