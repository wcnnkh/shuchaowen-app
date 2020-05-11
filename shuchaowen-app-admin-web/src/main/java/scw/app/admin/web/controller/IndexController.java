package scw.app.admin.web.controller;

import java.util.List;

import scw.app.admin.model.AdminLoginInfo;
import scw.app.admin.pojo.AdminRole;
import scw.app.admin.pojo.AdminRoleGroupAction;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleService;
import scw.app.admin.web.AdminLogin;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.mapper.MapperUtils;
import scw.mvc.action.authority.HttpActionAuthorityManager;
import scw.mvc.annotation.Controller;
import scw.net.http.HttpMethod;
import scw.result.DataResult;
import scw.result.ResultFactory;
import scw.security.authority.AuthorityTree;
import scw.security.authority.MenuAuthorityFilter;
import scw.security.authority.http.HttpAuthority;
import scw.security.login.LoginService;

@Controller("admin")
public class IndexController {
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private LoginService<Integer> loginService;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	@Controller(value = "login", methods = HttpMethod.POST)
	public DataResult<AdminLoginInfo> login(String userName, String password) {
		if (StringUtils.isEmpty(userName, password)) {
			return resultFactory.parameterError();
		}

		AdminRole adminRole = adminRoleService.check(userName, password);
		if (adminRole == null) {
			return resultFactory.error("账号或密码错误");
		}

		AdminLoginInfo adminLoginInfo = new AdminLoginInfo();
		adminLoginInfo.setUserToken(loginService.login(adminRole.getId()));
		adminLoginInfo.setRole(adminRole);
		return resultFactory.success(adminLoginInfo);
	}

	@AdminLogin
	@Controller(value = "menus")
	public DataResult<List<AuthorityTree<HttpAuthority>>> getMenus(int uid) {
		AdminRole adminRole = adminRoleService.getById(uid);
		if (adminRole == null) {
			return resultFactory.error("用户不存在");
		}

		List<AuthorityTree<HttpAuthority>> authorityTrees;
		if (adminRole.getUserName().equals(AdminRoleService.DEFAULT_ADMIN_NAME)) {
			authorityTrees = httpActionAuthorityManager.getAuthorityTreeList(
					null, new MenuAuthorityFilter<HttpAuthority>());
		} else {
			List<AdminRoleGroupAction> adminRoleGroupActions = adminRoleGroupActionService
					.getActionList(adminRole.getGroupId());
			List<String> actionIds = MapperUtils.getMapper().getFieldValueList(adminRoleGroupActions, "actionId");
			authorityTrees = httpActionAuthorityManager
					.getRelationAuthorityTreeList(actionIds,
							new MenuAuthorityFilter<HttpAuthority>());
		}
		return resultFactory.success(authorityTrees);
	}
}
