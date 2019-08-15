package com.ziyue.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ziyue.entity.BaseAttach;
import com.ziyue.entity.BaseLog;
import com.ziyue.entity.BaseUser;
import com.ziyue.service.ExampleExcelService;
import com.ziyue.util.CollectUtil;
import com.ziyue.util.DateUtil;
import com.ziyue.util.FileLoad;
import com.ziyue.util.HttpResult;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;
import com.ziyue.util.Token;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/example")
public class ExampleController  {
	@Autowired
	private ExampleExcelService exampleExcelService;
	
	/**
	 * 条码	
	 * @return
	 */
	@RequestMapping("/bar")
	public String bar() {
		return "/example/bar/bar";
	}
	
	/**
	 * 二维码
	 * @return
	 */
	@RequestMapping("/qr")
	public String qr() {
		return "/example/bar/qr";
	}
	
	/**
	 * 任务
	 * @return
	 */
	@RequestMapping("/task")
	public String task() {
		return "/example/task/task";
	}
	
	/**
	 * 富文本编辑器
	 * @param model
	 * @param title
	 * @param content
	 * @return
	 */
	@RequestMapping("/editor")
	public String kindeditor(Model model,String title,String content) {
		if(!StringUtil.isEmpty(title)) {
			model.addAttribute("title", title);
		}
		if(!StringUtil.isEmpty(title)) {
			model.addAttribute("content", content);
		}
		return "/example/editor/editor";
	}
	
	/**
	 * 原生表单上传文件
	 * @param file
	 * @return
	 */
	@RequestMapping("/upload")
	public String upload(MultipartFile file) {	
		String path = FileLoad.upLoadTemp(file);
		if(!StringUtil.isEmpty(path)) {
			log.debug("文件上传路径 ---> " + path );
		}
		return "example/upload/upload";
	}
	
	@RequestMapping("/multiple")
	public String uploadmultiple(HttpServletRequest request) {
		List<BaseAttach> attachs = FileLoad.getMultipartFiles(request);
		if(null != attachs && attachs.size() > 0) {
			log.debug("文件上传情况 ---> " + attachs );
		}
		return "example/upload/upload";
	}
	
	
	@RequestMapping("/webuploader")
	public String webload(HttpServletRequest request) {
		List<BaseAttach> attachs = FileLoad.getUploaderFiles(request);
		if(null != attachs && attachs.size() > 0) {
			log.debug("文件上传情况 ---> " + attachs );
		}
		return "example/upload/webuploader";
	}
	
	@RequestMapping("/webuploadergroup")
	public String webloadgroup(HttpServletRequest request) {
		List<BaseAttach> attachs =  FileLoad.getUploaderFiles(request);	
		if(null != attachs && attachs.size() > 0) {
			log.debug("文件上传情况 ---> " + attachs );
		}
		return "example/upload/webuploader_group";
	}

	
	@RequestMapping("/turnBase64str")
	public  String  base64() {
		return "example/upload/base64";
	}
	
	/**
	 * Base64 String上传图片
	 */
	@RequestMapping("/base64str")
	public @ResponseBody HttpResult base64str(Model model,HttpServletRequest request){
		try {
			String size = FileLoad.upLoadBase64Str(request);
			model.addAttribute("size", size);
			log.debug("文件大小 ---> " + size );
			return HttpResult.success(model);
		}catch(Exception e) {
			return HttpResult.error(e.getMessage());
		}
	}
	

	@RequestMapping("/turnBase64FormData")
	public  String  turnBase64FormData() {
		return "example/upload/base64_formdata";
	}
	
	/**
	 * Base64 MultipartFile上传图片
	 */
	@RequestMapping("/base64FormData")
	public @ResponseBody HttpResult  base64FormData(Model model,HttpServletRequest request,@RequestPart("imgdata") MultipartFile imgdata){
		try {
			String size = FileLoad.upLoadBase64Img(request, imgdata);
			model.addAttribute("size", size);
			log.debug("文件大小 ---> " + size );
			return HttpResult.success(model);
		}catch(Exception e) {
			return HttpResult.error(e.getMessage());
		}
	}
	
	@RequestMapping("/turnExcelExp")
	public  String  turnExpExcel(Model model) {
		model.addAttribute("fields", BaseLog.FIELD);
		return "example/excel/exp";
	}
	

	/**
	 * 按照条件导出日志
	 * @param String format 文件后缀
	 */	
	@RequestMapping("/exp")
	public void expLog(HttpServletRequest request,HttpServletResponse response,String format) throws Exception{
		BaseUser user = (BaseUser)request.getSession().getAttribute("loginUser");
		List<String> fields =  CollectUtil.arrayToList(request.getParameterValues("field")); //要导出的字段
		//判断后缀是否有效
		if(null == format || "csv,xlsx".indexOf(format.toLowerCase()) == -1  ){
			format = "xlsx";
		}
		PageModel pagemodel = PageModel.pageModel(request);
		//指定要导出的文件,导出文件放到/attached/temp文件夹下，系统会自动清除
		String expFile = "/attached/temp/" +  DateUtil.getDateLong()+user.getId()+"_log."+format;
		int total = exampleExcelService.expLog(pagemodel,expFile,format,fields);
		
		if(total < 1){
			FileLoad.downFail(response, null,total);
		}else{
			FileLoad.download(expFile,"日志."+format,request,response);	
		}
	}
	
	
	@RequestMapping("/turnExcelImp")
	public  String  turnImpExcel() {
		return "example/excel/imp";
	}

	@RequestMapping("/imp")
	public String impLog(Model model,HttpServletRequest request,MultipartFile file) throws Exception{
		if(Token.validToken(request)){
			String attachUrl = FileLoad.upLoadTemp(file);
			HttpResult result = exampleExcelService.impLogFormExcel(attachUrl);
			model.addAttribute("result",result);
		}
		return "example/excel/imp";
	}
}