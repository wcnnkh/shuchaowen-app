package scw.app.admin.web.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scw.app.admin.model.AdminRoleGroupInfo;
import scw.app.admin.pojo.AdminRole;
import scw.app.admin.pojo.AdminRoleGroup;
import scw.app.admin.pojo.AdminRoleGroupAction;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleGroupService;
import scw.app.admin.service.AdminRoleService;
import scw.app.admin.web.AdminActionFilter;
import scw.app.admin.web.AdminLoginRequired;
import scw.app.common.model.ElementUiTree;
import scw.beans.annotation.Autowired;
import scw.core.annotation.KeyValuePair;
import scw.http.HttpMethod;
import scw.mapper.MapperUtils;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.annotation.ActionAuthorityParent;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.RequestBody;
import scw.mvc.annotation.ResultFactory;
import scw.mvc.exception.TapeDescriptionException;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.result.DataResult;
import scw.result.Result;
import scw.security.authority.AuthorityTree;
import scw.security.authority.http.HttpAuthority;

@ActionAuthorityParent(AdminRoleController.class)
@AdminLoginRequired
@Controller(value = "/admin/group/")
@ResultFactory
public class AdminRoleGroupController {
	@Autowired
	private AdminRoleGroupService adminRoleGroupService;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired
	private scw.result.ResultFactory resultFactory;

	@ActionAuthority(value = "管理员权限组", menu = true, attributes = { @KeyValuePair(key = AdminActionFilter.ROUTE_ATTR_NAME, value = "ManagementAuthority") })
	@Controller(value = "list")
	public Collection<AdminRoleGroup> list(int uid, int parentGroupId) {
		AdminRole adminRole = adminRoleService.getById(uid);
		if (adminRole == null) {
			throw new TapeDescriptionException("系统错误，用户不存在");
		}

		return adminRoleGroupService.getSubList(parentGroupId == 0 ? adminRole
				.getGroupId() : parentGroupId);
	}

	@AdminLoginRequired
	@Controller(value = "trees")
	public List<ElementUiTree<Integer>> getAdminRoleGroupTreeList(int uid) {
		AdminRole adminRole = adminRoleService.getById(uid);
		if (adminRole == null) {
			throw new TapeDescriptionException("系统错误，用户不存在");
		}

		return adminRoleGroupService.getAdminRoleGroupTreeList(adminRole
				.getGroupId());
	}

	@AdminLoginRequired
	@Controller(value = "authoritys")
	public Object getUserAuthority(int groupId, int uid) {
		AdminRoleGroup group = adminRoleGroupService.getById(groupId);
		if (group == null) {
			return resultFactory.error("参数错误");
		}

		AdminRole adminRole = adminRoleService.getById(uid);
		List<AuthorityTree<HttpAuthority>> authorityTrees;
		if (adminRole.getUserName().equals(AdminRoleService.DEFAULT_ADMIN_NAME)) {
			authorityTrees = httpActionAuthorityManager
					.getAuthorityTreeList(null);
		} else {
			List<AdminRoleGroupAction> adminRoleGroupActions = adminRoleGroupActionService
					.getActionList(adminRole.getGroupId());
			List<String> actionIds = MapperUtils.getMapper().getFieldValueList(
					adminRoleGroupActions, "actionId");
			authorityTrees = httpActionAuthorityManager
					.getRelationAuthorityTreeList(actionIds, null);
		}

		Map<String, Object> map = new HashMap<String, Object>(4);
		List<AdminRoleGroupAction> adminRoleGroupActions = adminRoleGroupActionService
				.getActionList(groupId);
		List<String> actionIds = MapperUtils.getMapper().getFieldValueList(
				adminRoleGroupActions, "actionId");
		map.put("authorityTrees", authorityTrees);
		map.put("selectedIds", actionIds);
		return map;
	}

	@ActionAuthority("创建/更新管理员权限组")
	@Controller(value = "create_or_update", methods = HttpMethod.POST)
	public DataResult<AdminRoleGroupInfo> create(
			@RequestBody AdminRoleGroupInfo adminRoleGroupInfo) {
		return adminRoleGroupService.createOrUpdate(adminRoleGroupInfo);
	}

	@ActionAuthority("启用或禁用权限组")
	@Controller(value = "disable", methods = HttpMethod.POST)
	public Result disable(int groupId, boolean disable) {
		return adminRoleGroupService.disableGroup(groupId, disable);
	}
}
