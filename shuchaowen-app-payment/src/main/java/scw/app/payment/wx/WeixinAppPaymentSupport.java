package scw.app.payment.wx;

import scw.app.payment.PaymentChannelSupports;
import scw.app.payment.PaymentException;
import scw.app.payment.PaymentRequest;
import scw.app.payment.PaymentResponse;
import scw.app.payment.PaymentSupport;
import scw.app.payment.PaymentVoucher;
import scw.net.message.InputMessage;

public class WeixinAppPaymentSupport implements PaymentSupport {

	public boolean isSupport(String channel) {
		return PaymentChannelSupports.WX_APP.getName().equals(channel);
	}

	public PaymentVoucher request(PaymentRequest paymentRequest)
			throws PaymentException {
		// TODO Auto-generated method stub
		return null;
	}

	public PaymentResponse paymentCall(InputMessage inputMessage)
			throws PaymentException {
		// TODO Auto-generated method stub
		return null;
	}

}
