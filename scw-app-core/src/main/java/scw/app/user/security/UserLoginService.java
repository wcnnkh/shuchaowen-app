package scw.app.user.security;

import java.util.Map;

import scw.app.user.pojo.User;
import scw.mvc.HttpChannel;

public interface UserLoginService {
	Map<String, Object> login(User user, HttpChannel httpChannel);
	
	Map<String, Object> info(User user);
}
