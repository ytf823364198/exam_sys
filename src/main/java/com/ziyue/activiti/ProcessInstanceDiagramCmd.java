package com.ziyue.activiti;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

public class ProcessInstanceDiagramCmd implements Command<InputStream> {
	protected String processInstanceId;
	public ProcessInstanceDiagramCmd(String processInstanceId) {
		super();
		this.processInstanceId = processInstanceId;
	}


	public InputStream execute(CommandContext commandContext) {
		 ExecutionEntityManager executionEntityManager = 
				 commandContext.getExecutionEntityManager();
		 ExecutionEntity executionEntity = 
				 executionEntityManager.findExecutionById(processInstanceId);
		 List<String> activityIds = executionEntity.findActiveActivityIds();
		 String processDefinitionId = executionEntity.getProcessDefinitionId();
		 GetBpmnModelCmd getBpmnModelCmd = new GetBpmnModelCmd(processDefinitionId);
		 BpmnModel bpmnModel = getBpmnModelCmd.execute(commandContext);
		 ProcessDiagramGenerator pdg = new DefaultProcessDiagramGenerator();
	     InputStream  is = pdg.generateDiagram(bpmnModel, "png", activityIds, Collections.<String>emptyList(),"宋体","宋体", null, 1.0);
		 return is;
	}

}
