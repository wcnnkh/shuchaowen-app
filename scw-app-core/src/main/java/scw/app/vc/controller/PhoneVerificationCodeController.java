package scw.app.vc.controller;

import scw.app.vc.enums.VerificationCodeType;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.Result;

@Controller(value = "/phone/code", methods = { HttpMethod.GET, HttpMethod.POST })
public class PhoneVerificationCodeController {
	private final PhoneVerificationCodeService phoneVerificationCodeService;

	public PhoneVerificationCodeController(PhoneVerificationCodeService phoneVerificationCodeService) {
		this.phoneVerificationCodeService = phoneVerificationCodeService;
	}

	@Controller(value = "send")
	public Result send(String phone, VerificationCodeType type) {
		return phoneVerificationCodeService.send(phone, type);
	}

	@Controller(value = "check")
	public Result check(String phone, String code, VerificationCodeType type) {
		return phoneVerificationCodeService.check(phone, code, type);
	}
}
