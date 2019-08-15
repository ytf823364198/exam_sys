package com.ziyue.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ziyue.dao.BaseLogDao;
import com.ziyue.entity.BaseLog;
import com.ziyue.util.FileLoad;
import com.ziyue.util.HttpResult;
import com.ziyue.util.POIUtil;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExampleExcelService {
	@Autowired
	private BaseLogDao baseLogDao;
	
	public int expLog(PageModel pageModel, String expFile, String format,List<String> fields) {
		int total =  baseLogDao.countLogByPageModel(pageModel);
		if(total > 0) {
			expFile = FileLoad.FILE_ROOT + expFile;
			pageModel.setPagecount(10000);
			pageModel.setPagesizeByCount(total);
			if("xlsx".equals(format)){
				expLogFieldXlsx( pageModel,expFile,fields);
			}else{
				expLogFieldCsv( pageModel,expFile,fields);
			}
		}
		return total;
	}
	
	
	public void expLogFieldXlsx(PageModel pageModel,String expFile,List<String> fields) {
		
		if(null == fields || fields.size() == 0) {
			expLogXlsx( pageModel,expFile);
			return ;
		}
		
		String[] title = new String[fields.size()];
		for(int i = 0; i< fields.size(); i++ ){
			title[i] =  BaseLog.FIELD.get(fields.get(i));
		}
		
		XSSFWorkbook workBook = new XSSFWorkbook();
		Map<String,CellStyle> styles = POIUtil.getStyles(workBook);
		FileOutputStream outputStream = null;
		int sheetQuerySize = 1000000 / pageModel.getPagecount() ;//表格一个Sheet最多可放100万条记录
		try {
			outputStream = new FileOutputStream(new File(expFile));
			int xlsSheet = 0;XSSFSheet sheet = null;XSSFRow row = null;
			for(int curpage = 1 ; curpage <= pageModel.getPagesize() ; curpage++){
				pageModel.setCurpage(curpage);

				if(curpage % sheetQuerySize == 1 ){
					sheet = workBook.createSheet("日志" + ( ++ xlsSheet ) );
					POIUtil.creatHead(workBook, row, sheet, title,10);
				}
				
				int num = curpage % sheetQuerySize ;
				if(num == 0 ){num = sheetQuerySize;}	
				List<BaseLog> blogs =  baseLogDao.findLogByPageModel(pageModel);
				
				if(null != blogs && blogs.size() > 0){
					
					for ( int i = 0; i< blogs.size(); i++) {//每次写一行记录
						BaseLog blog = blogs.get(i);
						row = sheet.createRow(i+1 + (num - 1) * pageModel.getPagecount());
						row.setHeight((short) (400));
						int rowNo = 0;

						for(String field : fields){
							if(StringUtil.isEmpty(field)){
								continue;
							}
							switch(field) {
								case "id" : POIUtil.setCell(row ,rowNo++, blog.getId(),styles.get("base"));break;
								case "userid" : POIUtil.setCell(row ,rowNo++,blog.getUserid(),styles.get("base"));break;
								case "realname" : POIUtil.setCell(row ,rowNo++, blog.getRealname(),styles.get("base"));break;
								case "action" : POIUtil.setCell(row ,rowNo++,blog.getAction(),styles.get("base"));break;
								case "ip" : POIUtil.setCell(row ,rowNo++,blog.getIp(),styles.get("base"));break;
								case "browser" : POIUtil.setCell(row ,rowNo++, blog.getBrowser(),styles.get("base"));break;
								case "opttime" : POIUtil.setCell(row ,rowNo++, blog.getOpttime(),styles.get("base"));break;
								case "result" : POIUtil.setCell(row ,rowNo++, blog.getResult(),styles.get("base"));break;
							}
						}	
						
					}
				}	
				
				blogs.clear();
			}
			workBook.write(outputStream);
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
			POIUtil.closeStream(workBook, outputStream);
        }
	}
	
	/**
	 * 
	 * @param pageModel  分页封装类
	 * @param expFile  导出文件路径
	 * @param fields  导出勾选的字段
	 */
	public void expLogFieldCsv(PageModel pageModel,String expFile,List<String> fields){
		
		if(null == fields || fields.size() == 0) {
			expLogCsv( pageModel,expFile);
			return ;
		}
		//遍历表头
		String title =  "";
		for(String field : fields){
			title = title + BaseLog.FIELD.get(field)+",";
		}
		title = title + "\r\n";
		
		FileWriter fw = null;
		try {
	    	fw = new FileWriter(expFile);
	    	fw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
	    	fw.write(title);
	    	String content = null;
			for(int curpage = 1 ; curpage <= pageModel.getPagesize() ; curpage++){
				pageModel.setCurpage(curpage);
				List<BaseLog> blogs =  baseLogDao.findLogByPageModel(pageModel);
				if(null != blogs && blogs.size() > 0 ){
					for(BaseLog blog : blogs){
						StringBuffer sb = new StringBuffer("");
						
						for(String field : fields){
							if(null == field || "".equals(field)){
								continue;
							}
							//遍历要导出的字段
							switch(field) { 
								case "id": sb.append(blog.getId()).append(","); break;
								case "userid": sb.append(blog.getUserid()).append(","); break;
								case "realname": sb.append(blog.getRealname()).append(","); break;
								case "action": sb.append(blog.getAction()).append(","); break;
								case "ip": sb.append(blog.getIp()).append(","); break;
								case "browser": sb.append("\t"+StringUtil.csvHandlerStr(blog.getBrowser())).append(","); break;
								case "opttime": sb.append(blog.getOpttime()).append(","); break;
								case "result": sb.append(blog.getResult()).append(","); break;
							}	
						
						
						}	
						content = sb.toString().replaceAll("null", "")+"\r\n";
						fw.write(content);
					}
					blogs.clear();	
				}		
			}
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
	        try {
	            if(null != fw) {
	                fw.close();
	            }
	        } catch (Exception e) {
	        	
	        }
	    }
	}
	
	
	public void expLogXlsx(PageModel pageModel,String expFile) {
		Long s = new Date().getTime();
		String[] titles = {"ID:10","userid:6","realname:8","action:16","ip:10","browser:25","opttime:16","result:10"};
		XSSFWorkbook workBook = new XSSFWorkbook();
		Map<String,CellStyle> styles = POIUtil.getStyles(workBook);
		FileOutputStream outputStream = null;
		int sheetQuerySize = 1000000 / pageModel.getPagecount() ;//表格一个Sheet最多可放100万条记录
		try {
			outputStream = new FileOutputStream(new File(expFile));
			int xlsSheet = 0;XSSFSheet sheet = null;XSSFRow row = null;
			for(int curpage = 1 ; curpage <= pageModel.getPagesize() ; curpage++){
				pageModel.setCurpage(curpage);

				if(curpage % sheetQuerySize == 1 ){
					xlsSheet = xlsSheet + 1;
					sheet = workBook.createSheet("日志"+xlsSheet);
					POIUtil.creatHead(workBook, row, sheet, titles);
				}
				
				int num = curpage % sheetQuerySize ;
				if(num == 0 ){num = sheetQuerySize;}	
				List<BaseLog> blogs =  baseLogDao.findLogByPageModel(pageModel);
				
				if(null != blogs && blogs.size() > 0){
					
					for ( int i = 0; i< blogs.size(); i++) {//每次写一行记录
						BaseLog blog = blogs.get(i);
						row = sheet.createRow(i+1 + (num - 1) * pageModel.getPagecount());
						row.setHeight((short) (400));
						int rowNo = 0;
						POIUtil.setCell(row , rowNo++ , blog.getId() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getUserid() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getRealname() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getAction() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getIp() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getBrowser() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getOpttime() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , blog.getResult() ,styles.get("base"));
					}
				}	
				
				blogs.clear();
			}
			workBook.write(outputStream);
			System.out.println( s - ( new Date().getTime() ));
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
			POIUtil.closeStream(workBook, outputStream);
			
        }
	}
	
	public void expLogCsv(PageModel pageModel,String expFile){
		String title = "ID,userid,realname,action,ip,browser,opttime,result"+"\r\n";		
		FileWriter fw = null;
		try {
	    	fw = new FileWriter(expFile);
	    	fw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
	    	fw.write(title);
	    	String content = null;
			for(int curpage = 1 ; curpage <= pageModel.getPagesize() ; curpage++){
				pageModel.setCurpage(curpage);
				List<BaseLog> blogs =  baseLogDao.findLogByPageModel(pageModel);
				if(null != blogs && blogs.size() > 0 ){
					for(BaseLog blog : blogs){
						StringBuffer sb = new StringBuffer("");
						sb.append(blog.getId()).append(",");
						sb.append(blog.getUserid()).append(",");
						sb.append(blog.getRealname()).append(",");
						sb.append(blog.getAction()).append(",");
						sb.append(blog.getIp()).append(",");
						sb.append("\t"+StringUtil.csvHandlerStr(blog.getBrowser())).append(",");
						sb.append(blog.getOpttime()).append(",");
						sb.append(blog.getResult()).append(",");
						content = sb.toString().replaceAll("null", "")+"\r\n";
						fw.write(content);
					}
					blogs.clear();	
				}		
			}
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
            try {
                if(null != fw) {
                    fw.close();
                }
            } catch (Exception e) {
            	
            }
        }
	}

	
	@Transactional
	public HttpResult impLogFormExcel(String attachUrl) {
		List<ArrayList<String>> dataList = new POIUtil().read(attachUrl);
		HttpResult result = this.checkExcelData(dataList);
		if("error".equals(result.getStatus() )) {
			return result;
		}
		
		for(ArrayList<String> cols : dataList){
			BaseLog blog = new BaseLog();
			int j = 0;
				for(String col : cols){
					switch(j) { 
							case 0:blog.setId(col);break; 
							case 1:blog.setUserid(col);break; 
							case 2:blog.setRealname(col);break; 
							case 3:blog.setAction(col);break; 
							case 4:blog.setIp(col);break; 
							case 5:blog.setBrowser(col);break; 
							case 6:blog.setOpttime(col);break; 
							case 7:blog.setResult(col);break; 
					}
				j++;
			}
			//保存到数据库
			//baseLogDao.addLog(log); 
			log.debug( log.toString() );
		}
		result.setMsg("数据导入成功,共导入 "+dataList.size()+" 条记录");
		return result;
	}
	
	
	//验证导入数据
	private HttpResult checkExcelData(List<ArrayList<String>> dataList ){
		if(null != dataList && dataList.size() >0){
			int i = 1;
			for(ArrayList<String> cols : dataList){
				int j = 0;
				for(String col : cols){
					switch(j) { 
						case 0:
							if(StringUtil.isEmpty(col)){
								return HttpResult.error("第"+i+"行,ID未填写");
							}
						break; 
						case 1:
							if(StringUtil.isEmpty(col)){
								return HttpResult.error("第"+i+"行,USERID未填写");
							}
						break;
					}
					j++;						
				}
				i++;
			}
		}else{
			return HttpResult.error("没有填写数据");
		}
		return  HttpResult.success();
	}
	
}
