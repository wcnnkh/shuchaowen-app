package scw.app.logistics.event;

import scw.core.instance.annotation.Configuration;
import scw.event.support.DefaultBasicEventDispatcher;

@Configuration(order = Integer.MIN_VALUE, value = LogisticsEventDispatcher.class)
public class DefaultLogisticsEventDispatcher extends DefaultBasicEventDispatcher<LogisticsEvent>
		implements LogisticsEventDispatcher {

	public DefaultLogisticsEventDispatcher() {
		super(true);
	}
}
