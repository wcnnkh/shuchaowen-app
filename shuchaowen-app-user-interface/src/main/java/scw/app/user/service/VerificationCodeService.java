package scw.app.user.service;

import scw.app.user.enums.UnionIdType;
import scw.app.user.enums.VerificationCodeType;
import scw.mvc.annotation.Controller;
import scw.result.Result;

@Controller(value="user")
public interface VerificationCodeService {
	/**
	 * 发送验证码
	 * 
	 * @param unionId
	 * @param unionIdType
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "send_verification_code")
	Result sendVerificationCode(String unionId, UnionIdType unionIdType, VerificationCodeType verificationCodeType);

	/**
	 * 检查验证码
	 * 
	 * @param unionId
	 * @param unionIdType
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "check_verification_code")
	Result checkVerificationCode(String unionId, UnionIdType unionIdType, VerificationCodeType verificationCodeType);
}
