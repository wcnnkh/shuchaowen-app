package scw.app.payment;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import scw.app.payment.pojo.Order;
import scw.apple.pay.ApplePay;
import scw.tencent.wx.pay.WeiXinPay;

public class SimplePaymentConfig extends PaymentConfig {
	private AlipayConfig alipayConfig;
	private AlipayClient alipayClient;
	private WeiXinPay weiXinPay;
	private ApplePay applePay;

	public SimplePaymentConfig(String host, AlipayConfig alipayConfig, WeiXinPay weiXinPay) {
		this(host, alipayConfig, weiXinPay, new ApplePay());
	}

	public SimplePaymentConfig(String host, AlipayConfig alipayConfig, WeiXinPay weiXinPay, ApplePay applePay) {
		super(host);
		this.alipayConfig = alipayConfig;
		this.alipayClient = new DefaultAlipayClient("https://openapi.alipay.com", alipayConfig.getAppId(),
				alipayConfig.getPrivateKey(), alipayConfig.getDataType(), alipayConfig.getCharset(),
				alipayConfig.getPublicKey(), alipayConfig.getSignType());
		this.weiXinPay = weiXinPay;
		this.applePay = applePay;
	}

	public AlipayClient getAlipayClient(Order order) {
		return alipayClient;
	}

	public WeiXinPay getWeiXinPay(Order order) {
		return weiXinPay;
	}

	public AlipayConfig getAlipayConfig(Order order) {
		return alipayConfig;
	}

	@Override
	public ApplePay getApplePay(Order order) {
		return applePay;
	}

}
