package scw.app.user.service;

import java.util.Collection;
import java.util.List;

import scw.app.user.pojo.PermissionGroupAction;
import scw.result.Result;

public interface PermissionGroupActionService {
	boolean check(int groupId, String actionId);

	Result setActions(int groupId, Collection<String> authorityIds);
	
	List<PermissionGroupAction> getActionList(int groupId);
}
