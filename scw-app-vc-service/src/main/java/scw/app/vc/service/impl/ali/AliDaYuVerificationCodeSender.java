package scw.app.vc.service.impl.ali;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import scw.app.vc.service.impl.VerificationCodeSender;
import scw.core.utils.StringUtils;
import scw.core.utils.XTime;
import scw.http.HttpUtils;
import scw.http.MediaType;
import scw.json.JSONUtils;
import scw.logger.Logger;
import scw.logger.LoggerUtils;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.SignatureUtils;

/**
 * 推荐使用 {@see CommonAliyunVerificationCodeSender}
 * @author shuchaowen
 *
 */
public class AliDaYuVerificationCodeSender implements VerificationCodeSender {
	private static Logger logger = LoggerUtils.getLogger(AliDaYuVerificationCodeSender.class);
	private final String host;
	private final String appKey;
	private final String version;
	private final String format;
	private final String sign_method;
	private final String appSecret;
	private final String sms_free_sign_name;
	private final String sms_template_code;
	private final Map<String, String> commonParams;
	private final ResultFactory resultFactory;

	public AliDaYuVerificationCodeSender(String appKey, String appSecret, String sms_free_sign_name,
			String sms_template_code, Map<String, String> commonParams, ResultFactory resultFactory) {
		this("http://gw.api.taobao.com/router/rest", appKey, "2.0", "json", "md5", appSecret, sms_free_sign_name,
				sms_template_code, commonParams, resultFactory);
	}

	public AliDaYuVerificationCodeSender(String host, String appKey, String version, String format, String sign_method,
			String appSecret, String sms_free_sign_name, String sms_template_code, Map<String, String> commonParams,
			ResultFactory resultFactory) {
		this.host = host;
		this.appKey = appKey;
		this.version = version;
		this.format = format;
		this.sign_method = sign_method;
		this.appSecret = appSecret;
		this.sms_free_sign_name = sms_free_sign_name;
		this.sms_template_code = sms_template_code;
		this.commonParams = commonParams;
		this.resultFactory = resultFactory;
	}

	public Result send(String user, String code) {
		logger.info("向[{}]发送验证码:{}", user, code);
		Map<String, String> params = new HashMap<String, String>(8);
		if (commonParams != null) {
			params.putAll(commonParams);
		}
		params.put("code", code);

		Map<String, String> map = new HashMap<String, String>();
		map.put("app_key", appKey);
		map.put("v", version);
		map.put("format", format);
		map.put("sign_method", sign_method);

		map.put("timestamp", XTime.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
		map.put("sms_free_sign_name", sms_free_sign_name);
		map.put("sms_param", JSONUtils.toJSONString(params));
		map.put("sms_template_code", sms_template_code);
		map.put("method", "alibaba.aliqin.fc.sms.num.send");
		map.put("sms_type", "normal");
		map.put("rec_num", user);
		map.put("sign", getSign(map));
		String content = HttpUtils.getHttpClient().post(String.class, host, map, MediaType.APPLICATION_FORM_URLENCODED).getBody();
		logger.info(content);
		return resultFactory.success(content);
	}

	/**
	 * 签名
	 * 
	 * @param map
	 * @param secret
	 * @param sign_method
	 * @return
	 */
	protected String getSign(Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		StringBuilder sb = new StringBuilder();
		boolean isMd5 = false;
		if ("md5".equals(sign_method)) {
			sb.append(appSecret);
			isMd5 = true;
		}

		for (String key : keys) {
			String value = map.get(key);
			if (StringUtils.isNull(key, value)) {
				continue;
			}
			// sb.append(key).append(Http.encode(value, "utf-8"));
			sb.append(key).append(value);
		}

		String bytes = null;
		if (isMd5) {
			sb.append(appSecret);
			bytes = SignatureUtils.md5(sb.toString(), "UTF-8");
		} else {
			bytes = SignatureUtils.hmacMD5(sb.toString(), appSecret, "UTF-8");
		}

		return bytes.toUpperCase();
	}

}
