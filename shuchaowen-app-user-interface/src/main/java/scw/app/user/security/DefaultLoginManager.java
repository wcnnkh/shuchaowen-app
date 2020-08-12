package scw.app.user.security;

import scw.core.instance.annotation.Configuration;
import scw.security.login.LoginService;
import scw.security.login.UserToken;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultLoginManager implements LoginManager {
	private LoginService<Long> loginService;

	public DefaultLoginManager(LoginService<Long> loginService) {
		this.loginService = loginService;
	}

	public UserToken<Long> login(LoginType type, long uid) {
		return loginService.login(uid);
	}

	public UserToken<Long> getToken(LoginType type, String token) {
		return loginService.getUserToken(token);
	}

	public boolean cancelLogin(LoginType type, String token) {
		return loginService.cancelLogin(token);
	}

}
