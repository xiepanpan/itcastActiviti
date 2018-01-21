package cn.panda.j_receiveTask;

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
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ReceiveTaskTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * �������̶��� (��inputStream)
	 */
	@Test
	//keyֵ��ͬ ��Դ����
	public void deployementProcessDefinition_inputStream() {
		//�ӵ�ǰ�ļ��в���
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("receiveTask.png");
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("���ջ����")//��Ӳ������
						.addInputStream("receiveTask.bpmn", inputStreamBpmn)
						.addInputStream("receiveTask.png", inputStreamPng)
						.deploy();//��ɲ���
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * ��������ʵ��  �������̱��� ��ȡ���̱��� ���ִ��һ��
	 */
	@Test
	public void startProcessInstance() {
		//ʹ��keyֵ���� Ĭ�ϰ������°汾�����̶�������
		String processDefinitionKey="receiveTask";
		ProcessInstance processInstance = processEngine.getRuntimeService()//������ִ�е�����ʵ����ִ�ж�����ص�Service
						.startProcessInstanceByKey(processDefinitionKey);//ʹ�����̶����key��������ʵ�� key��Ӧbpmn�ļ���id����ֵ
		//һ������ʵ�� ����ִ�ж���
		System.out.println("����ʵ��id"+processInstance.getId());//����ʵ��id
		System.out.println("���̶���ID"+processInstance.getProcessDefinitionId());//���̶���ID
		
		//��ѯִ�ж���ID
		Execution execution = processEngine.getRuntimeService()
						.createExecutionQuery()
						.processInstanceId(processInstance.getId())
						.activityId("receivetask1")//��ǰ�id ��ӦreceiveTask.bpmn �id
						.singleResult();
		
		//ʹ�����̱������õ������۶� ��������ҵ�����
		processEngine.getRuntimeService()
						.setVariable(execution.getId(), "���ܵ���������", 21000);
		
		//���ִ��һ��
		processEngine.getRuntimeService()
						.signal(execution.getId());
		
		//��ѯִ�ж���ID
				Execution execution2 = processEngine.getRuntimeService()
								.createExecutionQuery()
								.processInstanceId(processInstance.getId())
								.activityId("receivetask2")//��ǰ�id ��ӦreceiveTask.bpmn �id
								.singleResult();
		
		//�ӱ����л�ȡ���ܵ������۶��ֵ
		Integer num = (Integer)processEngine.getRuntimeService()
						.getVariable(execution2.getId(), "���ܵ���������");
		System.out.println("���ϰ巢���� ��"+num);
	}
	
	
}
