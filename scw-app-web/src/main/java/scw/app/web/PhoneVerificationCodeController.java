package scw.app.web;

import java.util.Map;

import scw.app.user.enums.AccountType;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.UserService;
import scw.app.vc.enums.VerificationCodeType;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.beans.annotation.Autowired;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;
import scw.security.session.UserSession;

@Controller(value = "/phone/code", methods = { HttpMethod.GET, HttpMethod.POST })
public class PhoneVerificationCodeController {
	private final PhoneVerificationCodeService phoneVerificationCodeService;
	private final UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private UserControllerService userControllerService;

	public PhoneVerificationCodeController(PhoneVerificationCodeService phoneVerificationCodeService,
			UserService userService) {
		this.phoneVerificationCodeService = phoneVerificationCodeService;
		this.userService = userService;
	}

	@Controller(value = "send")
	public Result send(String phone, VerificationCodeType type) {
		if (type == null || StringUtils.isEmpty(phone)) {
			return resultFactory.parameterError();
		}

		switch (type) {
		case REGISTER:
		case BIND:
			if (userService.getUserByAccount(AccountType.PHONE, phone) != null) {
				return resultFactory.error("该账号已注册");
			}
			break;
		default:
			break;
		}

		return phoneVerificationCodeService.send(phone, type);
	}

	@Controller(value = "check")
	public Result check(String phone, String code, VerificationCodeType type) {
		return phoneVerificationCodeService.check(phone, code, type);
	}

	@Controller(value = "login")
	public Result login(String phone, String code, HttpChannel httpChannel) {
		if (StringUtils.isEmpty(phone, code)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.LOGIN);
		if (result.isError()) {
			return result;
		}

		User user = userService.getUserByAccount(AccountType.PHONE, phone);
		if (user == null) {
			DataResult<User> dataResult = userService.register(AccountType.PHONE, phone, null, null);
			if (dataResult.isError()) {
				return dataResult;
			}

			user = dataResult.getData();
		}

		Map<String, Object> infoMap = userControllerService.login(user, httpChannel);
		return resultFactory.success(infoMap);
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

		User user = userService.getUserByAccount(AccountType.PHONE, phone);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		return userService.updatePassword(user.getUid(), password);
	}

	@Controller(value = "bind")
	@LoginRequired
	public Result bind(UserSession<Long> requestUser, String phone, String code) {
		if (StringUtils.isEmpty(phone, code)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.BIND);
		if (result.isError()) {
			return result;
		}

		return userService.bind(requestUser.getUid(), AccountType.PHONE, phone);
	}

	@Controller(value = "register")
	public Result register(String phone, String code, String password) {
		if (StringUtils.isEmpty(phone, code, password)) {
			return resultFactory.parameterError();
		}

		Result result = phoneVerificationCodeService.check(phone, code, VerificationCodeType.REGISTER);
		if (result.isError()) {
			return result;
		}

		return userService.register(AccountType.PHONE, phone, password, null);
	}
}
