package cn.panda.d_processVariables;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.sun.accessibility.internal.resources.accessibility;

public class ProcessVariablesTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * �������̶��� (��InputStream)
	 */
	@Test
	//keyֵ��ͬ ��Դ����
	public void deployementProcessDefinition_inputStream() {
		InputStream inputStreambpmn= this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");//��claspath����
		InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("���̶���")//��Ӳ������
						.addInputStream("processVariables.bpmn", inputStreambpmn)//ʹ����Դ�ļ������ƣ�����Դ����һ�£� ��������
						.addInputStream("processVariables.png", inputStreampng )
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
	 * �������̱���
	 */
	@Test
	public void setVariables() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "1504";
		
//		taskService.setVariable(taskId, "�������", 3);
//		taskService.setVariable(taskId, "�������", new Date());
//		taskService.setVariable(taskId, "���ԭ��", "�ؼ����� �Է�");
		
		//ʹ��javabean 
		Person person = new Person();
		person.setId(20);
		person.setName("�仨");
		taskService.setVariable(taskId, "��Ա��Ϣ(��ӹ̶��汾)", person);
		
		System.out.println("�������̱����ɹ�");
	}
	
	/**
	 * ��ȡ���̱���
	 */
	@Test
	public void getVariables() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "1504";
//		Integer days = (Integer)taskService.getVariable(taskId, "�������");
//		Date date = (Date)taskService.getVariable(taskId, "�������");
//		String reason = (String)taskService.getVariable(taskId, "���ԭ��");
//		System.out.println("���������"+days);
//		System.out.println("������ڣ�"+date);
//		System.out.println("���ԭ��"+reason);
		
		// ��ȡjavabean
		Person person = (Person)taskService.getVariable(taskId, "��Ա��Ϣ(��ӹ̶��汾)");
		System.out.println(person.getId()+" "+person.getName());
	}
	
	/**
	 * ģ�����úͻ�ȡ���̱����ĳ���
	 */
	public void setAndGetVariables() {
		//������ʵ�� ִ�ж�������ִ�У�
		RuntimeService runtimeService = processEngine.getRuntimeService();
		//��������أ�����ִ�У�
		TaskService taskService = processEngine.getTaskService();
		//�������̱���
//		runtimeService.setVariable(executionId, variableName, value);
//		runtimeService.setVariables(executionId, variables);//map����
		
//		runtimeService.startProcessInstanceById(processDefinitionId, variables);//��������ʵ��ͬʱ �������̱��� map����
//		taskService.complete(taskId, variables);//�������ͬʱ �������̱���
		
		//��ȡ���̱���
//		runtimeService.getVariable(executionId, variableName);
//		runtimeService.getVariables(executionId);
	}
	
	/**
	 * ����ҵ�����
	 */
	@Test
	public void completeMyPersonalTask() {
		String taskId = "2702";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("������� ����ID��"+taskId);
	}
	
	/**
	 * ��ѯ���̱�������ʷ��
	 */
	@Test
	public void findHistoryProcessVariables() {
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
						.createHistoricVariableInstanceQuery()
						.variableName("��Ա��Ϣ")
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricVariableInstance hvi : list) {
				System.out.println(hvi.getId()+" "+hvi.getVariableName()+" "+hvi.getValue());
			}
		}
	}
}
