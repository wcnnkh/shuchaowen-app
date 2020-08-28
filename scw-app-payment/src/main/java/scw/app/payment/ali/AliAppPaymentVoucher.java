package scw.app.payment.ali;

import scw.app.payment.PaymentVoucher;

import com.alipay.api.response.AlipayTradeAppPayResponse;

public class AliAppPaymentVoucher extends PaymentVoucher{
	private static final long serialVersionUID = 1L;
	private AlipayTradeAppPayResponse alipayTradeAppPayResponse;
	public AlipayTradeAppPayResponse getAlipayTradeAppPayResponse() {
		return alipayTradeAppPayResponse;
	}
	public void setAlipayTradeAppPayResponse(
			AlipayTradeAppPayResponse alipayTradeAppPayResponse) {
		this.alipayTradeAppPayResponse = alipayTradeAppPayResponse;
	}
}
