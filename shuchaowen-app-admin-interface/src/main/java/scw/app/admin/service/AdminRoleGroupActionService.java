package scw.app.admin.service;

import java.util.Collection;
import java.util.List;

import scw.app.admin.pojo.AdminRoleGroupAction;
import scw.result.Result;

public interface AdminRoleGroupActionService {
	boolean check(int groupId, String actionId);

	Result setActions(int groupId, Collection<String> authorityIds);
	
	List<AdminRoleGroupAction> getActionList(int groupId);
}
