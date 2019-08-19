package com.ziyue.service;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import com.ziyue.dao.WjQuestionKpiDao;
import com.ziyue.util.*;
import com.ziyue.entity.MenuTree;
import com.ziyue.entity.QuestionBank;
import com.ziyue.entity.WjQuestionKpi;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 题目表的业务逻辑层操作
 * @author autoCode
 * @date 2019-8-15 16:26:53
 * @version V0.0.1
 */

@Service
public class WjQuestionKpiService {
	@Autowired
	private WjQuestionKpiDao wjQuestionKpiDao;
	
	/**
	 * 添加题目表
	 * @param wj_question 题目表对象
	 
	 * @return id 题目表对象Id
	 */
	public String addWjQuestionKpi(QuestionBank wj_question){
		return wjQuestionKpiDao.addWjQuestionKpi(wj_question);
	}
	
	/**
	 * 删除题目表
	 * @param id 题目表 对象Id
	 * @return String(error:错误;ok:成功)
	 */
	public void delWjQuestionKpiById(String id){
		//删除表单
		wjQuestionKpiDao.delWjQuestionKpiById(id);
	}

	/**
	 * 修改题目表
	 * @param wj_question 题目表对象
	 */
	public void modifyWjQuestionKpi(WjQuestionKpi wj_question){
		wjQuestionKpiDao.modifyWjQuestionKpi(wj_question);
	}

	/**
	 * 通过ID查找题目表对象
	 * @param id 题目表对象Id
	 * @return 题目表对象
	 */
	public QuestionBank findWjQuestionKpiById(String id){
		if(!StringUtil.isEmpty(id)){
			return wjQuestionKpiDao.findWjQuestionKpiById(id);
		}
		return null;
	}
	
	/**
	 * 分页查找题目表对象
	 * @param pageModel  封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		return wjQuestionKpiDao.findBaseSplitPage(pageModel);
	}

	public ArrayList<QuestionBank> getTreeList() {
		// TODO Auto-generated method stub
		return wjQuestionKpiDao.getTreeList();
	}
	public List<QuestionBank> getAllQuestionList() {
		// TODO Auto-generated method stub
		return wjQuestionKpiDao.getAllQuestionList();
	}

	public QuestionBank findQuestionById(String id) {
		// TODO Auto-generated method stub
		return wjQuestionKpiDao.findQuestionById(id);
	}

	public int modify(QuestionBank questionBank) {
		// TODO Auto-generated method stub
		return wjQuestionKpiDao.modify(questionBank);
	}

	public int delete(String id) {
		// TODO Auto-generated method stub
		return wjQuestionKpiDao.delete(id);
	}

	public List<QuestionBank> findChildTypeById(String id) {
		// TODO Auto-generated method stub
		return wjQuestionKpiDao.findChildTypeById(id);
	}
	
	
}
