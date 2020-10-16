package scw.app.payment.dao;

import scw.app.payment.pojo.ApplePayOrderInfo;

public interface ApplePayOrderInfoDao {
	ApplePayOrderInfo getById(String transactionId);

	ApplePayOrderInfo create(String transactionId, String orderId);
}
