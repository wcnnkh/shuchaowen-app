package scw.app.payment;

public class PaymentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PaymentException(String message) {
		super(message);
	}

	public PaymentException(Throwable e) {
		super(e);
	}

	public PaymentException(String message, Throwable e) {
		super(message, e);
	}
}
