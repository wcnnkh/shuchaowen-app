package scw.app.user.service;

import scw.app.user.enums.VerificationCodeType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;
import scw.security.login.UserToken;

/**
 * 手机号验证码服务
 * 
 * @author shuchaowen
 *
 */
@Controller(value = "code")
public interface VerificationCodeService {
	/**
	 * 发送验证码
	 * 
	 * @param phone
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "sendPhoneCode")
	Result sendPhoneCode(String phone, VerificationCodeType verificationCodeType);

	/**
	 * 检查验证码
	 * 
	 * @param phone
	 * @param code
	 * @param verificationCodeType
	 * @return
	 */
	@Controller(value = "checkPhoneCode")
	Result checkPhoneCode(String phone, String code, VerificationCodeType verificationCodeType);

	DataResult<User> registerByPhoneCode(String phone, String code, String password,
			UserAttributeModel userAttributeModel);

	DataResult<UserToken<Long>> loginByPhoneCode(String phone, String code);
}
