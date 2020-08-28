package scw.app.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scw.app.user.pojo.PermissionGroup;
import scw.app.user.pojo.PermissionGroupAction;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.RequestUser;
import scw.app.user.security.SecurityActionInterceptor;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpRequest;
import scw.http.server.ServerHttpResponse;
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
public class AdminIndexController {
	@Autowired
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Autowired
	private PageFactory pageFactory;
	@Autowired
	private LoginManager loginManager;

	@LoginRequired
	@Controller(value = "menus")
	@scw.mvc.annotation.ResultFactory
	public List<AuthorityTree<HttpAuthority>> getMenus(RequestUser requestUser) {
		if (userService.isSuperAdmin(requestUser.getUid())) {
			return httpActionAuthorityManager.getAuthorityTreeList(new MenuAuthorityFilter<HttpAuthority>());
		} else {
			User user = userService.getUser(requestUser.getUid());
			List<PermissionGroupAction> actions = permissionGroupActionService
					.getActionList(user.getPermissionGroupId());
			List<String> actionIds = MapperUtils.getMapper().getFieldValueList(actions, "actionId");
			return httpActionAuthorityManager.getRelationAuthorityTreeList(actionIds,
					new MenuAuthorityFilter<HttpAuthority>());
		}
	}

	@Controller
	@LoginRequired
	public Page index(RequestUser requestUser, ServerHttpRequest request) {
		Page page = pageFactory.getPage("/admin/ftl/index.ftl");
		StringBuilder sb = new StringBuilder(4096);
		appendMenuHtml(sb, getMenus(requestUser), request.getContextPath());
		page.put("leftHtml", sb.toString());
		page.put("admin", userService.getUser(requestUser.getUid()));
		return page;
	}

	private void appendMenuHtml(StringBuilder sb, List<AuthorityTree<HttpAuthority>> authorityTrees,
			String contextPath) {
		if (authorityTrees == null || authorityTrees.isEmpty()) {
			return;
		}

		for (AuthorityTree<HttpAuthority> actionResult : authorityTrees) {
			HttpAuthority httpAuthority = actionResult.getAuthority();
			boolean isSub = !CollectionUtils.isEmpty(actionResult.getSubList());
			sb.append("<li>");
			sb.append("<a ");
			if (isSub) {
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
			sb.append("<cite>").append(httpAuthority.getName()).append("</cite>");
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
		return pageFactory.getPage("/admin/ftl/login.ftl");
	}

	@Controller(value = "login", methods = HttpMethod.POST)
	public Result login(String username, String password, ServerHttpResponse httpResponse) {
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

		if (!userService.isSuperAdmin(user.getUid())) {
			PermissionGroup group = permissionGroupService.getById(user.getPermissionGroupId());
			if (group == null) {
				return resultFactory.error("权限不足，无法登录，请联系管理员");
			}
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
		httpResponse.addCookie(RequestUser.UID_NAME, user.getUid() + "");
		httpResponse.addCookie(RequestUser.TOKEN_NAME, userToken.getToken());
		return resultFactory.success(map);
	}

	@LoginRequired
	@Controller(value = "welcome")
	public Page welcome() {
		return pageFactory.getPage("/admin/ftl/welcome.ftl");
	}

	@LoginRequired
	@Controller(value = "update_pwd")
	public View update_pwd() {
		return pageFactory.getPage("/admin/ftl/update_pwd.ftl");
	}

	@LoginRequired
	@Controller(value = "update_pwd", methods = HttpMethod.POST)
	public Result update_pwd(RequestUser requestUser, String oldPwd, String newPwd) {
		if (StringUtils.isNull(oldPwd, newPwd)) {
			return resultFactory.parameterError();
		}

		Result result = userService.checkPassword(requestUser.getUid(), oldPwd);
		if (result.isError()) {
			return resultFactory.error("旧密码错误");
		}

		return userService.updatePassword(requestUser.getUid(), newPwd);
	}

	@Controller(value = "cancel_login")
	public View cacelLogin(RequestUser requestUser, ServerHttpRequest request) {
		loginManager.cancelLogin(requestUser.getToken());
		return new Redirect(request.getContextPath() + SecurityActionInterceptor.ADMIN_LOGIN_PATH);
	}

	@Controller(value = "to_login")
	public View toLogin() {
		return pageFactory.getPage("/admin/ftl/to_login.ftl");
	}
}