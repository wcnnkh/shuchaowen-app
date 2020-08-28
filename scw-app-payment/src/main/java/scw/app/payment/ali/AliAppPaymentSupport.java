package scw.app.payment.ali;

import scw.app.payment.PaymentChannelSupports;
import scw.app.payment.PaymentException;
import scw.app.payment.PaymentRequest;
import scw.app.payment.PaymentResponse;
import scw.app.payment.PaymentSupport;
import scw.lang.NestedRuntimeException;
import scw.net.message.InputMessage;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

public class AliAppPaymentSupport implements PaymentSupport {
	private final AlipayClient alipayClient;
	private final String notifyUrl;

	public AliAppPaymentSupport(AlipayClient alipayClient, String notifyUrl) {
		this.alipayClient = alipayClient;
		this.notifyUrl = notifyUrl;
	}

	public boolean isSupport(String channel) {
		return PaymentChannelSupports.ALI_APP.getName().equals(channel);
	}

	public AliAppPaymentVoucher request(PaymentRequest paymentRequest)
			throws PaymentException {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(paymentRequest.getBody());
		model.setSubject(paymentRequest.getSubject());
		model.setOutTradeNo(paymentRequest.getOrderNo());
		model.setTotalAmount(((double) paymentRequest.getAmount() / 100) + "");
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);

		AliAppPaymentVoucher voucher = new AliAppPaymentVoucher();
		try {
			AlipayTradeAppPayResponse response = alipayClient
					.sdkExecute(request);
			voucher.setAlipayTradeAppPayResponse(response);
			voucher.setPaymentRequest(paymentRequest);
			return voucher;
		} catch (AlipayApiException e) {
			throw new NestedRuntimeException(e);
		}
	}

	public PaymentResponse paymentCall(InputMessage inputMessage)
			throws PaymentException {
		// TODO Auto-generated method stub
		return null;
	}

}
