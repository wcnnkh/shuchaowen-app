package scw.app.user.security;

import scw.core.GlobalPropertyFactory;
import scw.security.login.UserToken;
import scw.util.Accept;

public interface RequestUser extends Accept<UserToken<Long>> {
	static final String TOKEN_NAME = GlobalPropertyFactory.getInstance().getValue("user.security.token.name",
			String.class, "token");
	static final String UID_NAME = GlobalPropertyFactory.getInstance().getValue("user.security.uid.name", String.class,
			"uid");

	Long getUid();

	String getToken();
	
	boolean isLogin();

	boolean accept(UserToken<Long> userToken);
}