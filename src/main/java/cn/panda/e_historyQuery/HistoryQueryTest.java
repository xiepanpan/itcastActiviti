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
	 * ��ѯ��ʷ����ʵ��act_hi_procinst
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
	 * ��ѯ��ʷ�act_hi_actinst
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
	 * ��ѯ��ʷ����
	 */
	@Test
	public void findHistoryTask() {
		String processInstanceId="2201";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
						.createHistoricTaskInstanceQuery()//������ʷ����ʵ����ѯ
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
