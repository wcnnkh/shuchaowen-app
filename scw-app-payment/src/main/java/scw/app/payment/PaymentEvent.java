package scw.app.payment;

import scw.event.support.BasicEvent;

public class PaymentEvent extends BasicEvent {
	private final String orderId;
	private final PaymentMethod method;
	private final PaymentStatus status;

	public PaymentEvent(String orderId, PaymentMethod method, PaymentStatus status) {
		this.orderId = orderId;
		this.method = method;
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public PaymentStatus getStatus() {
		return status;
	}
}
