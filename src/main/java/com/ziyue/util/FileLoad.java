package com.ziyue.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ziyue.entity.BaseAttach;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class FileLoad {
	//微软的浏览器
	private static String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};
	//禁止上传的文件格式
	private final static String  forbidExt = "jsp,exe,html,js,bat,jar,";
	
	public static String FILE_ROOT;
	
	@Value("${spring.http.multipart.location}")
    public void setFileRoot(String root) {
		FILE_ROOT = root;
    }
	 
	/**
	 * 需要使用Trident和Edge关键字来判断是否是微软的浏览器（微软抛弃了IE，开始使用Edge了）
	 * @param HttpServletRequest request
	 * @return
	 */
    public static boolean isMSBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal))
                return true;
        }
        return false;
    }
    
    /**
     * 文件名编码转换
     * @param request
     * @param viewFileName 下载后显示的文件名
     * @return
     */
    public static String encodeFileName (HttpServletRequest request,String viewFileName) {
    	String agent = (String)request.getHeader("USER-AGENT");  
        try {
        	if(agent != null && agent.toLowerCase().indexOf("firefox") > 0){
            	viewFileName = new String(viewFileName.getBytes("UTF-8"), "ISO-8859-1");
            }else{
            	viewFileName =  java.net.URLEncoder.encode(viewFileName, "UTF-8");
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return viewFileName;
    }
    
    /**
     * 获取文件的小写格式，并判读文件是否有风险
     * @param MultipartFile myFile
     * @return format 文件格式（小写）
     */
    public static String format(MultipartFile  myFile) {
    	if(null == myFile || "".equals(myFile.getOriginalFilename())){
 	    	return null;
 	    }	
 	    String formats[]=(myFile.getOriginalFilename()).split("\\.");
 	    if(null == formats || formats.length < 1) {
 	    	return null;
 	    }

  		String format = formats[formats.length-1].toLowerCase();
  		//防止jsp，js，exe 植入
  		if(forbidExt.indexOf(format) > -1 ){
  			myFile = null;
  			return null;
  		}
  		return format;
    }
    
	/**
	 * 传统文件上传，单一文件，
	 * @param MultipartFile myFile
	 * @return BaseAttach 
	 */
	public static BaseAttach upLoad(MultipartFile  myFile){
		String format = format(myFile);
		if(null == format || "".equals(format)) {
			return null;
		}
 		Date date = new Date();
	    SimpleDateFormat foryy = new SimpleDateFormat("yyyy");
		SimpleDateFormat formm = new SimpleDateFormat("MM");
	    //获得要上传的文件路径
		String dir = "/attached/"+format+"/"+foryy.format(date)+"/"+formm.format(date);
		String filepath = dir + "/"+(UUID.randomUUID().toString())+"."+format;
		try {
			FileUtil.mkdir(FILE_ROOT + dir); 
       		File uploadfile = new java.io.File(FILE_ROOT + filepath);
       		myFile.transferTo(uploadfile);
        } catch (Exception e)  {
            e.printStackTrace(); 
        } 
		 return new BaseAttach(filepath, myFile.getOriginalFilename(), format, myFile.getSize());
	}
	
	/**
	 * 传统文件上传，单一文件，并指定上传路径
	 * @param MultipartFile myFile
	 * @param String fixedPath 指定上传的文件路径
	 * @param String filename  指定上传的文件名
	 * @return BaseAttach 
	 */
	public static BaseAttach upLoad(MultipartFile  myFile,String fixedPath,String filename){
		String format = format(myFile);
		if(null == format || "".equals(format)) {
			return null;
		}
	    //获得要上传的文件路径
		String dir = "/attached/"+fixedPath;
		String filepath = dir + "/"+filename+"."+format;
		try {
			FileUtil.mkdir(FILE_ROOT + dir); 
       		File uploadfile = new java.io.File(FILE_ROOT + filepath);
       		myFile.transferTo(uploadfile);
        } catch (Exception e)  {
            e.printStackTrace(); 
        } 
		return new BaseAttach(filepath, myFile.getOriginalFilename(), format, myFile.getSize());
	}
	
	/**
	 * 上传文件到临时文件夹
	 * @param MultipartFile myFile
	 * @return String filepath 文件存放绝对路径
	 */
	public static String upLoadTemp(MultipartFile  myFile){
		String format = format(myFile);
		if(null == format || "".equals(format)) {
			return null;
		}
	    //获得要上传的文件路径
		String dir= FILE_ROOT + "/attached/temp";
		String filepath = dir + "/"+(UUID.randomUUID().toString())+"."+format;
		try {
			FileUtil.mkdir( dir); 
       		File uploadfile = new java.io.File(filepath);
       		log.debug("uploadfile ---> " +uploadfile.getAbsolutePath() );
       		myFile.transferTo(uploadfile);
        } catch (Exception e)  {
            e.printStackTrace(); 
        } 
		return filepath;
	}
	
	
	/**
	 * 下载文件
	 * @param filePath 文件路径
	 * @param viewFileName 下载后的文件名 如"测试.docx"
	 * @param String sutux文件后缀
	 */
	public static void download(String filePath, String viewFileName,HttpServletRequest request,HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream fileInput = null;
		try {
			response.setCharacterEncoding("UTF-8");
			outStream = response.getOutputStream();
			fileInput = new FileInputStream(FILE_ROOT + filePath);
			
			String formats[] = viewFileName.split("\\.");
	 		String format = formats[formats.length-1].toLowerCase();
	 		
	 		switch(format) { 
	 			case	"pdf":	response.setContentType("application/pdf");	break; 
	 			case	"bmp":	response.setContentType("application/x-bmp");	break; 
	 			case	"jpeg":	response.setContentType("image/jpeg");			break; 
	 			case	"jpg":	response.setContentType("image/jpeg");			break; 
	 			case	"png":	response.setContentType("application/x-png");	break; 
		 		case	"ppt":	response.setContentType("application/x-ppt");	break; 
		 		case 	"pptx":	response.setContentType("application/x-ppt");	break; 
		 		case 	"xls":	response.setContentType("application/x-xls");	break; 
		 		case	"xlsx":	response.setContentType("application/x-xls");	break; 
		 		case	"doc":	response.setContentType("application/msword");	break; 
		 		case 	"docx":	response.setContentType("application/msword");	break; 
		 		case 	"txt":	response.setContentType("text/plain");			break; 
		 		case	"rtf":	response.setContentType("application/x-rtf");	break; 
		 		case	"mp3":	response.setContentType("audio/mp3");			break; 
		 		case	"movie":response.setContentType("video/x-sgi-movie");	break; 
		 		case	"avi":	response.setContentType("video/avi");			break; 
		 		default	:		response.setContentType("application/x-msdownload");	break; 
	 		}

            response.setHeader("Content-disposition", "attachment; filename="+ encodeFileName(request,viewFileName) );
			int numOfBytes = fileInput.available();
			byte byteArray[] = new byte[numOfBytes];
			fileInput.read(byteArray);
			response.setHeader("Content-Length", "" + numOfBytes);
			outStream.write(byteArray);
			
		} catch (Exception ex) {
			
		}finally {
			try {
				fileInput.close();
				outStream.flush();
				outStream.close();
			} catch (IOException e) {
				
			}
		}
	}
	
	/**
	 * 多文件上传
	 * @param HttpServletRequest request
	 * @return List<BaseAttach>
	 */
	public static List<BaseAttach> getMultipartFiles(HttpServletRequest request){
		List<BaseAttach> attachs = new ArrayList<BaseAttach>();
		try{
			//创建一个通用的多部分解析器 
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
			 //判断 request 是否有文件上传,即多部分请求 
			if(multipartResolver.isMultipart(request)){ 
				//转换成多部分request    
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
	            //取得request中的所有文件名  
	            Iterator<String> iter = multiRequest.getFileNames();  
	            while(iter.hasNext()){  
	                //取得上传文件  
	               String key =  iter.next(); 
	               List<MultipartFile> files = multiRequest.getFiles(key);  
	                if(files != null && files.size() > 0 ){   
	                	for(MultipartFile file : files){
	                		BaseAttach attach = FileLoad.upLoad(file);
	        				if(null != attach){
	        					attach.setFlag(key);
	        					attachs.add(attach);
	        				}
	                	}
	                }  
	            }  
	           return attachs;
			}
			return attachs;
		}catch(Exception e){
			e.printStackTrace();
			return attachs;
		}
	}

	/**
	 * 百度webUploader多文件上传
	 * @param HttpServletRequest request
	 */
	public static void webUploader(HttpServletRequest request){
		String guid = request.getParameter("guid");//文件组分配的Id 
		String fileId = request.getParameter("id");//文件ID
		String chunk = request.getParameter("chunk");//当前切片数
		String chunks = request.getParameter("chunks");//总切片数
		if(null == chunk || "".equals(chunk) || "1".equals(chunks)){
			chunk = "temp";
		}
		try{
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
			if(multipartResolver.isMultipart(request)){ 
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
	            Iterator<String> iter = multiRequest.getFileNames();  
	            while(iter.hasNext()){  
	               String key =  iter.next(); 
	               List<MultipartFile> files = multiRequest.getFiles(key);  
	                if(files != null && files.size() > 0 ){   
	                	for(MultipartFile myFile : files){
	                		String format = format(myFile);
	                		if(null == format || "".equals(format)) {
	                			continue;
	                		}
	                		
	                		String dir= "/attached/temp/"+guid+"/"+fileId;
	                		String filepath = dir + "/" + chunk + "." + format;
	                		try {
	                			FileUtil.mkdir(FILE_ROOT+dir); 
	                       		File uploadfile = new java.io.File(  filepath);
	                       		myFile.transferTo(uploadfile);
	                        } catch (Exception e)  {
	                            e.printStackTrace(); 
	                        } 
	                	}
	                }  
	            }  
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * MUI Uploader多文件上传
	 * @param request
	 */
	public static void muiUploader(HttpServletRequest request){
		String guid = request.getParameter("guid");//文件组分配的Id 
		String filename	= "temp";
		try{
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
			if(multipartResolver.isMultipart(request)){ 
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
	            Iterator<String> iter = multiRequest.getFileNames();  
	            while(iter.hasNext()){  
	               String key =  iter.next(); 
	               List<MultipartFile> files = multiRequest.getFiles(key);  
	                if(files != null && files.size() > 0 ){   
	                	for(MultipartFile myFile : files){
	                		
	                		String format = format(myFile);
	                		if(null == format || "".equals(format)) {
	                			continue;
	                		}
	                		
	                		String dir= "/attached/temp/"+guid+"/"+ myFile.getName();
	                		String filepath = dir + "/" + filename + "." + format;
	                		try {
	                			FileUtil.mkdir(FILE_ROOT+dir); 
	                       		File uploadfile = new java.io.File(  filepath);
	                       		myFile.transferTo(uploadfile);
	                        } catch (Exception e)  {
	                            e.printStackTrace(); 
	                        } 
	          
	                	}
	                }  
	            }  
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 获取上传成功的文件，并将上传到临时文件夹的文件移动到正式文件夹
	 * @param HttpServletRequest request
	 * @return List<BaseAttach>
	 */
	public static List<BaseAttach> getUploaderFiles(HttpServletRequest request){
		
		String [] fileIds = request.getParameterValues("wuFileId");//文件ID
		if(null == fileIds || fileIds.length < 1 ){
			return null;
		}
		String [] guIds = request.getParameterValues("wuGuId");//文件组id
		String [] fileNames = request.getParameterValues("wuFileName");//文件名
		String [] fileExts = request.getParameterValues("wuFileExt");//文件后缀
		String [] fileStatuses = request.getParameterValues("wuFileStatus");//上传状态
		String [] fileVals = request.getParameterValues("fileVal");//表单名字
		
		List<BaseAttach> attachs = null ;
		
		Date date = new Date();
	    SimpleDateFormat foryy = new SimpleDateFormat("yyyy");
		SimpleDateFormat formm = new SimpleDateFormat("MM");
		String oldFilePath = "/attached/temp";
		
		if(null != fileIds && fileIds.length > 0 ){
			for(int i = 0;i < fileIds.length ; i++ ){
				if(!"complete".equals(fileStatuses[i]) ){//非正确上传
					continue;
				}	
					
				String fileExt = fileExts[i].toLowerCase();
				String fileId = fileIds[i];
				String fileName = fileNames[i];
				String fileVal = fileVals[i];
				String guId = guIds[i];
				String uuid = UUID.randomUUID().toString().replace("-", "");
				
				//获得要上传的文件路径
				String newFileName="/attached/"+fileExt+"/"+foryy.format(date)+"/"+formm.format(date);
				//不存在则创建一个文件夹或者目录
				FileUtil.mkdir(FILE_ROOT+newFileName);	
	            String srcFilePath = oldFilePath+"/"+guId+"/"+fileId+"/temp."+fileExt;
	            File oldFile = new File(FILE_ROOT+srcFilePath);
	            //判断文件是否存在
	            if (!oldFile.exists()) {
	            	continue;
	    		}

	            Long filesize = oldFile.length();

	     		File newFile = new File(FILE_ROOT+ newFileName +"/"+ uuid +"." + fileExt);
	     		//移动文件
	     		oldFile.renameTo(newFile);

	     		if(null == attachs ){
	     			attachs = new ArrayList<BaseAttach>();
	     		}
	     		BaseAttach attach = new BaseAttach(newFileName +"/"+ uuid +"." + fileExt, fileName, fileExt, filesize);
	     		attach.setFlag(fileVal);
	     		attachs.add(attach);
	     		
			}
		}
		return attachs;
	}
	
	
	
	
   /**
    * Base64上传图片,到临时目录,返回原图的大小
    * @param HttpServletRequest request
    * @param MultipartFile myFile
    * @return String fileSize
    */
	public static String upLoadBase64Img(HttpServletRequest request,MultipartFile  myFile){
		String guid = request.getParameter("guid");//文件组分配的Id 
		String fileName = request.getParameter("fileName");
		String fileId = request.getParameter("fileId");
		Long fileSize = myFile.getSize();
		
	    if(null == myFile || "".equals(myFile.getOriginalFilename())){
	    	return null;
	    }	
	    String formats[] = fileName.split("\\.");
 		String format = formats[formats.length-1].toLowerCase();
 		//防止jsp，js，exe 植入ddd
 		if(forbidExt.indexOf(format.toLowerCase()) > -1 ){
 			myFile = null;
 			return null;
 		}
	    //获得要上传的文件路径
		String newFileName= FILE_ROOT + "/attached/temp/"+guid+"/"+fileId;
		log.debug("newFileName-->" + newFileName);
		try  {
             FileUtil.mkdir(newFileName);
             newFileName = newFileName + "/temp."+format;
             //上传图片
       		 File uploadfile = new java.io.File( newFileName);
       		 myFile.transferTo(uploadfile);
        } catch (Exception e)  {
            e.printStackTrace(); 
        } 

		if(fileSize/ 1024/1024 < 0.05 ){
			return	(fileSize / 1024) +"KB";
		}else{
			return (Double.valueOf(fileSize *100  / 1024 /1024) /100  ) +"MB";
		}
		
	}

	
	/**
	 * Base64上传图片(String),到临时目录,返回原图的大小
	 * @param HttpServletRequest request
	 * @return String fileSize
	 */
	public static String upLoadBase64Str(HttpServletRequest request){
		String imgdata = request.getParameter("imgdata");
		String guid = request.getParameter("guid");//文件组分配的Id 
		String fileName = request.getParameter("filename");
		String fileId = request.getParameter("fileId");

	    if(null == imgdata || "".equals(imgdata)){
	    	return null;
	    }
	    if(imgdata.indexOf(",") >0 ) {
	    	imgdata = imgdata.split(",")[1];
	    }
	    String formats[] = fileName.split("\\.");
 		String format = formats[formats.length-1];
 		
	    //获得要上传的文件路径
		String newFileName = FILE_ROOT + "/attached/temp/"+guid+"/"+fileId;
		 try  {
			FileUtil.mkdir( newFileName);
            newFileName = newFileName + "/temp."+format;
             //上传图片
       		FileUtil.createBase64Image(imgdata, newFileName);
       		
       		File uploadfile = new java.io.File( newFileName);
       		Long fileSize =  uploadfile.length();
       		if(uploadfile.length() / 1024/1024 < 0.05 ){
				return	(fileSize / 1024) +"KB";
			}else{
				return ((Double.valueOf(fileSize *100  / 1024 /1024) /100  ) ) +"MB";
			}
       		
         } catch (Exception e)  {
            e.printStackTrace(); 
            return null;
        } 
		
	}
	
	/**
	 * 文件下载失败的提示信息
	 * @param HttpServletResponse response
	 * @param String msg 提示信息
	 * @param Integer flag 提示标识（0:无导出的数据;）
	 */
	public static void downFail(HttpServletResponse response,String msg,Integer flag){
		PrintWriter pw = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			pw = response.getWriter();
			if(null == msg || "".equals(msg) && null != flag ){
				msg =( flag == 0 ? "对不起,没有要导出的数据!" : "对不起,导出数据出错,请联系管理员!" );
			}
			if(null == msg || "".equals(msg)){
				msg = "对不起，导出失败!";
			}
		    pw.write("<script  type=\"text/javascript\" >alert('"+msg+"');history.go(-1);</script>");
		}catch (IOException e) {
		}finally{
			if(null!=pw){
			  pw.close();
			}
		}
	}

	
}
