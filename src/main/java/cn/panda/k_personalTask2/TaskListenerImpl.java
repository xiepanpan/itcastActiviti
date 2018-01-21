package cn.panda.k_personalTask2;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class TaskListenerImpl implements TaskListener {
	
	/**
	 * 指定任务的办理人
	 */
	//通过类找办理人
	public void notify(DelegateTask delegateTask) {
		delegateTask.setAssignee("东尼大木");
	}

}
