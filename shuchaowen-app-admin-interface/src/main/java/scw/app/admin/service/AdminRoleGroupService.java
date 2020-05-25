package scw.app.admin.service;

import java.util.List;

import scw.app.admin.model.AdminRoleGroupInfo;
import scw.app.admin.pojo.AdminRoleGroup;
import scw.app.common.model.ElementUiTree;
import scw.result.DataResult;
import scw.result.Result;

public interface AdminRoleGroupService {
	AdminRoleGroup getById(int id);

	List<AdminRoleGroup> getSubList(int id);

	DataResult<AdminRoleGroupInfo> createOrUpdate(
			AdminRoleGroupInfo adminRoleGroupInfo);

	Result disableGroup(int groupId, boolean disable);

	List<ElementUiTree<Integer>> getAdminRoleGroupTreeList(int parentGroupId);
	
	List<Integer> getAllSubList(int groupId);
}
