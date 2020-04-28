package scw.app.admin.web.controller;

import java.util.List;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.mvc.action.authority.HttpActionAuthority;
import scw.mvc.action.authority.HttpActionAuthorityManager;
import scw.mvc.annotation.Controller;
import scw.security.authority.AuthorityTree;
import scw.security.login.LoginService;
import scw.security.token.UserToken;
import scw.util.result.DataResult;
import scw.util.result.ResultFactory;

@Controller("admin")
public class IndexController {
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private LoginService<Integer> loginService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	@Controller(value = "login")
	public DataResult<UserToken<Integer>> login(String userName, String password) {
		if (StringUtils.isEmpty(userName, password)) {
			return resultFactory.parameterError();
		}

		AdminRole adminRole = adminRoleService.check(userName, password);
		if (adminRole == null) {
			return resultFactory.error("账号或密码错误");
		}

		return resultFactory.success(loginService.login(adminRole.getId()));
	}

	@Controller(value = "menus")
	public DataResult<List<AuthorityTree<HttpActionAuthority>>> getMenus() {
		return resultFactory.success(httpActionAuthorityManager
				.getAuthorityTreeList(null));
	}
}
