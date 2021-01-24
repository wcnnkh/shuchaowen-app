package scw.app.user.service;

import java.util.List;

import scw.app.model.ElementUiTree;
import scw.app.user.model.PermissionGroupInfo;
import scw.app.user.pojo.PermissionGroup;
import scw.context.result.DataResult;

public interface PermissionGroupService {
	PermissionGroup getById(int id);

	/**
	 * 获取子权限组
	 * @param id
	 * @param ergodic 是否递归子集
	 * @return
	 */
	List<PermissionGroup> getSubList(int id, boolean ergodic);

	DataResult<PermissionGroupInfo> createOrUpdate(
			PermissionGroupInfo adminRoleGroupInfo);

	List<ElementUiTree<Integer>> getPermissionGroupTreeList(int parentGroupId);
}
