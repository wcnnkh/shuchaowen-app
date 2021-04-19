package scw.app.payment.dto;

import scw.lang.Nullable;

/**
 * 退款请求
 * @author shuchaowen
 *
 */
public class RefundRequest extends PaymentRequest {
	private static final long serialVersionUID = 1L;
	/**
	 * 原订单总金额
	 */
	private int totalPrice;
	
	@Nullable
	private String platformOrderId;

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPlatformOrderId() {
		return platformOrderId;
	}

	public void setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
	}
}
