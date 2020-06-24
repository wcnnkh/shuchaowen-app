package scw.app.common.vc;

import scw.result.Result;

/**
 * 验证码发送
 * 
 * @author shuchaowen
 *
 */
public interface VerificationCodeSender {
	/**
	 * 发送验证码
	 * 
	 * @param user
	 * @return 如果成功，返回体就是验证码
	 */
	Result send(String user, String code);
}
