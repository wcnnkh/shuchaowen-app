package scw.app.common.service;

import java.util.List;

import scw.app.common.model.TaskModel;
import scw.app.common.pojo.Task;
import scw.app.common.pojo.UserTask;
import scw.result.DataResult;
import scw.result.Result;

public interface UserTaskService {
	DataResult<Task> addTask(TaskModel taskModel);

	Result update(Task task);

	List<UserTask> getUserTaskList(long uid);
}
