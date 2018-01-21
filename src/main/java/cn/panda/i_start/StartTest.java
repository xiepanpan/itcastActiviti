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
	 * 部署流程定义 (从inputStream)
	 */
	@Test
	//key值相同 资源升级
	public void deployementProcessDefinition_inputStream() {
		//从当前文件夹查找
		InputStream inputStreamBpmn =this.getClass().getResourceAsStream("start.bpmn");
		InputStream inputStreamPng =this.getClass().getResourceAsStream("start.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("开始活动")//添加部署对象
						.addInputStream("start.bpmn", inputStreamBpmn)
						.addInputStream("start.png", inputStreamPng)
						.deploy();//完成部署
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * 启动流程实例  判断流程是否结束 查询历史
	 */
	@Test
	public void startProcessInstance() {
		//使用key值启动 默认按照最新版本的流程定义启动
		String processDefinitionKey="start";
		ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例 key对应bpmn文件的id属性值
		//一个流程实例 两个执行对象
		System.out.println("流程实例id"+processInstance.getId());//流程实例id
		System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
		
		//判断流程是否结束 查询正在执行的执行对象表
		ProcessInstance resultpi = processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstance.getId())
						.singleResult();
		if (resultpi==null) {
			//查询历史 获取流程相关信息
			HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
							.createHistoricProcessInstanceQuery()
							.processInstanceId(processInstance.getId())
							.singleResult();
			System.out.println(historicProcessInstance.getId()+" "+historicProcessInstance.getStartTime());
		}
	}
	
	
}
