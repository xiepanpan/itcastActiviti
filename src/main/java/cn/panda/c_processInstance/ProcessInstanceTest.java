package cn.panda.c_processInstance;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * �������̶��� (��zip)
	 */
	@Test
	//keyֵ��ͬ ��Դ����
	public void deployementProcessDefinition_zip() {
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream=new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("���̶���")//��Ӳ������
						.addZipInputStream(zipInputStream)
						.deploy();//��ɲ���
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * ��������ʵ��
	 */
	@Test
	public void startProcessInstance() {
		//ʹ��keyֵ���� Ĭ�ϰ������°汾�����̶�������
		String processDefinitionKey="processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService()//������ִ�е�����ʵ����ִ�ж�����ص�Service
						.startProcessInstanceByKey(processDefinitionKey);//ʹ�����̶����key��������ʵ�� key��Ӧbpmn�ļ���id����ֵ
		System.out.println("����ʵ��id"+processInstance.getId());//����ʵ��id
		System.out.println("���̶���ID"+processInstance.getProcessDefinitionId());//���̶���ID
	}
	
	/**
	 * ��ѯ��ǰ�˵ĸ�������
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee="����";
		List<Task> list = processEngine.getTaskService()//������ִ�е����������ص�Service
						.createTaskQuery()//���������ѯ����
						.taskAssignee(assignee)//ָ����������
						.orderByTaskCreateTime().asc()
						.list();
		if (list!=null&&list.size()>0) {
			for (Task task : list) {
				System.out.println("����ID"+task.getId());
				System.out.println("��������"+task.getName());
				System.out.println("���񴴽�ʱ��"+task.getCreateTime());
				System.out.println("����İ�����"+task.getAssignee());
				System.out.println("����ʵ��id:"+task.getProcessInstanceId());
				System.out.println("ִ�ж���id:"+task.getExecutionId());
				System.out.println("���̶���id:"+task.getProcessDefinitionId());
				
			}
		}
	}
	
	/**
	 * ����ҵ�����
	 */
	@Test
	public void completeMyPersonalTask() {
		String taskId = "1202";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("������� ����ID��"+taskId);
	}
	
	/**
	 * ��ѯ����״̬���ж���������ִ�л��ǽ����� 
	 */
	@Test
	public void isProcessEnd() {
		String processInstanceId="1001";
		//����ִ�е�����ʵ���Ƿ����
		ProcessInstance processInstance = processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstanceId)
						.singleResult();
		if (processInstance==null) {
			System.out.println("�����Ѿ�����");
		}
		else {
			System.out.println("����û�н���");
		}
	}
	
	/**
	 * ��ѯ��ʷ����
	 */
	@Test
	public void findHistoryTask() {
		String taskAssignee="����";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
						.createHistoricTaskInstanceQuery()//������ʷ����ʵ����ѯ
						.taskAssignee(taskAssignee)//ָ����ʷ����İ�����
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricTaskInstance historicTaskInstance : list) {
				System.out.println(historicTaskInstance.getId()+" "+historicTaskInstance.getName());
			}
		}
	}
	
	/**
	 * ��ѯ��ʷ����ʵ��act_hi_procinst
	 */
	@Test
	public void findHistoryProcessInstance(){
		String processInstanceId="1001";
		HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
						.createHistoricProcessInstanceQuery()
						.processInstanceId(processInstanceId)
						.singleResult();
		System.out.println(historicProcessInstance.getId()+historicProcessInstance.getProcessDefinitionId()
		+historicProcessInstance.getStartTime());
	}
	
	/**
	 * ��ѯ��ʷ���̱���act_hi_varinst
	 */
	@Test
	public void findHistoryProcessVariables() {
		String processInstanceId = "1501";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
						.createHistoricVariableInstanceQuery()
						.processInstanceId(processInstanceId )
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricVariableInstance hvi : list) {
				System.out.println(hvi.getId()+" "+hvi.getVariableName()+" "+hvi.getValue());
			}
		}
	}
}
