package scw.app.payment;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import scw.app.payment.pojo.Order;
import scw.tencent.wx.WeiXinPay;

public class SimplePaymentConfig extends PaymentConfig {
	private AlipayConfig alipayConfig;
	private AlipayClient alipayClient;
	private WeiXinPay weiXinPay;

	public SimplePaymentConfig(String host, AlipayConfig alipayConfig, WeiXinPay weiXinPay) {
		super(host);
		this.alipayConfig = alipayConfig;
		this.alipayClient = new DefaultAlipayClient("https://openapi.alipay.com", alipayConfig.getAppId(),
				alipayConfig.getPrivateKey(), alipayConfig.getDataType(), alipayConfig.getCharset(),
				alipayConfig.getPublicKey(), alipayConfig.getSignType());
		this.weiXinPay = weiXinPay;
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

}
