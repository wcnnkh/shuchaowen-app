package scw.app.vc.controller;

import scw.app.vc.enums.VerificationCodeType;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.Result;

@Controller(value = "/phone/code", methods={HttpMethod.GET, HttpMethod.POST})
public class PhoneVerificationCodeController {
	private final PhoneVerificationCodeService phoneVerificationCodeService;
	
	public PhoneVerificationCodeController(PhoneVerificationCodeService phoneVerificationCodeService){
		this.phoneVerificationCodeService = phoneVerificationCodeService;
	}
	
	/**
	 * 发送验证码
	 * 
	 * @param phone
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "send")
	public Result send(String phone, VerificationCodeType verificationCodeType){
		return phoneVerificationCodeService.send(phone, verificationCodeType);
	}

	/**
	 * 检查验证码
	 * 
	 * @param phone
	 * @param code
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "code")
	public Result check(String phone, String code, VerificationCodeType verificationCodeType){
		return phoneVerificationCodeService.check(phone, code, verificationCodeType);
	}
}
