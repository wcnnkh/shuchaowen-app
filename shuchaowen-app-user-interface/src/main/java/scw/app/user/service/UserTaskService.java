package scw.app.user.service;

import java.util.List;

import scw.app.user.model.TaskModel;
import scw.app.user.model.UserTaskInfo;
import scw.app.user.pojo.Task;
import scw.app.user.pojo.UserTask;
import scw.result.DataResult;
import scw.result.Result;

public interface UserTaskService {
	DataResult<Task> addTask(TaskModel taskModel);

	Result update(Task task);

	List<UserTask> getUserTaskList(long uid);

	List<UserTaskInfo> getUserTaskInfoList(long uid);
}
