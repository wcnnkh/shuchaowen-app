package scw.app.payment.model;

import java.io.Serializable;

public class RefundRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String refundDesc;
	private Integer price;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
}
