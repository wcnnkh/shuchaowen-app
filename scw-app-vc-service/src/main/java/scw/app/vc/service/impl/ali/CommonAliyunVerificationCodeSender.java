package scw.app.vc.service.impl.ali;

import java.util.HashMap;
import java.util.Map;

import com.aliyuncs.IAcsClient;

import scw.json.JSONUtils;
import scw.result.ResultFactory;

/**
 * 通用模版的短信发送
 * 
 * @author shuchaowen
 *
 */
public class CommonAliyunVerificationCodeSender extends AliyunVerificationCodeSender {
	private Map<String, String> commonParams;

	/**
	 * 
	 * @param client
	 * @param resultFactory
	 * @param signName
	 *            签名名称
	 * @param templateCode
	 *            模版code
	 * @param commonParams
	 *            模块的通用参数
	 */
	public CommonAliyunVerificationCodeSender(IAcsClient client, ResultFactory resultFactory, String signName,
			String templateCode, Map<String, String> commonParams) {
		super(client, resultFactory, signName, templateCode);
		this.commonParams = commonParams;
	}

	@Override
	public String createTemplateParam(String phone, String code) {
		Map<String, String> params = new HashMap<String, String>(8);
		params.put("code", code);
		if (commonParams != null) {
			params.putAll(commonParams);
		}
		return JSONUtils.toJSONString(params);
	}

}
