package scw.app.common.service.impl;

import java.util.List;

import scw.app.common.model.TaskModel;
import scw.app.common.pojo.Task;
import scw.app.common.pojo.UserTask;
import scw.app.common.service.UserTaskService;
import scw.app.util.BaseServiceImpl;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;

@Configuration(order=Integer.MIN_VALUE)
public class UserTaskServiceImpl extends BaseServiceImpl implements UserTaskService {

	public UserTaskServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserTask.class, false);
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