package com.ziyue.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ziyue.dao.BaseInspectDao;
import com.ziyue.dao.ExampleLeaveDao;
import com.ziyue.entity.BaseInspect;
import com.ziyue.entity.BaseUser;
import com.ziyue.entity.ExampleLeave;
import com.ziyue.util.DateUtil;
import com.ziyue.util.FileLoad;
import com.ziyue.util.POIUtil;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 对ExampleLeaveService 请假申请的业务逻辑层操作
 * @author autoCode
 * @date 2018-4-16 15:09:03
 * @version V0.0.1
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
@Service
@Slf4j
public class ExampleLeaveService {
	@Autowired
	private ExampleLeaveDao exampleLeaveDao;
	@Autowired
	private BaseInspectDao baseInspectDao;  
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	@Transactional
	public String addExampleLeave(ExampleLeave lea ,BaseUser user){
		lea.setApptime(DateUtil.fullTime());
		lea.setAppuserid(user.getId());
		lea.setStatus("0");
		String id = exampleLeaveDao.addExampleLeave(lea);
		//填写历史流程记录
		BaseInspect ins = new BaseInspect(id,user,"填写","填写了请假申请",ExampleLeave.class.getSimpleName(),"请假申请",null);
		baseInspectDao.addInspect(ins);
		return id;
	}
	
	@Transactional
	public void delExampleLeaveById(String id){
		//删除历史流程记录
		baseInspectDao.delInspectByObjectId(id);
		//清空工作流
		ExampleLeave lea = exampleLeaveDao.findExampleLeaveById(id);
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
		exampleLeaveDao.delExampleLeaveById(id);
	}

	
	public void modifyExampleLeave(ExampleLeave lea){
		lea.setStatus("0");
		exampleLeaveDao.modifyExampleLeave(lea);
	}

	
	public ExampleLeave findExampleLeaveById(String id){
		if(null != id && !"".equals(id)){
			return exampleLeaveDao.findExampleLeaveById(id);
		}
		return null;
	}
	
	
	public ExampleLeave findExampleLeaveDetailById(String id){
		ExampleLeave lea = this.findExampleLeaveById(id);
		if(null != lea){
			//读取操作的历史流程
			lea.setInspects(baseInspectDao.findInspectByObjectId(id));
		}
		return lea;
	}
	
	
	public void commitExampleLeave(String id,BaseUser user){
		ExampleLeave lea = exampleLeaveDao.findExampleLeaveById(id);
		//开启工作流
		startProcessExampleLeave(lea,user);
	}
	
	//启动流程
	@Transactional
	private void startProcessExampleLeave(ExampleLeave lea, BaseUser user){
		log.debug( ExampleLeave.class.getSimpleName() );
		Map<String, Object> variables = new HashMap<String,Object>();
		//注意：这里需要自己编写，请在variables中，设置工作流需要的流程变量		
		variables.put("id", lea.getId());
		variables.put("appuserid", user.getId());
		variables.put("day", lea.getDay());
		variables.put("organHeadId", "lind");
		//使用流程定义的key，启动流程实例，同时设置流程变量
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ExampleLeave.class.getSimpleName(),lea.getId(),variables);
		//流程实例ID
		final String procinstid = processInstance.getId();
		//修改业务数据[请假申请]表单
		exampleLeaveDao.modifyExampleLeaveByCols(new HashMap(){
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
		BaseInspect ins = new BaseInspect(lea.getId(),user,"提交","提交请假申请",ExampleLeave.class.getSimpleName(),task.getName(),task.getTaskDefinitionKey());
		baseInspectDao.addInspect(ins);
		
		this.endProcessExampleLeave(procinstid,lea);
	}
	
	@Transactional
	public void doTask(BaseUser user, ExampleLeave lea){
		Task task = taskService.createTaskQuery()//
				.taskId(lea.getTaskid())//使用任务ID查询
				.singleResult();

		String procinstid = task.getProcessInstanceId();

		Map<String, Object> variables = new HashMap<String,Object>();
		if(StringUtil.isEmpty(lea.getOutcome()) ){
			lea.setOutcome("确定");
		}
		log.debug(lea.getOutcome());
		variables.put("outcome", lea.getOutcome());
		//在这里判断,每个节点,此任务节点需要其他处理可在此编码
		if("TaskCommit".equals(task.getTaskDefinitionKey()) ){
			if("提交".equals(lea.getOutcome())) {
				exampleLeaveDao.modifyExampleLeave(lea);
			}
		}

		taskService.complete(task.getId(), variables);
		BaseInspect ins = new BaseInspect(lea.getId(),user,lea.getOutcome(),lea.getInfo(),ExampleLeave.class.getSimpleName(), task.getName(),task.getTaskDefinitionKey());
		baseInspectDao.addInspect(ins);

		this.endProcessExampleLeave(procinstid,lea);
	}
	
	
	private void endProcessExampleLeave(String procinstid,ExampleLeave lea) {
		//使用流程实例ID查询
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId(procinstid)
				.singleResult();
		final String status = "作废".equals(lea.getOutcome() ) ? "9" : "2" ;
		//流程结束了
		if(pi == null){
			//修改表单，这里需要自己编写，(修改单据状态为“已审批”或“已作废”)
			exampleLeaveDao.modifyExampleLeaveByCols(new HashMap(){
				{put("status",status);}// 注意:这里需要自己编写
				}, lea.getId());
		}
	}
	
	public PageModel findRunTaskSplitPage(PageModel pageModel){
		return exampleLeaveDao.findRunTaskSplitPage(pageModel);
	}
	
	public PageModel findHisTaskSplitPage(PageModel pageModel){
		return exampleLeaveDao.findHisTaskSplitPage(pageModel);
	}
	
	public PageModel findBaseSplitPage(PageModel pageModel){
		return exampleLeaveDao.findBaseSplitPage(pageModel);
	}

	/**
	 * 导出请假申请，默认格式为xlsx
	 * @param PageModel pageModel 分页封装类
	 * @param String expFile 导出文件的路径
	 * @return int 导出的记录数，记录数为0，返回让Controller层处理
	 */
	public int expExampleLeave(PageModel pageModel, String expFile) {
		int total =  exampleLeaveDao.countExampleLeaveByPageModel(pageModel);
		if(total > 0) {
			expFile = FileLoad.FILE_ROOT + expFile;
			pageModel.setPagecount(10000);
			pageModel.setPagesizeByCount(total);
			this.expExampleLeaveXlsx( pageModel,expFile);
		}
		return total;
	}
	
	public void expExampleLeaveXlsx(PageModel pageModel,String expFile) {
		String[] titles = {"ID:12","年龄:6","邮件:12","请假日期:8","天数:6","性别:6","状态:0","扣除薪水:8"};
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
				List<ExampleLeave> leaves =  exampleLeaveDao.findExampleLeaveByPageModel(pageModel);
				if(null != leaves && leaves.size() > 0){
					for ( int i = 0; i< leaves.size(); i++) {//每次写一行记录
						ExampleLeave leave = leaves.get(i);
						row = sheet.createRow(i+1 + (num - 1) * pageModel.getPagecount());
						row.setHeight((short) (400));
						int rowNo = 0;
						POIUtil.setCell(row , rowNo++ , leave.getId() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , leave.getAge() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , leave.getEmail() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , leave.getLeavedate() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , leave.getDay() ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , ExampleLeave.EX_LEAVE_SEX.get(leave.getSex()) ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , ExampleLeave.EX_LEAVE_STATUS.get(leave.getStatus()) ,styles.get("base"));
						POIUtil.setCell(row , rowNo++ , leave.getMoney() ,styles.get("right"));
					}
				}	
				leaves.clear();
			}
			workBook.write(outputStream);
		}catch (Exception e) {
	    	e.printStackTrace();
		}finally {
			POIUtil.closeStream(workBook, outputStream);
        }
	}
	

}
