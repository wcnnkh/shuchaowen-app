package scw.app.admin.web;

import java.util.List;

import scw.app.user.pojo.PermissionGroupAction;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.RequestUserToken;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.http.server.ServerHttpRequest;
import scw.mapper.MapperUtils;
import scw.mvc.annotation.Controller;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.mvc.servlet.Jsp;
import scw.mvc.view.View;
import scw.result.ResultFactory;
import scw.security.authority.AuthorityTree;
import scw.security.authority.MenuAuthorityFilter;
import scw.security.authority.http.HttpAuthority;

@Controller(value="admin")
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

	public List<AuthorityTree<HttpAuthority>> getMenus(long uid) {
		if(userService.isSuperAdmin(uid)){
			return httpActionAuthorityManager.getAuthorityTreeList(
					null, new MenuAuthorityFilter<HttpAuthority>());
		} else {
			User user = userService.getUser(uid);
			List<PermissionGroupAction> actions = permissionGroupActionService
					.getActionList(user.getPermissionGroupId());
			List<String> actionIds = MapperUtils.getMapper().getFieldValueList(actions, "actionId");
			return httpActionAuthorityManager
					.getRelationAuthorityTreeList(actionIds,
							new MenuAuthorityFilter<HttpAuthority>());
		}
	}

	@Controller
	@LoginRequired
	public Page index(RequestUserToken requestUserToken, ServerHttpRequest request) {
		Page page = pageFactory.getPage("index.html");
		StringBuilder sb = new StringBuilder(4096);
		appendMenuHtml(sb, getMenus(requestUserToken.getUid()), request.getContextPath());
		page.put("leftHtml", sb.toString());
		page.put("admin", userService.getUser(requestUserToken.getUid()));
		return page;
	}

	private void appendMenuHtml(StringBuilder sb, List<AuthorityTree<HttpAuthority>> authorityTrees, String contextPath) {
		if (authorityTrees == null || authorityTrees.isEmpty()) {
			return;
		}

		for (AuthorityTree<HttpAuthority> actionResult : authorityTrees) {
			HttpAuthority httpAuthority = actionResult.getAuthority();
			boolean isSub = !CollectionUtils.isEmpty(actionResult.getSubList());
			if (httpAuthority.isMenu() && !isSub) {
				//一个菜单，但没有子集
				continue;
			}

			sb.append("<li>");
			sb.append("<a ");
			if(httpAuthority.isMenu()){
				sb.append("href='javascript:;'");
			} else {
				sb.append("_href='");
				sb.append(contextPath + httpAuthority.getPath());
				sb.append("'");
			}
			sb.append(">");
			String icon = httpAuthority.getAttributeMap().get("icon");
			if(StringUtils.isNotEmpty(icon)){
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
		return new Jsp("login.jsp");
	}

	@Controller(value = "to_login")
	public View toLogin() {
		return new Jsp("/to_login.jsp");
	}

	/*@Controller(value = "login", methods = HttpMethod.POST)
	public Object login(String username, String password, AdminToken adminToken, ServerHttpResponse serverHttpResponse) {
		JSONObject object = new JSONObject();
		DataResult<UserToken<Long>> result = adminService.login(username, password);
		if (result.isError()) {
			return result;
		}
		
		UserToken<Long> userSessionMetaData = result.getData();
		if (!ObjectUtils.isEmpty(userSessionMetaData)) {
			Admin admin = adminService.getAdmin(Long.valueOf(userSessionMetaData.getUid()));
			if (!ObjectUtils.isEmpty(admin)) {
				object.put("userName", admin.getUsername());
				object.put("id", result.getData().getToken());
				object.put("uid", result.getData().getUid());
			}
			adminToken.login(serverHttpResponse, userSessionMetaData);
		}
		return resultFactory.success(object);
	}

	@Controller(value = "welcome", interceptors = AdminFilter.class)
	public View welcome(AdminToken adminToken) {
		Jsp jsp = new Jsp("welcome.jsp");
		return jsp;
	}

	@Controller(value = "cancel_login", interceptors = AdminFilter.class)
	public View cacelLogin(AdminToken adminToken) {
		adminService.cacelLogin(adminToken.getAdmin());
		return new Redirect(adminToken.getRequest().getRequest().getContextPath() + AdminFilter.LOGIN_EXPIRED_URL);
	}

	@Controller(value = "signout_login", interceptors = AdminFilter.class)
	public Result signoutLogin(AdminToken adminToken) {
		adminService.cacelLogin(adminToken.getAdmin());
		return resultFactory.success();
	}

	@Controller(value = "update_pwd", interceptors = AdminFilter.class)
	public View update_pwd(AdminToken adminToken) {
		return new Jsp("update_pwd.jsp");
	}

	@Controller(value = "update_pwd", interceptors = AdminFilter.class, methods = HttpMethod.POST)
	public Result update_pwd(AdminToken adminToken, String oldPwd, String newPwd) {
		if (StringUtils.isNull(oldPwd, newPwd)) {
			return resultFactory.parameterError();
		}

		if (!SignatureUtils.md5(oldPwd, "UTF-8").equals(adminToken.getAdmin().getPassword())) {
			return resultFactory.error("旧密码错误");
		}

		return adminService.updatePwd(adminToken.getAdmin(), newPwd);
	}*/
}
