package scw.app.event;

import scw.core.instance.annotation.Configuration;
import scw.event.EventListener;
import scw.event.EventRegistration;
import scw.event.support.DefaultNamedEventDispatcher;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultAppEventDispatcher implements AppEventDispatcher {
	private DefaultNamedEventDispatcher<AppEvent<?>> dispatcher = new DefaultNamedEventDispatcher<AppEvent<?>>(true);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> EventRegistration registerListener(Class<T> type, EventListener<AppEvent<T>> eventListener) {
		return dispatcher.registerListener(type, (EventListener) eventListener);
	}

	public <T> void publishEvent(final Class<T> type, final AppEvent<T> event) {
		dispatcher.publishEvent(type, event);
	}

}
