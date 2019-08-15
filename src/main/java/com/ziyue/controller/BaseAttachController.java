package com.ziyue.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.ziyue.entity.BaseAttach;
import com.ziyue.service.BaseAttachService;
import com.ziyue.util.FileLoad;
import com.ziyue.util.FileUtil;
import com.ziyue.util.HttpResult;
import com.ziyue.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/attach")
public class BaseAttachController {
	@Autowired
	private BaseAttachService baseAttachService;

	/**
	 * 文件下载
	 * @param  request
	 * @param  response
	 * @param  id 文件Id
	 */
	@GetMapping("/download/{id}")
	public void  download(HttpServletRequest request, HttpServletResponse response,@PathVariable("id") String id){
		try {
			if(null != id && !"".equals(id.trim()) ){
				BaseAttach attach = baseAttachService.findAttachById(id);
				File file = new File (FileLoad.FILE_ROOT + attach.getPath() );
				if(file.exists()){
					FileLoad.download(attach.getPath() , attach.getName(),request, response);
				}else{
					FileLoad.downFail(response, "对不起，您下载的文件已不存在!",null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 指定模板文件下载
	 * @param request
	 * @param response
	 * @param template 模板标识
	 */
	@GetMapping("/template/{template}")
	public void  template(HttpServletRequest request, HttpServletResponse response,@PathVariable("template") String template){
		if(StringUtil.isEmpty(template)){
			return;
		}
		try {
			switch(template) { 
				case "log":
					FileLoad.download("/attached/template/Log.xlsx", "导入日志模板.xlsx", request,response);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 利用百度提供的webuploader上传文件，支持大文件分块上传
	 * @param request
	 */
	@RequestMapping("/webuploader")
	public @ResponseBody String  webuploader(HttpServletRequest request){
		FileLoad.webUploader(request);
		return "ok";
	}

	/**
	 * 利用百度提供的webuploader上传文件，对已经上传的文件进行合并
	 * @param request
	 * @return HttpResult
	 */
	@RequestMapping("/merge")
	public @ResponseBody HttpResult merge(HttpServletRequest request){
		try{
			String guid = request.getParameter("guid");
			int chunks = Integer.parseInt(request.getParameter("chunks"));
			String ext = request.getParameter("ext").toLowerCase();
			String fileId = request.getParameter("fileId");//文件ID
			String dir = FileLoad.FILE_ROOT + "/attached/temp/"+guid+"/"+fileId;

			File file = new File(dir);
			
			if(file.list().length != chunks ){
				return HttpResult.build("error", "服务器接收到的文件切片数 [ " + file.list().length +" ] , 实际文件切片数 [ "+ chunks +" ] ");
			}
			
			log.debug("chunks--->" + chunks);
			
			File outfile = new File(dir+"/temp."+ext);
			FileOutputStream outputStream = new FileOutputStream(outfile, true);//文件追加写入
			
			byte[] byt = new byte[ 10 * 1024 * 1024 ];
			int len;
			FileInputStream temp = null;//分片文件
			for(int i = 0 ; i < chunks ; i++){
				temp = new FileInputStream(new File(dir + "/"+i +"."+ext));
				while((len = temp.read(byt))!=-1){
					outputStream.write(byt, 0, len);
				}
				temp.close();
			}
			outputStream.close();
			return HttpResult.success();
		}catch(Exception e){
			e.printStackTrace();
			return HttpResult.build("error", e.getMessage());
		}
	}

	/**
	 * mui文件上传
	 */
	@RequestMapping("/muiuploader")
	public void  muiuploader(HttpServletRequest request, HttpServletResponse response){
		FileLoad.muiUploader(request);
	}
	

	@PostMapping(value = "/editor")
	@ResponseBody
	public Map<String,Object> editorFileUpload(HttpServletRequest request) {
		try {
			//定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			extMap.put("flash", "swf,flv");
			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file",  "docx,xlsx,zip,rar,pdf");

			if(!ServletFileUpload.isMultipartContent(request)){
				return getError("请选择文件。");
			}
			String dirName = request.getParameter("dir");
			if (dirName == null) {
				dirName = "image";
			}
			
			if(!extMap.containsKey(dirName)){
				return getError("目录名不正确。");
			}

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            Iterator<String> iter = multiRequest.getFileNames();  
            while(iter.hasNext()){  
	               String key =  iter.next(); 
	               List<MultipartFile> files = multiRequest.getFiles(key);  
	                if(files != null && files.size() > 0 ){   
	                	for(MultipartFile myFile : files){
	                		String format = FileLoad.format(myFile);
	                		//检查扩展名
	                		if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(format)){
	                			return getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
	                		}
	                		
	                		Date date = new Date();
	                	    SimpleDateFormat foryy = new SimpleDateFormat("yyyy");
	                		SimpleDateFormat formm = new SimpleDateFormat("MM");
	                	    //获得要上传的文件路径
	                		String dir = "/attached/editor/"+format+"/"+foryy.format(date)+"/"+formm.format(date)+"/";
	                		String filepath = dir + UUID.randomUUID().toString() + "." + format;
	                		try {
	                			FileUtil.mkdir(FileLoad.FILE_ROOT + dir); 
	                       		File uploadfile = new java.io.File(FileLoad.FILE_ROOT + filepath);
	                       		myFile.transferTo(uploadfile);
	                        } catch (Exception e)  {
	                            e.printStackTrace(); 
	                        } 
	                		
	                		Map<String,Object> obj = new HashMap<String,Object>();
	    					obj.put("error", 0);
	    					obj.put("url", filepath);
	    					return obj;
	                	}
	                }  
	            }  

		}catch(Exception e) {
			e.printStackTrace();
		}
		return getError("上传文件失败");	
	}
	
	private Map<String, Object> getError(String errorMsg) {
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("error", 1);
		errorMap.put("message", errorMsg);
		return errorMap;
	}
	
}
