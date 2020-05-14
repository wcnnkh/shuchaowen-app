package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.Channel;
import scw.mvc.action.Action;
import scw.mvc.action.authority.HttpActionAuthorityManager;
import scw.mvc.action.filter.ActionFilter;
import scw.mvc.action.filter.ActionFilterChain;
import scw.mvc.annotation.ActionAuthority;
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

	public Object doFilter(Channel channel, Action action,
			ActionFilterChain chain) throws Throwable {
		AdminLogin adminLogin = action.getAnnotatedElement().getAnnotation(
				AdminLogin.class);
		ActionAuthority actionAuthority = action.getMethodAnnotatedElement().getAnnotation(
				ActionAuthority.class);
		if (adminLogin != null || actionAuthority != null) {
			AdminRole adminRole = adminRoleFactory
					.getAdminRole(channel, action);
			if (adminRole == null) {
				return resultFactory.authorizationFailure();
			}

			if (adminRole.getUserName().equals(
					AdminRoleService.DEFAULT_ADMIN_NAME)) {
				return chain.doFilter(channel, action);
			}

			if (actionAuthority != null) {
				HttpAuthority httpAuthority = httpActionAuthorityManager
						.getAuthority(action);
				if (httpAuthority == null) {
					channel.getLogger().warn("not found autority: {}", action);
					return chain.doFilter(channel, action);
				}
				// 权限判断
				if (adminRoleGroupActionService.check(adminRole.getGroupId(),
						httpAuthority.getId())) {
					return chain.doFilter(channel, action);
				}
				
				return resultFactory.error("权限不足");
			}
		}
		return chain.doFilter(channel, action);
	}

}
