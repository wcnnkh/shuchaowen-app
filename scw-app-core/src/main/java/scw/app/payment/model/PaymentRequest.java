package scw.app.payment.model;

public class PaymentRequest extends BasePaymentInfo {
	private static final long serialVersionUID = 1L;
	private int accumulatedPoints;// 使用积分
	private int voucterId;

	public int getAccumulatedPoints() {
		return accumulatedPoints;
	}

	public void setAccumulatedPoints(int accumulatedPoints) {
		this.accumulatedPoints = accumulatedPoints;
	}

	public int getVoucterId() {
		return voucterId;
	}

	public void setVoucterId(int voucterId) {
		this.voucterId = voucterId;
	}
}
