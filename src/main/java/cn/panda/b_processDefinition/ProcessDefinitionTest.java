package cn.panda.b_processDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**
	 * 部署流程定义 (从classpath)
	 */
	@Test
	public void deployementProcessDefinition_classpath() {
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createDeployment()//创建部署对象
						.name("流程定义")//添加部署对象
						.addClasspathResource("diagrams/helloworld.bpmn")//从classpath资源中加载 一次加载一个文件
						.addClasspathResource("diagrams/helloworld.png")//从classpath资源中加载 一次加载一个文件
						.deploy();//完成部署
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
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
	 * 查询流程定义
	 */
	@Test
	public void findProcessDefinition () {
		List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署相关的service
						.createProcessDefinitionQuery()//
//						.deploymentId()
//						.processDefinitionId(processDefinitionId)
//						.processDefinitionKey(processDefinitionKey)
//						.processDefinitionNameLike(processDefinitionNameLike)
						.orderByProcessDefinitionVersion().asc()
//						.orderByProcessDefinitionName().desc()
						
						.list();
		if (list!=null&&list.size()>0) {
			for (ProcessDefinition processDefinition : list) {
				System.out.println(processDefinition.getId());//流程定义的key+版本+随机生成数
				System.out.println(processDefinition.getName());//Key和Name的值为：bpmn文件process节点的id和name的属性值
				System.out.println(processDefinition.getVersion());
				System.out.println("资源名称bpmn文件"+processDefinition.getResourceName());
				System.out.println("资源名称png文件"+processDefinition.getDiagramResourceName());
				System.out.println("部署对象id："+processDefinition.getDeploymentId());
			}
		}
	}
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteProcessDefinition () {
		String deploymentId = "601";
		//只能删除没有启动的流程 否则异常
//		processEngine.getRepositoryService()
//						.deleteDeployment(deploymentId);
		//级联删除 启动的也删除
		processEngine.getRepositoryService()
						.deleteDeployment(deploymentId, true);
	}
	
	/**
	 * 查看流程图
	 * @throws Exception 
	 */
	@Test
	public void viewPic() throws Exception {
		//将生成的图片放到文件夹下
		String deploymentId ="801";
		//获取图片资源的名称
		List<String> list = processEngine.getRepositoryService()
						.getDeploymentResourceNames(deploymentId);
		String resourceName = "";
		if(list!=null && list.size()>0){
			for (String name : list) {
				if (name.indexOf(".png")>=0) {
					resourceName=name;
				}
			}
		}
		//获取文件输入流
		InputStream inputStream = processEngine.getRepositoryService()
						.getResourceAsStream(deploymentId, resourceName);
		//图片生成到D盘目录下
		File file =new File("E:/"+resourceName);
		//将输入流的图片写到D盘下
		FileUtils.copyInputStreamToFile(inputStream, file);
	}
	
	/**
	 * 查询最新版本的流程定义
	 */
	@Test
	public void findLastVersionProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService()
						.createProcessDefinitionQuery()
						.orderByProcessDefinitionVersion().asc()
						.list();
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>(); 
		if (list!=null&&list.size()>0) {
			for (ProcessDefinition processDefinition : list) {
				map.put(processDefinition.getKey(), processDefinition);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
		if (pdList!=null&&pdList.size()>0) {
			for (ProcessDefinition processDefinition : pdList) {
				System.out.println(processDefinition.getId());//流程定义的key+版本+随机生成数
				System.out.println(processDefinition.getName());//Key和Name的值为：bpmn文件process节点的id和name的属性值
				System.out.println(processDefinition.getVersion());
				System.out.println("资源名称bpmn文件"+processDefinition.getResourceName());
				System.out.println("资源名称png文件"+processDefinition.getDiagramResourceName());
				System.out.println("部署对象id："+processDefinition.getDeploymentId());
			}
		}
	}
	
	/**
	 * 删除流程定义（删除key相同的所有不同版本的流程定义）
	 */
	@Test
	public void deleteProcessDefinitionByKey() {
		String processDefinitionKey="helloworld";
		List<ProcessDefinition> list = processEngine.getRepositoryService()
						.createProcessDefinitionQuery()
						.processDefinitionKey(processDefinitionKey)
						.list();
		if (list!=null&&list.size()>0) {
			for (ProcessDefinition processDefinition : list) {
				String deploymentId = processDefinition.getDeploymentId();
				processEngine.getRepositoryService()
								.deleteDeployment(deploymentId, true);
			}
		}
	}
}
