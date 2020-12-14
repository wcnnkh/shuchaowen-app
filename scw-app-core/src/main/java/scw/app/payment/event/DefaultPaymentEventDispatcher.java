package scw.app.payment.event;

import scw.beans.annotation.Service;
import scw.event.support.DefaultBasicEventDispatcher;

@Service
public class DefaultPaymentEventDispatcher extends DefaultBasicEventDispatcher<PaymentEvent>
		implements PaymentEventDispatcher {

	public DefaultPaymentEventDispatcher() {
		super(true);
	}
}