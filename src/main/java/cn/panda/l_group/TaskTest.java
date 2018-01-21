package cn.panda.l_group;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
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
		variables.put("userIDs", "�ܽ���,���,����,СС");
		ProcessInstance processInstance = processEngine.getRuntimeService()//������ִ�е�����ʵ����ִ�ж�����ص�Service
						.startProcessInstanceByKey(processDefinitionKey,variables);//ʹ�����̶����key��������ʵ�� key��Ӧbpmn�ļ���id����ֵ
		System.out.println("����ʵ��id"+processInstance.getId());//����ʵ��id
		System.out.println("���̶���ID"+processInstance.getProcessDefinitionId());//���̶���ID
	}
	
	/**
	 * ��ѯ��ǰ�˵�������act_ru_task
	 */
	@Test
	public void findMyPersonalTask() {
		String candidateUser="���";
		List<Task> list = processEngine.getTaskService()//������ִ�е����������ص�Service
						.createTaskQuery()//���������ѯ����
						.taskCandidateUser(candidateUser)
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
		String taskId = "9905";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("������� ����ID��"+taskId);
	}
	
	/**
	 * ��ѯ����ִ�е���������˱�
	 */
	@Test
	public void findRunPersonTask() {
		String taskId = "8701";
		List<IdentityLink> list = processEngine.getTaskService()
						.getIdentityLinksForTask(taskId);
		if (list!=null&&list.size()>0) {
			for (IdentityLink identityLink : list) {
				System.out.println(identityLink.getUserId()+" "
							+identityLink.getProcessInstanceId()+" "
							+identityLink.getType());
			}
		}
	}
	
	/**
	 * ��ѯ��ʷ����İ�����
	 */
	@Test
	public void findHistoryPersonTask() {
		String processInstanceId = "8901";
		List<HistoricIdentityLink> list = processEngine.getHistoryService()
						.getHistoricIdentityLinksForProcessInstance(processInstanceId);
		if (list!=null&&list.size()>0) {
			for (HistoricIdentityLink historicIdentityLink : list) {
				System.out.println(historicIdentityLink.getTaskId()+" "
				+historicIdentityLink.getType()+" "
				+historicIdentityLink.getProcessInstanceId());
			}
		}
	}
	
	/**
	 * ʰȡ���� ������������������ ָ������İ������ֶ�
	 */
	@Test
	public void claim() {
		//����Id
		String userId = "���";
		String taskId = "9905";
		//������������ĳ�Ա Ҳ������������
		processEngine.getTaskService()
		.claim(taskId, userId);
	}
	
	/**
	 * ������������˵������� ǰ����֮ǰһ����������
	 */
	@Test
	public void setAssignee() {
		String taskId = "8905";
		processEngine.getTaskService()
						.setAssignee(taskId, null);
	}
	
	/**
	 * ������������ӳ�Ա
	 */
	@Test
	public void addGroupUser() {
		String taskId = "8905";
		String userId = "��S";
		processEngine.getTaskService()
						.addCandidateUser(taskId, userId);
	}
	
	/**
	 * ����������ɾ����Ա
	 */
	@Test
	public void deleteGroupUser() {
		String taskId = "8905";
		String userId = "��S";
		processEngine.getTaskService()
						.deleteCandidateUser(taskId, userId);
	}
}

