package scw.app.common.model;

import java.io.Serializable;

import scw.app.common.pojo.Task;
import scw.app.common.pojo.UserTask;

public class UserTaskInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Task task;
	private UserTask userTask;
	
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public UserTask getUserTask() {
		return userTask;
	}
	public void setUserTask(UserTask userTask) {
		this.userTask = userTask;
	}
}
