package scw.app.user.service.impl;

import scw.app.user.pojo.enums.UnionIdType;
import scw.app.user.pojo.enums.VerificationCodeType;
import scw.app.user.service.VerificationCodeService;
import scw.core.instance.annotation.Configuration;
import scw.result.Result;

@Configuration(value=VerificationCodeService.class, order=Integer.MIN_VALUE)
public class VerificationCodeServiceImpl implements VerificationCodeService{
	
	
	public Result sendVerificationCode(String unionId, UnionIdType unionIdType,
			VerificationCodeType verificationCodeType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Result checkVerificationCode(String unionId, UnionIdType unionIdType,
			VerificationCodeType verificationCodeType) {
		// TODO Auto-generated method stub
		return null;
	}

}
