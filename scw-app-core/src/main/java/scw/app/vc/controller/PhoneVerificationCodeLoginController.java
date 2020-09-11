package scw.app.vc.controller;

import java.util.Map;

import scw.app.user.controller.UserController;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
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
public class PhoneVerificationCodeLoginController {
	private final PhoneVerificationCodeService phoneVerificationCodeService;
	private final UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private LoginManager loginManager;

	public PhoneVerificationCodeLoginController(PhoneVerificationCodeService phoneVerificationCodeService,
			UserService userService) {
		this.phoneVerificationCodeService = phoneVerificationCodeService;
		this.userService = userService;
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
}
