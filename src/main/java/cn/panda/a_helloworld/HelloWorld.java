package cn.panda.a_helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class HelloWorld {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**
	 * 部署流程定义
	 */
	@Test
	public void deployementProcessDefinition() {
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("helloworld入门程序")//添加部署对象
						.addClasspathResource("diagrams/helloworld.bpmn")//从classpath资源中加载 一次加载一个文件
						.addClasspathResource("diagrams/helloworld.png")//从classpath资源中加载 一次加载一个文件
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
		String processDefinitionKey="helloworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例 key对应bpmn文件的id属性值
		System.out.println("流程实例id"+processInstance.getId());//流程实例id
		System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
	}
	
	/**
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee="王五";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						.taskAssignee(assignee)//指定个人任务
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
		String taskId = "302";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("完成任务 任务ID："+taskId);
	}
}
