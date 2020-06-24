package scw.app.user.model;

import java.io.Serializable;

import scw.app.user.pojo.Task;
import scw.app.user.pojo.UserTask;

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
