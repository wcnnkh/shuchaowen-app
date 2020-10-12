package scw.app.logistics.service;

import scw.app.logistics.model.LogisticsConfig;

public interface LogisticsService {
	LogisticsConfig getLogisticsConfig(String orderId);
}
