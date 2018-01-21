package cn.panda.h_parallelGateWay;

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

public class ParallelGateWayTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义 (从inputStream)
	 */
	@Test
	//key值相同 资源升级
	public void deployementProcessDefinition_inputStream() {
		//从当前文件夹查找
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("parallelGateWay.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("parallelGateWay.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("并行网关")//添加部署对象
						.addInputStream("parallelGateWay.bpmn", inputStreamBpmn)
						.addInputStream("parallelGateWay.png", inputStreamPng)
						.deploy();//完成部署
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * 启动流程实例  走分支 同时进行 
	 */
	@Test
	public void startProcessInstance() {
		//使用key值启动 默认按照最新版本的流程定义启动
		String processDefinitionKey="parallelGateWay";
		ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例 key对应bpmn文件的id属性值
		//一个流程实例 两个执行对象
		System.out.println("流程实例id"+processInstance.getId());//流程实例id
		System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
	}
	
	/**
	 * 查询当前人的个人任务act_ru_task
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee="商家";
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
	//买家付款 卖家发货
	@Test
	public void completeMyPersonalTask() {
		String taskId = "4503";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("完成任务 任务ID："+taskId);
	}
	
	
}
