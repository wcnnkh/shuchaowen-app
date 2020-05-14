package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.StringUtils;
import scw.mvc.action.manager.HttpAction;
import scw.mvc.http.HttpChannel;
import scw.security.login.LoginService;
import scw.security.login.UserToken;

@Configuration
public class DefaultAdminRoleFactory implements AdminRoleFactory {
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private LoginService<Integer> loginService;

	public AdminRole getAdminRole(HttpChannel httpChannel, HttpAction httpAction) {
		String token = httpChannel.getString("token");
		if (StringUtils.isEmpty(token)) {
			return null;
		}

		Integer uid = httpChannel.getInteger("uid");
		if (uid == null) {
			return null;
		}

		UserToken<Integer> userToken = loginService.getUserToken(token);
		if (userToken == null) {
			return null;
		}

		if (loginService.verification(token, uid)) {
			return adminRoleService.getById(uid);
		}

		return null;
	}

}
