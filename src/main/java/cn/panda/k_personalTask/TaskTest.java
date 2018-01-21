package cn.panda.k_personalTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class TaskTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * �������̶��� (��inputStream)
	 */
	@Test
	//keyֵ��ͬ ��Դ����
	public void deployementProcessDefinition_inputStream() {
		//�ӵ�ǰ�ļ��в���
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("task.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("task.png");
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("����")//��Ӳ������
						.addInputStream("task.bpmn", inputStreamBpmn)
						.addInputStream("task.png", inputStreamPng)
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
		String processDefinitionKey="task";
		//��������ʵ��ͬʱ �������̱��� ʹ�����̱���ָ�����������#{userID} 
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userID", "�ܽ���");
		ProcessInstance processInstance = processEngine.getRuntimeService()//������ִ�е�����ʵ����ִ�ж�����ص�Service
						.startProcessInstanceByKey(processDefinitionKey,variables);//ʹ�����̶����key��������ʵ�� key��Ӧbpmn�ļ���id����ֵ
		System.out.println("����ʵ��id"+processInstance.getId());//����ʵ��id
		System.out.println("���̶���ID"+processInstance.getProcessDefinitionId());//���̶���ID
	}
	
	/**
	 * ��ѯ��ǰ�˵ĸ�������act_ru_task
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee="�ܽ���";
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
		String taskId = "7405";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("������� ����ID��"+taskId);
	}
	
	
}
