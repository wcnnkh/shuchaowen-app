package scw.app.web;

import java.util.Map;

import scw.app.user.enums.AccountType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.UserLoginService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;
import scw.security.session.UserSession;
import scw.web.message.annotation.RequestBody;

@Controller(value = "user", methods = { HttpMethod.GET, HttpMethod.POST })
public class UserController {
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private UserLoginService userControllerService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Controller(value = "login")
	public Result login(String username, String password, HttpChannel httpChannel) {
		if (StringUtils.isEmpty(username, password)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByAccount(AccountType.USERNAME, username);
		if (user == null) {
			user = userService.getUserByAccount(AccountType.PHONE, username);
		}

		if (user == null) {
			return resultFactory.error("账号或密码错误");
		}

		Result result = userService.checkPassword(user.getUid(), password);
		if (result.isError()) {
			return result;
		}

		Map<String, Object> infoMap = userControllerService.login(user, httpChannel);
		return resultFactory.success(infoMap);
	}

	@Controller(value = "update")
	@LoginRequired
	public Result updateUserInfo(UserSession<Long> requestUser, @RequestBody UserAttributeModel userAttributeModel) {
		return userService.updateUserAttribute(requestUser.getUid(), userAttributeModel);
	}

	@Controller(value = "register")
	public Result register(String username, String password, @RequestBody UserAttributeModel userAttributeModel) {
		if (StringUtils.isEmpty(username, password)) {
			return resultFactory.parameterError();
		}

		return userService.register(AccountType.USERNAME, username, password, userAttributeModel);
	}

	@LoginRequired
	@Controller(value = "info")
	public Result info(UserSession<Long> requestUser) {
		User user = userService.getUser(requestUser.getUid());
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		Map<String, Object> map = userControllerService.info(user);
		return resultFactory.success(map);
	}
}
