package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.action.authority.HttpActionAuthority;
import scw.mvc.action.authority.HttpActionAuthorityIdentify;
import scw.mvc.action.manager.HttpAction;
import scw.mvc.http.HttpChannel;
import scw.result.ResultFactory;
import scw.util.SimpleResult;

@Configuration(order = Integer.MIN_VALUE)
public class AdminHttpActionAuthorityIdentify implements HttpActionAuthorityIdentify {
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private AdminRoleFactory adminRoleFactory;
	@Autowired
	private ResultFactory resultFactory;

	public SimpleResult<Object> identify(HttpChannel httpChannel, HttpAction httpAction,
			HttpActionAuthority httpActionAuthority) throws Throwable {
		AdminRole adminRole = adminRoleFactory.getAdminRole(httpChannel, httpAction);
		if (adminRole == null) {
			return new SimpleResult<Object>(false, resultFactory.authorizationFailure());
		}

		if (adminRoleGroupActionService.check(adminRole.getGroupId(), httpActionAuthority.getId())) {
			return new SimpleResult<Object>(true, null);
		}

		return new SimpleResult<Object>(false, resultFactory.error("权限不足"));
	}

}
