package cn.panda.k_personalTask2;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class TaskListenerImpl implements TaskListener {
	
	/**
	 * ָ������İ�����
	 */
	//ͨ�����Ұ�����
	public void notify(DelegateTask delegateTask) {
		delegateTask.setAssignee("�����ľ");
	}

}
