package scw.app.logistics.event;

import java.io.Serializable;

import scw.app.logistics.enums.LogisticsStatus;
import scw.event.support.BasicEvent;

public class LogisticsEvent extends BasicEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String orderId;
	private final LogisticsStatus status;

	public LogisticsEvent(String orderId, LogisticsStatus status) {
		this.orderId = orderId;
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public LogisticsStatus getStatus() {
		return status;
	}
}
