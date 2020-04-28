package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.action.filter.ActionFilterChain;
import scw.mvc.action.filter.HttpActionFilter;
import scw.mvc.action.manager.HttpAction;
import scw.mvc.http.HttpChannel;
import scw.util.result.ResultFactory;

@Configuration
public class AdminActionFilter extends HttpActionFilter {
	@Autowired
	private AdminRoleFactory adminRoleFactory;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;

	@Override
	protected Object doHttpFilter(HttpChannel channel, HttpAction action,
			ActionFilterChain chain) throws Throwable {
		AdminRole adminRole = adminRoleFactory.getAdminRole(channel, action);
		if (adminRole == null) {
			return resultFactory.authorizationFailure();
		}

		return chain.doFilter(channel, action);
	}

}
