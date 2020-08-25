package scw.app.user.security;

import scw.security.login.UserToken;
import scw.util.Accept;

public interface RequestUserToken extends Accept<UserToken<Long>>{
	Long getUid();
	
	String getToken();
	
	boolean accept(UserToken<Long> userToken);
}