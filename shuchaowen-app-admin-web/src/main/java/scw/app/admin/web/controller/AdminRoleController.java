package scw.app.admin.web.controller;

import java.util.List;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.pojo.AdminRoleGroupAction;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleService;
import scw.app.admin.web.AdminActionFilter;
import scw.app.admin.web.AdminLogin;
import scw.beans.annotation.Autowired;
import scw.core.Pagination;
import scw.core.annotation.KeyValuePair;
import scw.http.HttpMethod;
import scw.mapper.MapperUtils;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;
import scw.mvc.exception.TapeDescriptionException;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.security.authority.AuthorityTree;
import scw.security.authority.MenuAuthorityFilter;
import scw.security.authority.http.HttpAuthority;

@ActionAuthority(value = "系统设置", menu = true)
@AdminLogin
@Controller(value = "/admin/user")
@ResultFactory
public class AdminRoleController {
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;

	@ActionAuthority(value = "管理员列表", menu = true, attributes = { @KeyValuePair(key = AdminActionFilter.ROUTE_ATTR_NAME, value = "ManagementList") })
	@Controller("list")
	public Pagination<List<AdminRole>> list(String userName, String nickName,
			int uid, int page, int limit) {
		AdminRole adminRole = adminRoleService.getById(uid);
		if (adminRole == null) {
			throw new TapeDescriptionException("系统错误，用户不存在");
		}

		return adminRoleService.getAdminRolePagination(adminRole.getGroupId(),
				page, limit, userName, nickName);
	}

	@ActionAuthority("创建/更新管理员信息")
	@Controller(value = "create_or_update", methods = HttpMethod.POST)
	public void create() {
	}

	@ActionAuthority("创建管理员")
	@Controller(value = "create", methods = HttpMethod.POST)
	public void update() {
	}

	@AdminLogin
	@Controller(value = "authoritys")
	public List<AuthorityTree<HttpAuthority>> getUserAuthority(int uid) {
		AdminRole adminRole = adminRoleService.getById(uid);
		List<AuthorityTree<HttpAuthority>> authorityTrees;
		if (adminRole.getUserName().equals(AdminRoleService.DEFAULT_ADMIN_NAME)) {
			authorityTrees = httpActionAuthorityManager.getAuthorityTreeList(
					null, new MenuAuthorityFilter<HttpAuthority>());
		} else {
			List<AdminRoleGroupAction> adminRoleGroupActions = adminRoleGroupActionService
					.getActionList(adminRole.getGroupId());
			List<String> actionIds = MapperUtils.getMapper().getFieldValueList(
					adminRoleGroupActions, "actionId");
			authorityTrees = httpActionAuthorityManager
					.getRelationAuthorityTreeList(actionIds, null);
		}
		return authorityTrees;
	}
}
