package scw.app.payment;

import scw.app.payment.pojo.Order;
import scw.apple.pay.ApplePay;
import scw.core.Assert;
import scw.core.utils.StringUtils;
import scw.net.uri.UriUtils;
import scw.tencent.wx.pay.WeiXinPay;

import com.alipay.api.AlipayClient;

public abstract class PaymentConfig {
	public static final String WEIXIN_PREFIX = "/payment/weixin";
	public static final String ALI_PREFIX = "/payment/ali";
	public static final String SUCCESS_CONTROLLER = "success";
	public static final String REFUND_CONTROLLER = "refund";

	private final String host;

	public PaymentConfig(String host) {
		Assert.requiredArgument(UriUtils.isUri(host), "host");
		this.host = host;
	}

	public String getAliPaySuccessNotifyUrl() {
		return StringUtils.cleanPath(host + ALI_PREFIX + "/" + SUCCESS_CONTROLLER);
	}

	public String getWeiXinPaySuccessNotifyUrl() {
		return StringUtils.cleanPath(host + WEIXIN_PREFIX + "/" + SUCCESS_CONTROLLER);
	}

	public String getWeiXinRefundNotifyUrl() {
		return StringUtils.cleanPath(host + WEIXIN_PREFIX + "/" + REFUND_CONTROLLER);
	}

	public abstract AlipayClient getAlipayClient(Order order);

	public abstract WeiXinPay getWeiXinPay(Order order);

	public abstract AlipayConfig getAlipayConfig(Order order);
	
	public abstract ApplePay getApplePay(Order order);
}
