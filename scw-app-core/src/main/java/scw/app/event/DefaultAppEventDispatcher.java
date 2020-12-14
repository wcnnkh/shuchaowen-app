package scw.app.event;

import scw.beans.annotation.Service;
import scw.event.EventListener;
import scw.event.EventRegistration;
import scw.event.support.DefaultNamedEventDispatcher;

@Service
public class DefaultAppEventDispatcher implements AppEventDispatcher {
	@SuppressWarnings("rawtypes")
	private DefaultNamedEventDispatcher<Class, AppEvent<?>> dispatcher = new DefaultNamedEventDispatcher<Class, AppEvent<?>>(
			true);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> EventRegistration registerListener(Class<T> type, EventListener<AppEvent<T>> eventListener) {
		return dispatcher.registerListener(type, (EventListener) eventListener);
	}

	public <T> void publishEvent(final Class<T> type, final AppEvent<T> event) {
		dispatcher.publishEvent(type, event);
	}

}
