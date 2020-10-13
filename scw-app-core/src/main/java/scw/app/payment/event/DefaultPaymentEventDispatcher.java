package scw.app.payment.event;

import scw.core.instance.annotation.Configuration;
import scw.event.support.DefaultBasicEventDispatcher;

@Configuration(order = Integer.MIN_VALUE, value = PaymentEventDispatcher.class)
public class DefaultPaymentEventDispatcher extends DefaultBasicEventDispatcher<PaymentEvent>
		implements PaymentEventDispatcher {

	public DefaultPaymentEventDispatcher() {
		super(true);
	}
}