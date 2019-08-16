package com.ziyue.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ziyue.util.*;
import com.ziyue.entity.QuestionBank;
import com.ziyue.service.QuestionKpiService;
import com.ziyue.entity.BaseUser;


@Controller
@RequestMapping("/question_kpi")
public class QuestionKpiController {
	@Autowired
	private QuestionKpiService  questionKpiService;
	
	/**
	 * 转向添加题库信息界面
	 */
	@RequestMapping("/turnAdd")
	public String turnAddQuestionBank() {
		return "question_kpi/add";
	}
	
	/**
	 * 添加题库信息
	 * @param question_kpi 题库信息对象
	 * @return HttpResult
	 */
	@RequestMapping("/add")
	public @ResponseBody HttpResult addQuestionBank(HttpServletRequest request,QuestionBank question_kpi){
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//添加题库信息
		questionKpiService.addQuestionBank(question_kpi);
		return HttpResult.success();
	}

	/**
	 * 转向修改题库信息界面
	 * @param id 题库信息Id
	 */
	@RequestMapping("/turnModify/{id}")
	public String turnModifyQuestionBank(Model model,@PathVariable("id") String id) {
		model.addAttribute("question_kpi", questionKpiService.findQuestionBankById(id));
		return "question_kpi/modify";
	}
	
	/**
	 * 修改题库信息
	 * @param question_kpi 题库信息对象
	 * @return HttpResult
	 */
	@RequestMapping("/modify")
	public @ResponseBody HttpResult modifyQuestionBank(HttpServletRequest request,QuestionBank question_kpi){
		//验证重复提交的令牌
		if (!Token.validToken(request)) {
			return HttpResult.error("请不要重复操作");
		}
		//修改题库信息
		questionKpiService.modifyQuestionBank(question_kpi);
		return HttpResult.success();
	}

	/**
	 * 通过Id删除题库信息
	 * @param id 题库信息Id
	 * @return HttpResult HttpResult
	 */
	@RequestMapping("/delete/{id}")
	public @ResponseBody HttpResult deleteQuestionBank(@PathVariable("id") String id){
		questionKpiService.delQuestionBankById(id);
		return HttpResult.success();
	}

	/**
	 * 查询题库信息
	 * @param id 题库信息对象Id
	 */
	@RequestMapping("/detail/{id}")
	public String detailQuestionBankById(Model model,@PathVariable("id") String id){
		model.addAttribute("question_kpi", questionKpiService.findQuestionBankById(id));
		return "question_kpi/detail";
	}


	/**
	 * 分页查询题库信息
	 */
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		PageModel pageModel = PageModel.pageModel(request);
		pageModel = questionKpiService.findBaseSplitPage(pageModel);
		model.addAttribute("pageModel", pageModel);
		return "question_kpi/list";
	}
	
	/**
	 * 按照条件导出题库信息
	 */	
	@RequestMapping("/exp")
	public void expQuestionBank(HttpServletRequest request,HttpServletResponse response) throws Exception{
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		PageModel pageModel = PageModel.pageModel(request);
		//指定要导出的文件,导出文件放到/attached/temp文件夹下，系统会自动清除
		String expFile = "/attached/temp/" + QuestionBank.class.getSimpleName()+"_" + DateUtil.getDateLong()+"_"+user.getId()+".xlsx";
		int total = questionKpiService.expQuestionBank(pageModel,expFile);
		if(total < 1){
			FileLoad.downFail(response, null,total);
		}else{
			FileLoad.download(expFile,"题库信息.xlsx",request,response);	
		}
	}
}
