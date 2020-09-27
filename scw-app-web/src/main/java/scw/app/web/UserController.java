package scw.app.web;

import java.util.HashMap;
import java.util.Map;

import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.RequestUser;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpCookie;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpRequest;
import scw.http.server.ServerHttpResponse;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.RequestBody;
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
	@Autowired
	private UserControllerService userControllerService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Controller(value = "login")
	public Result login(String username, String password, ServerHttpRequest request, ServerHttpResponse response) {
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

		Map<String, Object> infoMap = userControllerService.login(user, request, response);
		return resultFactory.success(infoMap);
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

	@Controller(value = "update")
	@LoginRequired
	public Result updateUserInfo(RequestUser requestUser, @RequestBody UserAttributeModel userAttributeModel) {
		return userService.updateUserAttribute(requestUser.getUid(), userAttributeModel);
	}

	@Controller(value = "register")
	public Result register(String username, String password, @RequestBody UserAttributeModel userAttributeModel) {
		if (StringUtils.isEmpty(username, password)) {
			return resultFactory.parameterError();
		}

		return userService.registerByUsername(username, password, userAttributeModel);
	}
}
