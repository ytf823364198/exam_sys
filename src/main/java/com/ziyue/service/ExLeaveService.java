package com.ziyue.service;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import com.ziyue.dao.BaseInspectDao;
import com.ziyue.dao.ExLeaveDao;
import com.ziyue.util.*;
import com.ziyue.entity.ExLeave;
import com.ziyue.entity.BaseUser;
import com.ziyue.entity.BaseInspect;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请假申请的业务逻辑层操作
 * @author autoCode
 * @date 2018-9-8 21:44:27
 * @version V0.0.1
 */

@Service
public class ExLeaveService {
	@Autowired
	private ExLeaveDao exLeaveDao;
	@Autowired
	private BaseInspectDao baseInspectDao;  
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	/**
	 * 添加请假申请
	 * @param lea 请假申请对象
	 * @param user 添加操作的用户
	 * @return id 请假申请对象Id
	 */
	@Transactional
	public String addExLeave(ExLeave lea ,BaseUser user){
		String id = exLeaveDao.addExLeave(lea);
		//填写历史流程记录
		BaseInspect ins = new BaseInspect(id,user,"填写","填写请假申请",ExLeave.class.getSimpleName(),"请假申请",null);
		baseInspectDao.addInspect(ins);
		return id;
	}
	
	/**
	 * 删除请假申请
	 * @param id 请假申请 对象Id
	 * @return String(error:错误;ok:成功)
	 */
	@Transactional
	public void delExLeaveById(String id){
		//删除历史流程记录
		baseInspectDao.delInspectByObjectId(id);
		//清空工作流
		ExLeave lea = exLeaveDao.findExLeaveById(id);
		if(!StringUtil.isEmpty(lea.getProcinstid())){
			ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(lea.getProcinstid())//使用流程实例ID查询
					.singleResult();
			if(null != pi){//流程没有结束,结束了删除会报异常
				runtimeService.deleteProcessInstance(lea.getProcinstid(), "");
			}
			historyService.deleteHistoricProcessInstance(lea.getProcinstid());
		}
		//删除表单
		exLeaveDao.delExLeaveById(id);
	}

	/**
	 * 修改请假申请
	 * @param lea 请假申请对象
	 */
	public void modifyExLeave(ExLeave lea){
		exLeaveDao.modifyExLeave(lea);
	}

	/**
	 * 通过ID查找请假申请对象
	 * @param id 请假申请对象Id
	 * @return 请假申请对象
	 */
	public ExLeave findExLeaveById(String id){
		if(!StringUtil.isEmpty(id)){
			return exLeaveDao.findExLeaveById(id);
		}
		return null;
	}
	
	/**
	 * 通过ID查找请假申请对象所有信息
	 * @param id 请假申请对象Id
	 * @return 请假申请对象
	 */
	public ExLeave findExLeaveDetailById(String id){
		ExLeave lea = this.findExLeaveById(id);
		if(null != lea){
			//读取操作的历史流程
			lea.setInspects(baseInspectDao.findInspectByObjectId(id));
		}
		return lea;
	}
	
	/**
	 * 通过ID提交请假申请对象
	 * @param id 请假申请对象Id
	 * @param user 执行提交操作的用户
	 */
	public void commitExLeave(String id,BaseUser user){
		ExLeave lea = exLeaveDao.findExLeaveById(id);
		//开启工作流
		startProcessExLeave(lea,user);
	}
	
	/**
	 * 启动工作流
	 * @param lea 请假申请对象
	 * @param user 执行提交操作的用户
	 */
	@Transactional
	private void startProcessExLeave(ExLeave lea, BaseUser user){
		Map<String, Object> variables = new HashMap<String,Object>();
		//TODO 注意：这里需要自己编写，请在variables中，设置工作流需要的流程变量		
		variables.put("id", lea.getId());
		//使用流程定义的key，启动流程实例，同时设置流程变量
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ExLeave.class.getSimpleName(), lea.getId(),variables);
		//流程实例ID
		final String procinstid = processInstance.getId();
		//修改业务数据[请假申请]表单
		exLeaveDao.modifyExLeaveByCols(new HashMap(){
			{put("status","1");}// 注意:这里需要自己编写
			{put("procinstid",procinstid);}
		}, lea.getId());
		
		//指定个人任务查询
		Task task = taskService.createTaskQuery()
							   .taskAssignee(user.getId())
							   .processInstanceBusinessKey(lea.getId()).singleResult();
		//办理提交任务
		variables.clear();
		variables.put("outcome", "提交");
		taskService.complete(task.getId(),variables);
		//将办理记录设置到我们自己的流程表中,注意:请不要写入Activiti的ACT_HI_COMMENT表
		BaseInspect ins = new BaseInspect(lea.getId(),user,"提交","提交请假申请",ExLeave.class.getSimpleName(),task.getName(),task.getTaskDefinitionKey());
		baseInspectDao.addInspect(ins);
		//TODO 判断流程是否结束，结束后处理业务逻辑
		//this.endProcessExLeave(procinstid,lea);
	}
	
	/**
	 * 办理任务
	 * @param lea 请假申请对象
	 * @param user 执行提交操作的用户
	 */
	@Transactional
	public void doTask(BaseUser user, ExLeave lea){
		Task task = taskService.createTaskQuery()//
				.taskId(lea.getTaskid())//使用任务ID查询
				.singleResult();

		String procinstid = task.getProcessInstanceId();

		Map<String, Object> variables = new HashMap<String,Object>();
		if(StringUtil.isEmpty(lea.getOutcome()) ){
			lea.setOutcome("确定");
		}
		variables.put("outcome", lea.getOutcome());
		//TODO 在这里判断,每个节点,此任务节点需要其他处理可在此编码
		if("TaskCommit".equals(task.getTaskDefinitionKey()) ){
			if("提交".equals(lea.getOutcome())) {
				exLeaveDao.modifyExLeave(lea);
			}
		}

		taskService.complete(task.getId(), variables);
		BaseInspect ins = new BaseInspect(lea.getId(),user,lea.getOutcome(),lea.getInfo(),ExLeave.class.getSimpleName(),task.getName(),task.getTaskDefinitionKey());
		baseInspectDao.addInspect(ins);
		//判断流程是否结束，结束后处理业务逻辑
		this.endProcessExLeave(procinstid,lea);
	}
	
	/**
	 * 判断流程是否结束，结束后处理业务逻辑
	 * @param lea 请假申请对象
	 * @param procinstid 流程实例
	 */	
	private void endProcessExLeave(String procinstid,ExLeave lea) {
		//使用流程实例ID查询
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId(procinstid)
				.singleResult();
		final String status = "作废".equals(lea.getOutcome() ) ? "9" : "2" ;
		//流程结束了
		if(null == pi ){
			//TODO 修改表单，这里需要自己编写，(修改单据状态为“已审批”或“已作废”)
			exLeaveDao.modifyExLeaveByCols(new HashMap(){
				{put("status",status);}// 注意:这里需要自己编写
				}, lea.getId());
		}
	}
	
	/**
	 * 分页查找请假申请的待办事务
	 * @param pageModel  封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findRunTaskSplitPage(PageModel pageModel){
		return exLeaveDao.findRunTaskSplitPage(pageModel);
	}
	
	/**
	 * 分页查找请假申请的已办理任务
	 * @param pageModel  封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findHisTaskSplitPage(PageModel pageModel){
		return exLeaveDao.findHisTaskSplitPage(pageModel);
	}
	
	/**
	 * 分页查找请假申请对象
	 * @param pageModel  封装的分页对象
	 * @return PageModel封装的分页对象
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		return exLeaveDao.findBaseSplitPage(pageModel);
	}
	
	/**
	 * 导出请假申请，默认格式为xlsx
	 * @param pageModel 分页封装类
	 * @param expFile 导出文件的路径
	 * @return int导出的记录数，记录数为0，返回让Controller层处理
	 */
	public int expExLeave(PageModel pageModel, String expFile) {
		int total =  exLeaveDao.countExLeaveByPageModel(pageModel);
		if(total > 0) {
			expFile = FileLoad.FILE_ROOT + expFile;
			pageModel.setPagecount(10000);
			pageModel.setPagesizeByCount(total);
			this.expExLeaveXlsx( pageModel,expFile);
		}
		return total;
	}
	
	/**
	 * 导出请假申请，默认格式为xlsx的实现
	 * @param pageModel 分页封装类
	 * @param expFile 导出文件的路径
	 */	
	public void expExLeaveXlsx(PageModel pageModel,String expFile) {
		//TODO 表头
		String[] titles = {"请假天数:12","电话:12","邮件:16","年龄:12","扣除薪水:12","请假日期:12","备注说明:64","状态:12","性别:12","申请人:12","申请时间:20","流程实例ID:12"};
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
					sheet = workBook.createSheet("请假申请" + ( ++ xlsSheet ) );
					POIUtil.creatHead(workBook, row, sheet, titles);
				}
				
				int num = curpage % sheetQuerySize ;
				if(num == 0 ){num = sheetQuerySize;}
				List<ExLeave> leas =  exLeaveDao.findExLeaveByPageModel(pageModel);
				if(null != leas && leas.size() > 0){
					for ( int i = 0; i< leas.size(); i++) {//每次写一行记录
						ExLeave lea = leas.get(i);
						row = sheet.createRow(i+1 + (num - 1) * pageModel.getPagecount());
						int rowNo = 0;
						//TODO 导出的字段
						POIUtil.setCell(row , rowNo++ , lea.getDay() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getTelphone() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getEmail() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getAge() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getMoney() ,styles.get("right"));
						POIUtil.setCell(row , rowNo++ , lea.getLeavedate() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getRemark() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , ExLeave.EX_LEAVE_STATUS.get(lea.getStatus()) ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , ExLeave.EX_LEAVE_SEX.get(lea.getSex()) ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getAppuserid() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getApptime() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , lea.getProcinstid() ,styles.get("base"));
					}
				}	
				
				leas.clear();
			}
			workBook.write(outputStream);
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
           POIUtil.closeStream(workBook, outputStream);
        }
	}
	
	
	
}
