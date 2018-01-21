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
	 * 部署流程定义 (从InputStream)
	 */
	@Test
	//key值相同 资源升级
	public void deployementProcessDefinition_inputStream() {
		InputStream inputStreambpmn= this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");//从claspath中找
		InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("流程定义")//添加部署对象
						.addInputStream("processVariables.bpmn", inputStreambpmn)//使用资源文件的名称（与资源名称一致） 和输入流
						.addInputStream("processVariables.png", inputStreampng )
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
		String processDefinitionKey="processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例 key对应bpmn文件的id属性值
		System.out.println("流程实例id"+processInstance.getId());//流程实例id
		System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
	}
	
	/**
	 * 设置流程变量
	 */
	@Test
	public void setVariables() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "1504";
		
//		taskService.setVariable(taskId, "请假天数", 3);
//		taskService.setVariable(taskId, "请假日期", new Date());
//		taskService.setVariable(taskId, "请假原因", "回家相亲 吃饭");
		
		//使用javabean 
		Person person = new Person();
		person.setId(20);
		person.setName("翠花");
		taskService.setVariable(taskId, "人员信息(添加固定版本)", person);
		
		System.out.println("设置流程变量成功");
	}
	
	/**
	 * 获取流程变量
	 */
	@Test
	public void getVariables() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "1504";
//		Integer days = (Integer)taskService.getVariable(taskId, "请假天数");
//		Date date = (Date)taskService.getVariable(taskId, "请假日期");
//		String reason = (String)taskService.getVariable(taskId, "请假原因");
//		System.out.println("请假天数："+days);
//		System.out.println("请假日期："+date);
//		System.out.println("请假原因："+reason);
		
		// 获取javabean
		Person person = (Person)taskService.getVariable(taskId, "人员信息(添加固定版本)");
		System.out.println(person.getId()+" "+person.getName());
	}
	
	/**
	 * 模拟设置和获取流程变量的场景
	 */
	public void setAndGetVariables() {
		//与流程实例 执行对象（正在执行）
		RuntimeService runtimeService = processEngine.getRuntimeService();
		//与任务相关（正在执行）
		TaskService taskService = processEngine.getTaskService();
		//设置流程变量
//		runtimeService.setVariable(executionId, variableName, value);
//		runtimeService.setVariables(executionId, variables);//map集合
		
//		runtimeService.startProcessInstanceById(processDefinitionId, variables);//启动流程实例同时 设置流程变量 map集合
//		taskService.complete(taskId, variables);//完成任务同时 设置流程变量
		
		//获取流程变量
//		runtimeService.getVariable(executionId, variableName);
//		runtimeService.getVariables(executionId);
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyPersonalTask() {
		String taskId = "2702";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("完成任务 任务ID："+taskId);
	}
	
	/**
	 * 查询流程变量的历史表
	 */
	@Test
	public void findHistoryProcessVariables() {
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
						.createHistoricVariableInstanceQuery()
						.variableName("人员信息")
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricVariableInstance hvi : list) {
				System.out.println(hvi.getId()+" "+hvi.getVariableName()+" "+hvi.getValue());
			}
		}
	}
}
