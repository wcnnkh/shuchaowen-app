package scw.app.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import scw.app.common.BaseServiceImpl;
import scw.app.common.model.ElementUiTree;
import scw.app.user.model.PermissionGroupInfo;
import scw.app.user.pojo.PermissionGroup;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.PermissionGroupService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.CollectionUtils;
import scw.db.DB;
import scw.io.serialzer.SerializerUtils;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.sql.SimpleSql;

@Configuration(order = Integer.MIN_VALUE)
public class PermissionGroupServiceImpl extends BaseServiceImpl implements PermissionGroupService {
	@Autowired
	private PermissionGroupActionService adminRoleGroupActionService;

	public PermissionGroupServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
	}

	public PermissionGroup getById(int id) {
		return db.getById(PermissionGroup.class, id);
	}

	public DataResult<PermissionGroupInfo> createOrUpdate(PermissionGroupInfo adminRoleGroupInfo) {
		PermissionGroup adminRoleGroup = getById(adminRoleGroupInfo.getId());
		if (adminRoleGroup == null) {
			adminRoleGroup = new PermissionGroup();
			adminRoleGroup.setId(adminRoleGroupInfo.getId());
		}
		adminRoleGroup.setName(adminRoleGroupInfo.getName());
		db.saveOrUpdate(adminRoleGroup);
		Result result = adminRoleGroupActionService.setActions(adminRoleGroup.getId(),
				adminRoleGroupInfo.getAuthorityIds());
		if (result.isSuccess()) {
			PermissionGroupInfo info = SerializerUtils.clone(adminRoleGroupInfo);
			info.setId(adminRoleGroup.getId());
			return resultFactory.success(info);
		}
		return result.dataResult();
	}

	public List<PermissionGroup> getSubList(int id) {
		return db.select(PermissionGroup.class, new SimpleSql("select * from admin_role_group where parentId=?", id));
	}

	public Result disableGroup(int groupId, boolean disable) {
		PermissionGroup adminRoleGroup = getById(groupId);
		if (adminRoleGroup == null) {
			return resultFactory.error("分组不存在");
		}

		adminRoleGroup.setDisable(disable);
		return resultFactory.success();
	}

	public List<ElementUiTree<Integer>> getPermissionGroupTreeList(int parentGroupId) {
		List<PermissionGroup> adminRoleGroups = getSubList(parentGroupId);
		if (CollectionUtils.isEmpty(adminRoleGroups)) {
			return null;
		}

		List<ElementUiTree<Integer>> elementUiTrees = new ArrayList<ElementUiTree<Integer>>();
		for (PermissionGroup group : adminRoleGroups) {
			ElementUiTree<Integer> tree = new ElementUiTree<Integer>(group.getId(), group.getName(),
					getPermissionGroupTreeList(group.getId()));
			elementUiTrees.add(tree);
		}
		return elementUiTrees;
	}

	public List<Integer> getAllSubList(int groupId) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(groupId);
		appendSubList(list, groupId);
		return list;
	}

	public void appendSubList(List<Integer> list, int groupId) {
		List<PermissionGroup> groups = getSubList(groupId);
		if (CollectionUtils.isEmpty(groups)) {
			return;
		}

		for (PermissionGroup group : groups) {
			list.add(group.getId());
			appendSubList(list, group.getId());
		}
	}
}
