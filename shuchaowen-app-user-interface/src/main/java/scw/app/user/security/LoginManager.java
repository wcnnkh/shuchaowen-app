package scw.app.user.security;

import scw.security.login.UserToken;

public interface LoginManager {
	UserToken<Long> login(LoginType type, long uid);

	UserToken<Long> getToken(LoginType type, String token);

	boolean cancelLogin(LoginType type, String token);
}
