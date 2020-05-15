package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.net.http.server.mvc.HttpChannel;
import scw.net.http.server.mvc.action.Action;
import scw.net.http.server.mvc.action.ActionFilter;
import scw.net.http.server.mvc.action.ActionService;
import scw.net.http.server.mvc.annotation.ActionAuthority;
import scw.result.ResultFactory;
import scw.security.authority.http.HttpAuthority;

@Configuration
public class AdminActionFilter implements ActionFilter {
	public static final String ROUTE_ATTR_NAME = "route";

	@Autowired
	private AdminRoleFactory adminRoleFactory;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	public Object doFilter(HttpChannel httpChannel, Action action, ActionService service) throws Throwable {
		AdminLogin adminLogin = action.getAnnotatedElement().getAnnotation(AdminLogin.class);
		ActionAuthority actionAuthority = action.getMethodAnnotatedElement().getAnnotation(ActionAuthority.class);
		if (adminLogin != null || actionAuthority != null) {
			AdminRole adminRole = adminRoleFactory.getAdminRole(httpChannel, action);
			if (adminRole == null) {
				return resultFactory.authorizationFailure();
			}

			if (adminRole.getUserName().equals(AdminRoleService.DEFAULT_ADMIN_NAME)) {
				return service.doAction(httpChannel, action);
			}

			if (actionAuthority != null) {
				HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action);
				if (httpAuthority == null) {
					httpChannel.getLogger().warn("not found autority: {}", action);
					return service.doAction(httpChannel, action);
				}
				// 权限判断
				if (adminRoleGroupActionService.check(adminRole.getGroupId(), httpAuthority.getId())) {
					return service.doAction(httpChannel, action);
				}

				return resultFactory.error("权限不足");
			}
		}
		return service.doAction(httpChannel, action);
	}

}
