package scw.app.payment;

import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.support.DefaultBasicEventDispatcher;

/**
 * 默认的支付状态分发
 * @author shuchaowen
 *
 */
@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class DefaultPaymentStatusDispatcher extends
		DefaultBasicEventDispatcher<PaymentStatusEvent> implements
		PaymentStatusDispatcher {

	public DefaultPaymentStatusDispatcher() {
		super(true);
	}
}
