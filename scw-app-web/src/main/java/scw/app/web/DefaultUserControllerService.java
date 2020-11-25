package scw.app.web;

import java.util.HashMap;
import java.util.Map;

import scw.app.user.pojo.User;
import scw.core.instance.annotation.Configuration;
import scw.http.HttpCookie;
import scw.mvc.HttpChannel;
import scw.mvc.security.UserSessionResolver;
import scw.security.session.UserSession;
import scw.util.XUtils;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultUserControllerService implements UserControllerService {

	public Map<String, Object> login(User user, HttpChannel httpChannel) {
		Map<String, Object> map = new HashMap<String, Object>(8);
		UserSession<Long> userSession = httpChannel.createUserSession(Long.class, user.getUid(), XUtils.getUUID());
		map.put("token", userSession.getId());
		map.put("uid", userSession.getUid());
		map.putAll(info(user));
		HttpCookie uidCookie = new HttpCookie(UserSessionResolver.UID_NAME, userSession.getUid() + "");
		uidCookie.setPath("/");
		HttpCookie tokenCookie = new HttpCookie(UserSessionResolver.TOKEN_NAME, userSession.getId() + "");
		tokenCookie.setPath("/");
		httpChannel.getResponse().addCookie(uidCookie);
		httpChannel.getResponse().addCookie(tokenCookie);
		return map;
	}

	public Map<String, Object> info(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		return map;
	}
}
