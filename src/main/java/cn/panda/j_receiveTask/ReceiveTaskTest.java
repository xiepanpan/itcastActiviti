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
	 * 部署流程定义 (从inputStream)
	 */
	@Test
	//key值相同 资源升级
	public void deployementProcessDefinition_inputStream() {
		//从当前文件夹查找
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("receiveTask.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("接收活动任务")//添加部署对象
						.addInputStream("receiveTask.bpmn", inputStreamBpmn)
						.addInputStream("receiveTask.png", inputStreamPng)
						.deploy();//完成部署
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * 启动流程实例  设置流程变量 获取流程变量 向后执行一步
	 */
	@Test
	public void startProcessInstance() {
		//使用key值启动 默认按照最新版本的流程定义启动
		String processDefinitionKey="receiveTask";
		ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例 key对应bpmn文件的id属性值
		//一个流程实例 两个执行对象
		System.out.println("流程实例id"+processInstance.getId());//流程实例id
		System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
		
		//查询执行对象ID
		Execution execution = processEngine.getRuntimeService()
						.createExecutionQuery()
						.processInstanceId(processInstance.getId())
						.activityId("receivetask1")//当前活动id 对应receiveTask.bpmn 活动id
						.singleResult();
		
		//使用流程变量设置当日销售额 用来传递业务参数
		processEngine.getRuntimeService()
						.setVariable(execution.getId(), "汇总当日销量额", 21000);
		
		//向后执行一步
		processEngine.getRuntimeService()
						.signal(execution.getId());
		
		//查询执行对象ID
				Execution execution2 = processEngine.getRuntimeService()
								.createExecutionQuery()
								.processInstanceId(processInstance.getId())
								.activityId("receivetask2")//当前活动id 对应receiveTask.bpmn 活动id
								.singleResult();
		
		//从变量中获取汇总当日销售额的值
		Integer num = (Integer)processEngine.getRuntimeService()
						.getVariable(execution2.getId(), "汇总当日销量额");
		System.out.println("给老板发短信 金额："+num);
	}
	
	
}
