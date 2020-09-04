package scw.app.vc.service;

import scw.app.vc.enums.VerificationCodeType;
import scw.result.Result;

/**
 * 手机号验证码服务
 * 
 * @author shuchaowen
 *
 */
public interface PhoneVerificationCodeService {
	/**
	 * 发送验证码
	 * 
	 * @param phone
	 * @param verificationCodeType
	 * @return
	 */
	Result send(String phone, VerificationCodeType verificationCodeType);

	/**
	 * 检查验证码
	 * 
	 * @param phone
	 * @param code
	 * @param verificationCodeType
	 * @return
	 */
	Result check(String phone, String code, VerificationCodeType verificationCodeType);
}
