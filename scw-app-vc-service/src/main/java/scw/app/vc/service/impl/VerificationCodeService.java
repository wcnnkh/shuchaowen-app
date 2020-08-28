package scw.app.vc.service.impl;

import scw.result.Result;

public interface VerificationCodeService {
	Result send(String user);

	Result check(String user, String code);
}
