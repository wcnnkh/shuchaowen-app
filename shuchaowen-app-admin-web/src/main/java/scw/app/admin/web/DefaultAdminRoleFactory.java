package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Service;
import scw.core.utils.StringUtils;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.security.login.LoginService;
import scw.security.login.UserToken;

@Service
public class DefaultAdminRoleFactory implements AdminRoleFactory {
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private LoginService<Integer> loginService;

	public AdminRole getAdminRole(HttpChannel httpChannel, Action action) {
		String token = httpChannel.getValue("token").getAsString();
		if (StringUtils.isEmpty(token)) {
			return null;
		}

		Integer uid = httpChannel.getValue("uid").getAsInteger();
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
