package scw.app.logistics.event;

import scw.beans.annotation.Service;
import scw.event.support.DefaultBasicEventDispatcher;

@Service
public class DefaultLogisticsEventDispatcher extends DefaultBasicEventDispatcher<LogisticsEvent>
		implements LogisticsEventDispatcher {

	public DefaultLogisticsEventDispatcher() {
		super(true);
	}
}
