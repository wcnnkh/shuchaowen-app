package scw.app.user.service;

import java.util.List;

import scw.app.common.model.ElementUiTree;
import scw.app.user.model.PermissionGroupInfo;
import scw.app.user.pojo.PermissionGroup;
import scw.result.DataResult;
import scw.result.Result;

public interface PermissionGroupService {
	PermissionGroup getById(int id);

	List<PermissionGroup> getSubList(int id);

	DataResult<PermissionGroupInfo> createOrUpdate(
			PermissionGroupInfo adminRoleGroupInfo);

	Result disableGroup(int groupId, boolean disable);

	List<ElementUiTree<Integer>> getPermissionGroupTreeList(int parentGroupId);
	
	List<Integer> getAllSubList(int groupId);
}
