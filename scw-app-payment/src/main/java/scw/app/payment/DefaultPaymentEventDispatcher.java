package scw.app.payment;

import scw.event.support.DefaultBasicEventDispatcher;

public class DefaultPaymentEventDispatcher extends DefaultBasicEventDispatcher<PaymentEvent> {

	public DefaultPaymentEventDispatcher() {
		super(true);
	}
}
