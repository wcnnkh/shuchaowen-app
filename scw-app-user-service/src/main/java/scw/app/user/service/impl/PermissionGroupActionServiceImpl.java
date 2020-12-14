package scw.app.user.service.impl;

import java.util.Collection;
import java.util.List;

import scw.app.user.pojo.PermissionGroupAction;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;

@Service
public class PermissionGroupActionServiceImpl extends BaseServiceConfiguration implements PermissionGroupActionService {

	public PermissionGroupActionServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(PermissionGroupAction.class, false);
	}

	public boolean check(int groupId, String actionId) {
		return db.getById(PermissionGroupAction.class, groupId, actionId) != null;
	}

	public Result setActions(int groupId, Collection<String> actionIds) {
		List<PermissionGroupAction> actions = db.getByIdList(PermissionGroupAction.class, groupId);
		if (actions != null) {
			for (PermissionGroupAction action : actions) {
				db.delete(action);
			}
		}

		for (String actionId : actionIds) {
			PermissionGroupAction action = new PermissionGroupAction();
			action.setGroupId(groupId);
			action.setActionId(actionId);
			db.save(action);
		}
		return resultFactory.success();
	}

	public List<PermissionGroupAction> getActionList(int groupId) {
		return db.getByIdList(PermissionGroupAction.class, groupId);
	}

}
