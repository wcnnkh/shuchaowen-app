package scw.app.payment.enums;

public enum PaymentStatus {
	CREATED(0, "未支付"), SUCCESS(1, "已支付"), REFUND(2, "已退款"), CANCEL(3, "已取消");

	private final int status;
	private final String describe;

	private PaymentStatus(int status, String describe) {
		this.status = status;
		this.describe = describe;
	}

	public int getStatus() {
		return status;
	}

	public String getDescribe() {
		return describe;
	}

	/**
	 * @see PaymentStatus#isSwitchTo(int)
	 * @param status
	 * @return
	 */
	public boolean isSwitchTo(PaymentStatus status) {
		return isSwitchTo(status.getStatus());
	}

	/**
	 * 是否可以变更为此状态
	 * @param status
	 * @return
	 */
	public boolean isSwitchTo(int status) {
		if (status == this.status) {
			return status == REFUND.status;
		}

		if (status < this.status) {
			return false;
		}

		return true;
	}

	public static PaymentStatus forStatus(int status) {
		for (PaymentStatus paymentStatus : values()) {
			if (paymentStatus.status == status) {
				return paymentStatus;
			}
		}
		return null;
	}
}
