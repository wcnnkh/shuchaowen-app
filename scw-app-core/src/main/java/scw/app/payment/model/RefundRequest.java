package scw.app.payment.model;

import java.io.Serializable;

import scw.core.utils.StringUtils;

public class RefundRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String refundDesc;
	private int price;

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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPriceDescribe() {
		return StringUtils.formatNothingToYuan(price);
	}
}
