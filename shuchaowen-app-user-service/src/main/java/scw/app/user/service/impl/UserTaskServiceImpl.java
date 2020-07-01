package scw.app.user.service.impl;

import java.util.List;

import scw.app.common.BaseServiceImpl;
import scw.app.user.model.TaskModel;
import scw.app.user.pojo.Task;
import scw.app.user.pojo.UserTask;
import scw.app.user.service.UserTaskService;
import scw.db.DB;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;

public class UserTaskServiceImpl extends BaseServiceImpl implements UserTaskService {

	public UserTaskServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
	}

	public List<UserTask> getUserTaskList(long uid) {
		return db.getByIdList(UserTask.class, uid);
	}

	public DataResult<Task> addTask(TaskModel taskModel) {
		Task task = new Task();
		Copy.copy(task, taskModel);
		db.save(task);
		return resultFactory.success(task);
	}

	public Result update(Task task) {
		db.update(task);
		return resultFactory.success(task);
	}
}
