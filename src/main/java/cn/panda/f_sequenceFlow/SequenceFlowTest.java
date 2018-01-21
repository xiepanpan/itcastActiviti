package cn.panda.f_sequenceFlow;

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

public class SequenceFlowTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义 (从inputStream)
	 */
	@Test
	//key值相同 资源升级
	public void deployementProcessDefinition_inputStream() {
		//从当前文件夹查找
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("sequenceFlow.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("sequenceFlow.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("连线")//添加部署对象
						.addInputStream("sequenceFlow.bpmn", inputStreamBpmn)
						.addInputStream("sequenceFlow.png", inputStreamPng)
						.deploy();//完成部署
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		//使用key值启动 默认按照最新版本的流程定义启动
		String processDefinitionKey="sequenceFlow";
		ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例 key对应bpmn文件的id属性值
		System.out.println("流程实例id"+processInstance.getId());//流程实例id
		System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
	}
	
	/**
	 * 查询当前人的个人任务act_ru_task
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee="赵六";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						.taskAssignee(assignee)//指定个人任务
						.orderByTaskCreateTime().asc()
						.list();
		if (list!=null&&list.size()>0) {
			for (Task task : list) {
				System.out.println("任务ID"+task.getId());
				System.out.println("任务名称"+task.getName());
				System.out.println("任务创建时间"+task.getCreateTime());
				System.out.println("任务的办理人"+task.getAssignee());
				System.out.println("流程实例id:"+task.getProcessInstanceId());
				System.out.println("执行对象id:"+task.getExecutionId());
				System.out.println("流程定义id:"+task.getProcessDefinitionId());
				
			}
		}
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyPersonalTask() {
		String taskId = "3303";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("message", "重要");
		//完成任务同时 设置流程变量${message=='不重要'}
		processEngine.getTaskService()
						.complete(taskId, variables);
		System.out.println("完成任务 任务ID："+taskId);
	}
	
	
}
