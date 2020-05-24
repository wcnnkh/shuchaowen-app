package scw.app.admin.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import scw.app.admin.model.AdminRoleGroupInfo;
import scw.app.admin.pojo.AdminRoleGroup;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleGroupService;
import scw.app.common.model.ElementUiTree;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Service;
import scw.core.utils.CollectionUtils;
import scw.db.DB;
import scw.io.serialzer.SerializerUtils;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.sql.SimpleSql;

@Service
public class AdminRoleGroupServiceImpl extends BaseImpl implements
		AdminRoleGroupService {
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private ResultFactory resultFactory;

	public AdminRoleGroupServiceImpl(DB db) {
		super(db);
	}

	public AdminRoleGroup getById(int id) {
		return db.getById(AdminRoleGroup.class, id);
	}

	public DataResult<AdminRoleGroupInfo> createOrUpdate(
			AdminRoleGroupInfo adminRoleGroupInfo) {
		AdminRoleGroup adminRoleGroup = getById(adminRoleGroupInfo.getId());
		if (adminRoleGroup == null) {
			adminRoleGroup = new AdminRoleGroup();
			adminRoleGroup.setId(adminRoleGroupInfo.getId());
		}
		adminRoleGroup.setName(adminRoleGroupInfo.getName());
		db.saveOrUpdate(adminRoleGroup);
		Result result = adminRoleGroupActionService.setActions(
				adminRoleGroup.getId(), adminRoleGroupInfo.getAuthorityIds());
		if (result.isSuccess()) {
			AdminRoleGroupInfo info = SerializerUtils.clone(adminRoleGroupInfo);
			info.setId(adminRoleGroup.getId());
			return resultFactory.success(info);
		}
		return new DataResult<AdminRoleGroupInfo>(result);
	}

	public List<AdminRoleGroup> getSubList(int id) {
		return db.select(AdminRoleGroup.class, new SimpleSql(
				"select * from admin_role_group where parentId=?", id));
	}

	public Result disableGroup(int groupId, boolean disable) {
		AdminRoleGroup adminRoleGroup = getById(groupId);
		if (adminRoleGroup == null) {
			return resultFactory.error("分组不存在");
		}

		adminRoleGroup.setDisable(disable);
		return resultFactory.success();
	}

	public List<ElementUiTree<Integer>> getAdminRoleGroupTreeList(
			int parentGroupId) {
		List<AdminRoleGroup> adminRoleGroups = getSubList(parentGroupId);
		if (CollectionUtils.isEmpty(adminRoleGroups)) {
			return null;
		}

		List<ElementUiTree<Integer>> elementUiTrees = new LinkedList<ElementUiTree<Integer>>();
		for (AdminRoleGroup group : adminRoleGroups) {
			ElementUiTree<Integer> tree = new ElementUiTree<Integer>(
					group.getId(), group.getName(),
					getAdminRoleGroupTreeList(group.getId()));
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
		List<AdminRoleGroup> groups = getSubList(groupId);
		if (CollectionUtils.isEmpty(groups)) {
			return;
		}

		for (AdminRoleGroup group : groups) {
			list.add(group.getId());
			appendSubList(list, group.getId());
		}
	}
}
