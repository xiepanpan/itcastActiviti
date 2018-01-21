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
	 * �������̶��� (��classpath)
	 */
	@Test
	public void deployementProcessDefinition_classpath() {
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("���̶���")//��Ӳ������
						.addClasspathResource("diagrams/helloworld.bpmn")//��classpath��Դ�м��� һ�μ���һ���ļ�
						.addClasspathResource("diagrams/helloworld.png")//��classpath��Դ�м��� һ�μ���һ���ļ�
						.deploy();//��ɲ���
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * �������̶��� (��zip)
	 */
	@Test
	//keyֵ��ͬ ��Դ����
	public void deployementProcessDefinition_zip() {
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream=new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
						.createDeployment()//�����������
						.name("���̶���")//��Ӳ������
						.addZipInputStream(zipInputStream)
						.deploy();//��ɲ���
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * ��ѯ���̶���
	 */
	@Test
	public void findProcessDefinition () {
		List<ProcessDefinition> list = processEngine.getRepositoryService()//�����̶���Ͳ�����ص�service
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
				System.out.println(processDefinition.getId());//���̶����key+�汾+���������
				System.out.println(processDefinition.getName());//Key��Name��ֵΪ��bpmn�ļ�process�ڵ��id��name������ֵ
				System.out.println(processDefinition.getVersion());
				System.out.println("��Դ����bpmn�ļ�"+processDefinition.getResourceName());
				System.out.println("��Դ����png�ļ�"+processDefinition.getDiagramResourceName());
				System.out.println("�������id��"+processDefinition.getDeploymentId());
			}
		}
	}
	
	/**
	 * ɾ�����̶���
	 */
	@Test
	public void deleteProcessDefinition () {
		String deploymentId = "601";
		//ֻ��ɾ��û������������ �����쳣
//		processEngine.getRepositoryService()
//						.deleteDeployment(deploymentId);
		//����ɾ�� ������Ҳɾ��
		processEngine.getRepositoryService()
						.deleteDeployment(deploymentId, true);
	}
	
	/**
	 * �鿴����ͼ
	 * @throws Exception 
	 */
	@Test
	public void viewPic() throws Exception {
		//�����ɵ�ͼƬ�ŵ��ļ�����
		String deploymentId ="801";
		//��ȡͼƬ��Դ������
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
		//��ȡ�ļ�������
		InputStream inputStream = processEngine.getRepositoryService()
						.getResourceAsStream(deploymentId, resourceName);
		//ͼƬ���ɵ�D��Ŀ¼��
		File file =new File("E:/"+resourceName);
		//����������ͼƬд��D����
		FileUtils.copyInputStreamToFile(inputStream, file);
	}
	
	/**
	 * ��ѯ���°汾�����̶���
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
				System.out.println(processDefinition.getId());//���̶����key+�汾+���������
				System.out.println(processDefinition.getName());//Key��Name��ֵΪ��bpmn�ļ�process�ڵ��id��name������ֵ
				System.out.println(processDefinition.getVersion());
				System.out.println("��Դ����bpmn�ļ�"+processDefinition.getResourceName());
				System.out.println("��Դ����png�ļ�"+processDefinition.getDiagramResourceName());
				System.out.println("�������id��"+processDefinition.getDeploymentId());
			}
		}
	}
	
	/**
	 * ɾ�����̶��壨ɾ��key��ͬ�����в�ͬ�汾�����̶��壩
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
