package scw.app.vc.service.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import scw.app.vc.enums.VerificationCodeType;
import scw.app.vc.service.AbstractVerificationCodeService;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.data.TemporaryStorage;
import scw.integration.alibaba.dayu.MessageModel;
import scw.integration.alibaba.dayu.Sender;

public class AliDaYuPhoneVerificationCodeService extends AbstractVerificationCodeService
		implements PhoneVerificationCodeService {
	private EnumMap<VerificationCodeType, MessageModel> messageModelMap = new EnumMap<VerificationCodeType, MessageModel>(
			VerificationCodeType.class);
	private Sender sender;
	private String product;

	public AliDaYuPhoneVerificationCodeService(TemporaryStorage temporaryCache, Sender sender, String product,
			ResultFactory resultFactory) {
		super(temporaryCache, resultFactory);
		this.sender = sender;
		this.product = product;
	}

	public EnumMap<VerificationCodeType, MessageModel> getMessageModelMap() {
		return messageModelMap;
	}

	@Override
	protected Result sendInternal(String user, String code, VerificationCodeType type) {
		if (StringUtils.isEmpty(user, code)) {
			throw new IllegalArgumentException("参数错误");
		}

		MessageModel messageModel = getMessageModelMap().get(type);
		if (messageModel == null) {
			throw new IllegalArgumentException("不支持的验证码类型(" + type + ")");
		}

		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("code", code);
		parameterMap.put("product", product);
		return sender.send(messageModel, parameterMap, user);
	}
}
