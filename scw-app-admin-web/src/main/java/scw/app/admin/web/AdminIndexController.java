package scw.app.admin.web;

import java.util.List;
import java.util.Map;

import scw.app.user.enums.AccountType;
import scw.app.user.pojo.PermissionGroupAction;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.SecurityProperties;
import scw.app.user.security.UserLoginService;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mapper.Field;
import scw.mapper.MapperUtils;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.mvc.view.Redirect;
import scw.mvc.view.View;
import scw.security.authority.AuthorityTree;
import scw.security.authority.MenuAuthorityFilter;
import scw.security.authority.http.HttpAuthority;
import scw.security.session.UserSession;
import scw.web.ServerHttpRequest;
import scw.web.model.Page;

@Controller(value = SecurityProperties.ADMIN_CONTROLLER)
public class AdminIndexController {
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired
	private UserLoginService userLoginService;
	@Autowired
	private SecurityProperties securityConfigProperties;

	public AdminIndexController(UserService userService, PermissionGroupActionService permissionGroupActionService) {
		this.userService = userService;
		this.permissionGroupActionService = permissionGroupActionService;
	}

	@LoginRequired
	@Controller(value = "menus")
	@scw.mvc.annotation.FactoryResult
	public List<AuthorityTree<HttpAuthority>> getMenus(UserSession<Long> requestUser) {
		if (userService.isSuperAdmin(requestUser.getUid())) {
			return httpActionAuthorityManager.getAuthorityTreeList(new MenuAuthorityFilter<HttpAuthority>());
		} else {
			User user = userService.getUser(requestUser.getUid());
			if (user == null) {
				throw new RuntimeException("用户不存在");
			}
			List<PermissionGroupAction> actions = permissionGroupActionService
					.getActionList(user.getPermissionGroupId());
			Field field = MapperUtils.getMapper().getFields(PermissionGroupAction.class).find("actionId", null);
			List<String> actionIds = field.getValues(actions);
			return httpActionAuthorityManager.getRelationAuthorityTreeList(actionIds,
					new MenuAuthorityFilter<HttpAuthority>());
		}
	}
	
	@Controller(value = "login", methods=HttpMethod.POST)
	public Result login(String username, String password, HttpChannel httpChannel) {
		if (StringUtils.isEmpty(username, password)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByAccount(AccountType.USERNAME, username);
		if (user == null) {
			user = userService.getUserByAccount(AccountType.PHONE, username);
		}

		if (user == null) {
			return resultFactory.error("账号或密码错误");
		}

		Result result = userService.checkPassword(user.getUid(), password);
		if (result.isError()) {
			return result;
		}

		Map<String, Object> infoMap = userLoginService.login(user, httpChannel);
		return resultFactory.success(infoMap);
	}

	@Controller
	@LoginRequired
	public Page index(UserSession<Long> requestUser, ServerHttpRequest request) {
		Page page = new Page("/admin/ftl/index.ftl");
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
			String icon = httpAuthority.getAttributeMap().get(AdminConstants.ICON_ATTR_NAME);
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
	public Object login(HttpChannel httpChannel) {
		UserSession<Long> userSession = httpChannel.getUserSession(Long.class);
		if(userSession != null){
			return new Redirect(httpChannel.getRequest().getContextPath() + securityConfigProperties.getController());
		}
		return new Page("/admin/ftl/login.ftl");
	}

	@LoginRequired
	@Controller(value = "welcome")
	public Page welcome() {
		return new Page("/admin/ftl/welcome.ftl");
	}

	@LoginRequired
	@Controller(value = "update_pwd")
	public Page update_pwd() {
		return new Page("/admin/ftl/update_pwd.ftl");
	}

	@LoginRequired
	@Controller(value = "update_pwd", methods = HttpMethod.POST)
	public Result update_pwd(UserSession<Long> requestUser, String oldPwd, String newPwd) {
		if (StringUtils.isEmpty(oldPwd, newPwd)) {
			return resultFactory.parameterError();
		}

		Result result = userService.checkPassword(requestUser.getUid(), oldPwd);
		if (result.isError()) {
			return resultFactory.error("旧密码错误");
		}

		return userService.updatePassword(requestUser.getUid(), newPwd);
	}

	@Controller(value = "cancel_login")
	public View cacelLogin(UserSession<Long> requestUser, ServerHttpRequest request) {
		requestUser.invalidate();
		return new Redirect(request.getContextPath() + securityConfigProperties.getToLoginPath());
	}

	@Controller(value = "to_login")
	public Page toLogin() {
		return new Page("/admin/ftl/to_login.ftl");
	}
}
