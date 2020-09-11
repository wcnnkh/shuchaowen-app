package scw.app.user.controller;

import java.util.HashMap;
import java.util.Map;

import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.RequestUser;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpCookie;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpResponse;
import scw.mvc.annotation.Controller;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.UserToken;

@Controller(value = "user", methods = { HttpMethod.GET, HttpMethod.POST })
public class UserController {
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private LoginManager loginManager;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Controller(value = "login")
	public Result login(String username, String password, ServerHttpResponse response) {
		if (StringUtils.isEmpty(username, password)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByUsername(username);
		if (user == null) {
			user = userService.getUserByPhone(username);
		}

		if (user == null) {
			return resultFactory.error("账号或密码错误");
		}

		Result result = userService.checkPassword(user.getUid(), password);
		if (result.isError()) {
			return result;
		}

		UserToken<Long> userToken = loginManager.login(user.getUid());
		Map<String, Object> map = login(userToken, response);
		map.put("user", user);
		return resultFactory.success(map);
	}

	public static Map<String, Object> login(UserToken<Long> userToken, ServerHttpResponse response) {
		Map<String, Object> map = new HashMap<String, Object>(8);
		map.put("token", userToken.getToken());
		map.put("uid", userToken.getUid());
		HttpCookie uidCookie = new HttpCookie(RequestUser.UID_NAME, userToken.getUid() + "");
		uidCookie.setPath("/");
		HttpCookie tokenCookie = new HttpCookie(RequestUser.TOKEN_NAME, userToken.getToken() + "");
		tokenCookie.setPath("/");
		response.addCookie(uidCookie);
		response.addCookie(tokenCookie);
		return map;
	}
}
