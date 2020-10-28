package scw.app.event;

import scw.aop.annotation.AopEnable;
import scw.event.EventListener;
import scw.event.EventRegistration;

@AopEnable(false)
public interface AppEventDispatcher {
	<T> EventRegistration registerListener(Class<T> type, EventListener<AppEvent<T>> eventListener);

	<T> void publishEvent(Class<T> type, AppEvent<T> event);
}
