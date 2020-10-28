package scw.app.payment.dao;

import scw.app.payment.pojo.ApplePayTransaction;

public interface ApplePayTransactionDao {
	ApplePayTransaction getById(String transactionId);

	ApplePayTransaction create(String transactionId, String orderId);
}
