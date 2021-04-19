package scw.app.payment.dto;

public class PaymentResponse extends UnifiedPaymentRequest {
	private static final long serialVersionUID = 1L;
	/**
	 * 第三方支付凭据
	 */
	private Object credential;
	
	public Object getCredential() {
		return credential;
	}
	public void setCredential(Object credential) {
		this.credential = credential;
	}
}
