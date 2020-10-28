package scw.app.payment.event;

import scw.app.payment.enums.PaymentStatus;
import scw.event.support.BasicEvent;

public class PaymentEvent extends BasicEvent {
	private final String orderId;
	private final PaymentStatus status;
	private String refundOrderId;// 退款时才存在

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

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
}
