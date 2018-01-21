package cn.panda.c_processInstance;

import java.io.InputStream;
import java.util.List;
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

public class ProcessInstanceTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义 (从zip)
	 */
	@Test
	//key值相同 资源升级
	public void deployementProcessDefinition_zip() {
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream=new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("流程定义")//添加部署对象
						.addZipInputStream(zipInputStream)
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
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee="王五";
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
		String taskId = "1202";
		processEngine.getTaskService()
						.complete(taskId);
		System.out.println("完成任务 任务ID："+taskId);
	}
	
	/**
	 * 查询流程状态（判断流程正在执行还是结束） 
	 */
	@Test
	public void isProcessEnd() {
		String processInstanceId="1001";
		//正在执行的流程实例是否存在
		ProcessInstance processInstance = processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstanceId)
						.singleResult();
		if (processInstance==null) {
			System.out.println("流程已经结束");
		}
		else {
			System.out.println("流程没有结束");
		}
	}
	
	/**
	 * 查询历史任务
	 */
	@Test
	public void findHistoryTask() {
		String taskAssignee="张三";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
						.createHistoricTaskInstanceQuery()//创建历史任务实例查询
						.taskAssignee(taskAssignee)//指定历史任务的办理人
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricTaskInstance historicTaskInstance : list) {
				System.out.println(historicTaskInstance.getId()+" "+historicTaskInstance.getName());
			}
		}
	}
	
	/**
	 * 查询历史流程实例act_hi_procinst
	 */
	@Test
	public void findHistoryProcessInstance(){
		String processInstanceId="1001";
		HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
						.createHistoricProcessInstanceQuery()
						.processInstanceId(processInstanceId)
						.singleResult();
		System.out.println(historicProcessInstance.getId()+historicProcessInstance.getProcessDefinitionId()
		+historicProcessInstance.getStartTime());
	}
	
	/**
	 * 查询历史流程变量act_hi_varinst
	 */
	@Test
	public void findHistoryProcessVariables() {
		String processInstanceId = "1501";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
						.createHistoricVariableInstanceQuery()
						.processInstanceId(processInstanceId )
						.list();
		if (list!=null&&list.size()>0) {
			for (HistoricVariableInstance hvi : list) {
				System.out.println(hvi.getId()+" "+hvi.getVariableName()+" "+hvi.getValue());
			}
		}
	}
}
