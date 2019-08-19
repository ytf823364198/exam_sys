package com.ziyue.service;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import com.ziyue.dao.QuestionKpiDao;
import com.ziyue.util.*;
import com.ziyue.entity.QuestionBank;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 题库信息的业务逻辑层操作
 * @author autoCode
 * @date 2019-8-15 13:56:02
 * @version V0.0.1
 */

@Service
public class QuestionKpiService {
	@Autowired
	private QuestionKpiDao questionKpiDao;
	
	/**
	 * 添加题库信息
	 * @param question_kpi 题库信息对象
	 
	 * @return id 题库信息对象Id
	 */
	public String addQuestionBank(QuestionBank question_kpi){
		return questionKpiDao.addQuestionBank(question_kpi);
	}
	
	/**
	 * 删除题库信息
	 * @param id 题库信息 对象Id
	 * @return String(error:错误;ok:成功)
	 */
	public void delQuestionBankById(String id){
		//删除表单
		questionKpiDao.delQuestionBankById(id);
	}

	/**
	 * 修改题库信息
	 * @param question_kpi 题库信息对象
	 */
	public void modifyQuestionBank(QuestionBank question_kpi){
		questionKpiDao.modifyQuestionBank(question_kpi);
	}

	/**
	 * 通过ID查找题库信息对象
	 * @param id 题库信息对象Id
	 * @return 题库信息对象
	 */
	public QuestionBank findQuestionBankById(String id){
		if(!StringUtil.isEmpty(id)){
			return questionKpiDao.findQuestionBankById(id);
		}
		return null;
	}
	
	/**
	 * 分页查找题库信息对象
	 * @param pageModel  封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		return questionKpiDao.findBaseSplitPage(pageModel);
	}
	
	/**
	 * 导出题库信息，默认格式为xlsx
	 * @param pageModel 分页封装类
	 * @param expFile 导出文件的路径
	 * @return int导出的记录数，记录数为0，返回让Controller层处理
	 */
	public int expQuestionBank(PageModel pageModel, String expFile) {
		int total =  questionKpiDao.countQuestionBankByPageModel(pageModel);
		if(total > 0) {
			expFile = FileLoad.FILE_ROOT + expFile;
			pageModel.setPagecount(10000);
			pageModel.setPagesizeByCount(total);
			this.expQuestionBankXlsx( pageModel,expFile);
		}
		return total;
	}
	
	/**
	 * 导出题库信息，默认格式为xlsx的实现
	 * @param pageModel 分页封装类
	 * @param expFile 导出文件的路径
	 */	
	public void expQuestionBankXlsx(PageModel pageModel,String expFile) {
		//TODO 表头
		String[] titles = {"题库名称:16","创建时间:12","试卷备注:16","试卷类型:12"};
		XSSFWorkbook workBook = new XSSFWorkbook();
		Map<String,CellStyle> styles = POIUtil.getStyles(workBook);
		FileOutputStream outputStream = null;
		//表格一个Sheet最多可放100万条记录,超过100万条分Sheet
		int sheetQuerySize = 1000000 / pageModel.getPagecount() ;
		try {
			outputStream = new FileOutputStream(new File(expFile));
			int xlsSheet = 0; XSSFSheet sheet = null; XSSFRow row = null;
			for(int curpage = 1 ; curpage <= pageModel.getPagesize() ; curpage++){
				pageModel.setCurpage(curpage);

				if(curpage % sheetQuerySize == 1 ){
					sheet = workBook.createSheet("题库信息" + ( ++ xlsSheet ) );
					POIUtil.creatHead(workBook, row, sheet, titles);
				}
				
				int num = curpage % sheetQuerySize ;
				if(num == 0 ){num = sheetQuerySize;}
				List<QuestionBank> question_kpis =  questionKpiDao.findQuestionBankByPageModel(pageModel);
				if(null != question_kpis && question_kpis.size() > 0){
					for ( int i = 0; i< question_kpis.size(); i++) {//每次写一行记录
						QuestionBank question_kpi = question_kpis.get(i);
						row = sheet.createRow(i+1 + (num - 1) * pageModel.getPagecount());
						int rowNo = 0;
						//TODO 导出的字段
						POIUtil.setCell(row , rowNo++ , question_kpi.getText() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , question_kpi.getCreattime() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , question_kpi.getRemark() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , question_kpi.getType() ,styles.get("base"));
					}
				}	
				
				question_kpis.clear();
			}
			workBook.write(outputStream);
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
           POIUtil.closeStream(workBook, outputStream);
        }
	}
	
	
	
}
