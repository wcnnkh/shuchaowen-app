package scw.app.payment.controller;

import scw.core.Assert;
import scw.core.utils.StringUtils;
import scw.net.InetUtils;

public class NotifyUrlControllerConfig {
	public static final String WEIXIN_PREFIX = "/payment/weixin";
	public static final String ALI_PREFIX = "/payment/ali";
	public static final String SUCCESS_CONTROLLER = "success";
	public static final String REFUND_CONTROLLER = "refund";

	private final String host;

	public NotifyUrlControllerConfig(String host) {
		Assert.requiredArgument(InetUtils.isUrl(host), "host");
		this.host = host;
	}

	public String getAliPaySuccessNotifyUrl() {
		return StringUtils.cleanPath(host + ALI_PREFIX + "/" + SUCCESS_CONTROLLER);
	}

	public String getWeiXinPaySuccessNotifyUrl() {
		return StringUtils.cleanPath(host + WEIXIN_PREFIX + "/" + SUCCESS_CONTROLLER);
	}
	
	public String getWeiXinRefundNotifyUrl(){
		return StringUtils.cleanPath(host + WEIXIN_PREFIX + "/" + REFUND_CONTROLLER); 
	}
}
