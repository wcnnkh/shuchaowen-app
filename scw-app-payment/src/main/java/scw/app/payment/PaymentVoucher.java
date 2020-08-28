package scw.app.payment;

import java.io.Serializable;

/**
 * 支付凭据
 * @author shuchaowen
 *
 */
public class PaymentVoucher implements Serializable{
	private static final long serialVersionUID = 1L;
	private PaymentRequest paymentRequest;
	
	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}
	public void setPaymentRequest(PaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}
}
