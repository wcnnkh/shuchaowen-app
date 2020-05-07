package scw.app.admin.service.impl;

import java.util.Collection;
import java.util.List;

import scw.app.admin.pojo.AdminRoleGroupAction;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Service;
import scw.db.DB;
import scw.util.result.Result;
import scw.util.result.ResultFactory;

@Service
public class AdminRoleGroupActionServiceImpl extends BaseImpl implements
		AdminRoleGroupActionService {
	@Autowired
	private ResultFactory resultFactory;

	public AdminRoleGroupActionServiceImpl(DB db) {
		super(db);
	}

	public boolean check(int groupId, String actionId) {
		return db.getById(AdminRoleGroupAction.class, groupId, actionId) != null;
	}

	public Result setActions(int groupId, Collection<String> actionIds) {
		List<AdminRoleGroupAction> actions = db.getByIdList(
				AdminRoleGroupAction.class, groupId);
		if (actions != null) {
			for (AdminRoleGroupAction action : actions) {
				db.delete(action);
			}
		}

		for (String actionId : actionIds) {
			AdminRoleGroupAction action = new AdminRoleGroupAction();
			action.setGroupId(groupId);
			action.setActionId(actionId);
			db.save(action);
		}
		return resultFactory.success();
	}

	public List<AdminRoleGroupAction> getActionList(int groupId) {
		return db.getByIdList(AdminRoleGroupAction.class, groupId);
	}

}
