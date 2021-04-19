package scw.app.payment;

import scw.app.payment.service.PaymentService;
import scw.lang.NotSupportedException;
import scw.mvc.HttpChannel;

public interface PaymentServiceAdapter extends PaymentService {
	boolean isAccept(String paymentMethod);

	PaymentStatusEvent callback(String paymentMethod, String paymentStatus,
			HttpChannel httpChannel) throws NotSupportedException;
}
