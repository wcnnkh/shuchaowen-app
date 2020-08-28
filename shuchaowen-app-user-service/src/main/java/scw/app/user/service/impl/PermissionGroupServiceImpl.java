package scw.app.user.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import scw.app.model.ElementUiTree;
import scw.app.user.model.PermissionGroupInfo;
import scw.app.user.pojo.PermissionGroup;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.PermissionGroupService;
import scw.app.util.BaseServiceImpl;
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
		db.createTable(PermissionGroup.class, false);
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
		adminRoleGroup.setDisable(adminRoleGroupInfo.isDisable());
		adminRoleGroup.setParentId(adminRoleGroupInfo.getParentId());
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

	public List<PermissionGroup> getSubList(int id, boolean ergodic) {
		List<PermissionGroup> subList = db.select(PermissionGroup.class,
				new SimpleSql("select * from permission_group where parentId=?", id));
		if (!ergodic) {
			return subList;
		}

		if (CollectionUtils.isEmpty(subList)) {
			return Collections.emptyList();
		}

		List<PermissionGroup> list = new ArrayList<PermissionGroup>();
		for (PermissionGroup permissionGroup : subList) {
			list.add(permissionGroup);
			list.addAll(getSubList(permissionGroup.getId(), ergodic));
		}
		return list;
	}

	public List<ElementUiTree<Integer>> getPermissionGroupTreeList(int parentGroupId) {
		List<PermissionGroup> adminRoleGroups = getSubList(parentGroupId, false);
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
}
