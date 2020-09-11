package scw.app.vc.controller;

import java.util.Map;

import scw.app.user.controller.UserController;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.RequestUser;
import scw.app.user.service.UserService;
import scw.app.vc.enums.VerificationCodeType;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpResponse;
import scw.mvc.annotation.Controller;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.UserToken;

@Controller(value = "/phone/code", methods = { HttpMethod.GET, HttpMethod.POST })
public class PhoneVerificationCodeController {
	private final PhoneVerificationCodeService phoneVerificationCodeService;
	private final UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private LoginManager loginManager;

	public PhoneVerificationCodeController(PhoneVerificationCodeService phoneVerificationCodeService,
			UserService userService) {
		this.phoneVerificationCodeService = phoneVerificationCodeService;
		this.userService = userService;
	}

	@Controller(value = "send")
	public Result send(String phone, VerificationCodeType type) {
		return phoneVerificationCodeService.send(phone, type);
	}

	@Controller(value = "check")
	public Result check(String phone, String code, VerificationCodeType type) {
		return phoneVerificationCodeService.check(phone, code, type);
	}

	@Controller(value = "login")
	public Result login(String phone, String code, ServerHttpResponse response) {
		if (StringUtils.isEmpty(phone, code)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.LOGIN);
		if (result.isError()) {
			return result;
		}

		User user = userService.getUserByPhone(phone);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		UserToken<Long> userToken = loginManager.login(user.getUid());
		Map<String, Object> map = UserController.login(userToken, response);
		map.put("user", user);
		return resultFactory.success(map);
	}

	@Controller(value = "update_pwd")
	public Result updatePassword(String phone, String code, String password) {
		if (StringUtils.isEmpty(phone, code, password)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.UPDATE_PASSWORD);
		if (result.isError()) {
			return result;
		}

		User user = userService.getUserByPhone(phone);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		return userService.updatePassword(user.getUid(), password);
	}

	@Controller(value = "bind")
	@LoginRequired
	public Result bind(RequestUser requestUser, String phone, String code) {
		if (StringUtils.isEmpty(phone, code)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.BIND);
		if (result.isError()) {
			return result;
		}

		return userService.bindPhone(requestUser.getUid(), phone);
	}

	@Controller(value = "register")
	public Result register(String phone, String code, String password) {
		if (StringUtils.isEmpty(phone, code)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.BIND);
		if (result.isError()) {
			return result;
		}

		return userService.registerByPhone(phone, password, null);
	}
}
