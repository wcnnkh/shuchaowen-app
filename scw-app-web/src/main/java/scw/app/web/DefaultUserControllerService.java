package scw.app.web;

import java.util.HashMap;
import java.util.Map;

import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.RequestUser;
import scw.core.instance.annotation.Configuration;
import scw.http.HttpCookie;
import scw.http.server.ServerHttpRequest;
import scw.http.server.ServerHttpResponse;
import scw.security.login.UserToken;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultUserControllerService implements UserControllerService {
	private LoginManager loginManager;

	public DefaultUserControllerService(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public Map<String, Object> login(User user, ServerHttpRequest request, ServerHttpResponse response) {
		Map<String, Object> map = new HashMap<String, Object>(8);
		UserToken<Long> userToken = loginManager.login(user.getUid());
		map.put("token", userToken.getToken());
		map.put("uid", userToken.getUid());
		map.putAll(info(user));
		HttpCookie uidCookie = new HttpCookie(RequestUser.UID_NAME, userToken.getUid() + "");
		uidCookie.setPath("/");
		HttpCookie tokenCookie = new HttpCookie(RequestUser.TOKEN_NAME, userToken.getToken() + "");
		tokenCookie.setPath("/");
		response.addCookie(uidCookie);
		response.addCookie(tokenCookie);
		return map;
	}

	public Map<String, Object> info(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		return map;
	}
}
