package com.ziyue.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ziyue.service.BaseLogService;
import com.ziyue.util.PageModel;

@Controller
@RequestMapping("/log")
public class BaseLogController  {
	@Autowired
	private BaseLogService baseLogService;

	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		PageModel pageModel = PageModel.pageModel(request);
		model.addAttribute("pageModel", baseLogService.findBaseSplitPage(pageModel));
		return "base/log/list";
	}
	
}