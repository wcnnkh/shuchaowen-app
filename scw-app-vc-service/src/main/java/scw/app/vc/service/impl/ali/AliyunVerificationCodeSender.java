package scw.app.vc.service.impl.ali;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;

import scw.app.vc.service.impl.VerificationCodeSender;
import scw.json.JSONUtils;
import scw.json.JsonObject;
import scw.logger.Logger;
import scw.logger.LoggerUtils;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.util.RandomUtils;

public abstract class AliyunVerificationCodeSender implements VerificationCodeSender {
	private static final String SUCCESS_CODE = "OK";
	private static Logger logger = LoggerUtils.getLogger(AliyunVerificationCodeSender.class);
	private IAcsClient client;
	private ResultFactory resultFactory;
	private String signName;
	private String templateCode;

	public AliyunVerificationCodeSender(IAcsClient client, ResultFactory resultFactory, String signName,
			String templateCode) {
		this.client = client;
		this.signName = signName;
		this.templateCode = templateCode;
		this.resultFactory = resultFactory;
	}

	public Result send(String phone, String code) {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", phone);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", templateCode);
		request.putQueryParameter("TemplateParam", createTemplateParam(phone, code));
		try {
			CommonResponse response = client.getCommonResponse(request);
			JsonObject json = JSONUtils.parseObject(response.getData());
			if (json.getString("Code").equals(SUCCESS_CODE)) {
				return resultFactory.success();
			}
			return resultFactory.error(json.getString("Message"));
		} catch (ServerException e) {
			logger.error(e, "send code to phone: {}", phone);
			return resultFactory.error("短信服务器错误，请稍后再试");
		} catch (ClientException e) {
			logger.error(e, "send code to phone: {}", phone);
			return resultFactory.error("系统错误");
		}
	}

	protected String nextCode() {
		return RandomUtils.getNumCode(6);
	}

	public abstract String createTemplateParam(String phone, String code);
}
