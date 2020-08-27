package scw.app.user.security;

import scw.security.login.UserToken;

public interface LoginManager {
	UserToken<Long> login(long uid);

	UserToken<Long> getToken(String token);

	boolean cancelLogin(String token);
}
