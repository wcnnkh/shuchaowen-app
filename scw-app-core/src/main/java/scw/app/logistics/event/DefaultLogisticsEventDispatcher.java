package scw.app.logistics.event;

import scw.beans.annotation.Service;
import scw.event.support.DefaultEventDispatcher;

@Service
public class DefaultLogisticsEventDispatcher extends DefaultEventDispatcher<LogisticsEvent>
		implements LogisticsEventDispatcher {

	public DefaultLogisticsEventDispatcher() {
		super(true);
	}
}
