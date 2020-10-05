package scw.app.payment;

import scw.event.support.BasicEvent;

public class PaymentEvent extends BasicEvent {
	private final String orderId;
	private final PaymentStatus status;

	public PaymentEvent(String orderId, PaymentStatus status) {
		this.orderId = orderId;
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public PaymentStatus getStatus() {
		return status;
	}
}
