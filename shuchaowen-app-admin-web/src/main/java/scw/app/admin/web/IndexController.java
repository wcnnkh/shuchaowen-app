package scw.app.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scw.app.user.pojo.PermissionGroupAction;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.RequestUserToken;
import scw.app.user.security.SecurityActionInterceptor;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpRequest;
import scw.mapper.MapperUtils;
import scw.mvc.annotation.Controller;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.mvc.view.Redirect;
import scw.mvc.view.View;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.authority.AuthorityTree;
import scw.security.authority.MenuAuthorityFilter;
import scw.security.authority.http.HttpAuthority;
import scw.security.login.UserToken;

@Controller(value = "admin")
public class IndexController {
	@Autowired
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired
	private PageFactory pageFactory;
	@Autowired
	private LoginManager loginManager;

	@LoginRequired
	@Controller(value = "menus")
	@scw.mvc.annotation.ResultFactory
	public List<AuthorityTree<HttpAuthority>> getMenus(
			RequestUserToken requestUserToken) {
		if (userService.isSuperAdmin(requestUserToken.getUid())) {
			return httpActionAuthorityManager.getAuthorityTreeList(null,
					new MenuAuthorityFilter<HttpAuthority>());
		} else {
			User user = userService.getUser(requestUserToken.getUid());
			List<PermissionGroupAction> actions = permissionGroupActionService
					.getActionList(user.getPermissionGroupId());
			List<String> actionIds = MapperUtils.getMapper().getFieldValueList(
					actions, "actionId");
			return httpActionAuthorityManager.getRelationAuthorityTreeList(
					actionIds, new MenuAuthorityFilter<HttpAuthority>());
		}
	}

	@Controller
	@LoginRequired
	public Page index(RequestUserToken requestUserToken,
			ServerHttpRequest request) {
		Page page = pageFactory.getPage("/ftl/index.ftl");
		StringBuilder sb = new StringBuilder(4096);
		appendMenuHtml(sb, getMenus(requestUserToken), request.getContextPath());
		page.put("leftHtml", sb.toString());
		page.put("admin", userService.getUser(requestUserToken.getUid()));
		return page;
	}

	private void appendMenuHtml(StringBuilder sb,
			List<AuthorityTree<HttpAuthority>> authorityTrees,
			String contextPath) {
		if (authorityTrees == null || authorityTrees.isEmpty()) {
			return;
		}

		for (AuthorityTree<HttpAuthority> actionResult : authorityTrees) {
			HttpAuthority httpAuthority = actionResult.getAuthority();
			boolean isSub = !CollectionUtils.isEmpty(actionResult.getSubList());
			if (httpAuthority.isMenu() && !isSub) {
				// 一个菜单，但没有子集
				continue;
			}

			sb.append("<li>");
			sb.append("<a ");
			if (httpAuthority.isMenu()) {
				sb.append("href='javascript:;'");
			} else {
				sb.append("_href='");
				sb.append(contextPath + httpAuthority.getPath());
				sb.append("'");
			}
			sb.append(">");
			String icon = httpAuthority.getAttributeMap().get("icon");
			if (StringUtils.isNotEmpty(icon)) {
				sb.append("<i class='iconfont'>").append(icon).append("</i>");
			}
			sb.append("<cite>").append(httpAuthority.getName())
					.append("</cite>");
			if (isSub) {
				sb.append("<i class='iconfont nav_right'>&#xe697;</i>");
			}
			sb.append("</a>");
			if (isSub) {
				sb.append("<ul class='sub-menu'>");
				appendMenuHtml(sb, actionResult.getSubList(), contextPath);
				sb.append("</ul>");
			}
			sb.append("</li>");
		}
	}

	@Controller(value = "login")
	public View login() {
		return pageFactory.getPage("/ftl/login.ftl");
	}

	@Controller(value = "login", methods = HttpMethod.POST)
	public Result login(String username, String password) {
		if (StringUtils.isEmpty(username, password)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByUsername(username);
		if (user == null) {
			user = userService.getUserByPhone(username);
		}

		if (user == null) {
			return resultFactory.error("账号或密码错误");
		}

		Result result = userService.checkPassword(user.getUid(), password);
		if (result.isError()) {
			return result;
		}

		UserToken<Long> userToken = loginManager.login(user.getUid());
		Map<String, Object> map = new HashMap<String, Object>(8);
		map.put("user", user);
		map.put("token", userToken.getToken());
		map.put("uid", user.getUid());
		return resultFactory.success(map);
	}

	@LoginRequired
	@Controller(value = "welcome")
	public Page welcome() {
		return pageFactory.getPage("/ftl/welcome.ftl");
	}

	@LoginRequired
	@Controller(value = "update_pwd")
	public View update_pwd() {
		return pageFactory.getPage("/ftl/update_pwd.ftl");
	}

	@LoginRequired
	@Controller(value = "update_pwd", methods = HttpMethod.POST)
	public Result update_pwd(RequestUserToken requestUserToken, String oldPwd,
			String newPwd) {
		if (StringUtils.isNull(oldPwd, newPwd)) {
			return resultFactory.parameterError();
		}

		Result result = userService.checkPassword(requestUserToken.getUid(),
				oldPwd);
		if (result.isError()) {
			return resultFactory.error("旧密码错误");
		}

		return userService.updatePassword(requestUserToken.getUid(), newPwd);
	}
	
	@Controller(value = "cancel_login")
	public View cacelLogin(RequestUserToken requestUserToken, ServerHttpRequest request) {
		loginManager.cancelLogin(requestUserToken.getToken());
		return new Redirect(request.getContextPath() + SecurityActionInterceptor.ADMIN_LOGIN_PATH);
	}
}
