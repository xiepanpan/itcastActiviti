package cn.panda.i_start;

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

public class StartTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * �������̶��� (��inputStream)
	 */
	@Test
	//keyֵ��ͬ ��Դ����
	public void deployementProcessDefinition_inputStream() {
		//�ӵ�ǰ�ļ��в���
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("start.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("start.png");
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("��ʼ�")//��Ӳ������
						.addInputStream("start.bpmn", inputStreamBpmn)
						.addInputStream("start.png", inputStreamPng)
						.deploy();//��ɲ���
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * ��������ʵ��  �ж������Ƿ���� ��ѯ��ʷ
	 */
	@Test
	public void startProcessInstance() {
		//ʹ��keyֵ���� Ĭ�ϰ������°汾�����̶�������
		String processDefinitionKey="start";
		ProcessInstance processInstance = processEngine.getRuntimeService()//������ִ�е�����ʵ����ִ�ж�����ص�Service
						.startProcessInstanceByKey(processDefinitionKey);//ʹ�����̶����key��������ʵ�� key��Ӧbpmn�ļ���id����ֵ
		//һ������ʵ�� ����ִ�ж���
		System.out.println("����ʵ��id"+processInstance.getId());//����ʵ��id
		System.out.println("���̶���ID"+processInstance.getProcessDefinitionId());//���̶���ID
		
		//�ж������Ƿ���� ��ѯ����ִ�е�ִ�ж����
		ProcessInstance resultpi = processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstance.getId())
						.singleResult();
		if (resultpi==null) {
			//��ѯ��ʷ ��ȡ���������Ϣ
			HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
							.createHistoricProcessInstanceQuery()
							.processInstanceId(processInstance.getId())
							.singleResult();
			System.out.println(historicProcessInstance.getId()+" "+historicProcessInstance.getStartTime());
		}
	}
	
	
}
