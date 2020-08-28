package scw.app.vc.service;

import scw.app.vc.enums.VerificationCodeType;
import scw.mvc.annotation.Controller;
import scw.result.Result;

/**
 * 手机号验证码服务
 * 
 * @author shuchaowen
 *
 */
@Controller(value = "/phone/code")
public interface VerificationCodeService {
	/**
	 * 发送验证码
	 * 
	 * @param phone
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "send")
	Result send(String phone, VerificationCodeType verificationCodeType);

	/**
	 * 检查验证码
	 * 
	 * @param phone
	 * @param code
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "code")
	Result check(String phone, String code, VerificationCodeType verificationCodeType);
}
