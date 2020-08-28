package scw.app.payment;

import java.io.Serializable;

public class PaymentResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private PaymentStatus paymentStatus;

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}
