package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.action.authority.HttpActionAuthority;
import scw.mvc.action.authority.HttpActionAuthorityManager;
import scw.mvc.action.filter.ActionFilterChain;
import scw.mvc.action.filter.HttpActionFilter;
import scw.mvc.action.manager.HttpAction;
import scw.mvc.annotation.Authority;
import scw.mvc.http.HttpChannel;
import scw.util.result.ResultFactory;

@Configuration
public class AdminActionFilter extends HttpActionFilter {
	public static final String ROUTE_ATTR_NAME = "route";
	
	@Autowired
	private AdminRoleFactory adminRoleFactory;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	@Override
	protected Object doHttpFilter(HttpChannel channel, HttpAction action,
			ActionFilterChain chain) throws Throwable {
		AdminLogin adminLogin = action.getAnnotatedElement().getAnnotation(
				AdminLogin.class);
		Authority authority = action.getMethodAnnotatedElement().getAnnotation(
				Authority.class);
		if (adminLogin != null || authority != null) {
			AdminRole adminRole = adminRoleFactory
					.getAdminRole(channel, action);
			if (adminRole == null) {
				return resultFactory.authorizationFailure();
			}

			if (adminRole.getUserName().equals(
					AdminRoleService.DEFAULT_ADMIN_NAME)) {
				return chain.doFilter(channel, action);
			}

			if (authority != null) {
				HttpActionAuthority actionAuthority = httpActionAuthorityManager
						.getAuthority(action);
				if (actionAuthority == null) {
					channel.getLogger().warn("not found autority: {}", action);
					return chain.doFilter(channel, action);
				}
				// 权限判断
				if (adminRoleGroupActionService.check(adminRole.getGroupId(),
						actionAuthority.getId())) {
					return chain.doFilter(channel, action);
				}
				
				return resultFactory.error("权限不足");
			}
		}
		return chain.doFilter(channel, action);
	}

}
