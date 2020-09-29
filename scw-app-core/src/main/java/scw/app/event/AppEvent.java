package scw.app.event;

import scw.event.ObjectEvent;
import scw.event.support.EventType;

public class AppEvent<T> extends ObjectEvent<T> {
	private static final long serialVersionUID = 1L;
	private final EventType eventType;

	public AppEvent(T source, EventType eventType) {
		super(source);
		this.eventType = eventType;
	}

	public EventType getEventType() {
		return eventType;
	}
}
