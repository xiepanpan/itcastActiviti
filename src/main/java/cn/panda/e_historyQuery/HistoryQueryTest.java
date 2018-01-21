package cn.panda.e_historyQuery;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.Test;

public class HistoryQueryTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 查询历史流程实例act_hi_procinst
	 */
	@Test
	public void findHistoryProcessInstance(){
		String processInstanceId="2201";
		HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
						.createHistoricProcessInstanceQuery()
						.processInstanceId(processInstanceId)
						.singleResult();
		System.out.println(historicProcessInstance.getId()+historicProcessInstance.getProcessDefinitionId()
		+historicProcessInstance.getStartTime());
	}
	
	/**
	 * 查询历史活动act_hi_actinst
	 */
	@Test
	public void findHistoryActiviti(){
		String processInstanceId = "2201";
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
						.createHistoricActivityInstanceQuery()
						.processInstanceId(processInstanceId)
						.orderByHistoricActivityInstanceStartTime().asc()
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricActivityInstance hai : list) {
				System.out.println(hai.getId()+" "+hai.getProcessInstanceId()+" "+hai.getActivityName());
			}
		}
	}
	
	/**
	 * 查询历史任务
	 */
	@Test
	public void findHistoryTask() {
		String processInstanceId="2201";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
						.createHistoricTaskInstanceQuery()//创建历史任务实例查询
						.processInstanceId(processInstanceId)
						.orderByProcessInstanceId().asc()
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricTaskInstance historicTaskInstance : list) {
				System.out.println(historicTaskInstance.getId()+" "+historicTaskInstance.getName());
			}
		}
	}
	
	
	
}
