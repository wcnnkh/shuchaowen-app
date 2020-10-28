package scw.app.payment.dao.impl;

import scw.app.payment.dao.ApplePayTransactionDao;
import scw.app.payment.pojo.ApplePayTransaction;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;

@Configuration(order = Integer.MIN_VALUE)
public class ApplePayTransactionDaoImpl implements ApplePayTransactionDao {
	private DB db;

	public ApplePayTransactionDaoImpl(DB db) {
		this.db = db;
		db.createTable(ApplePayTransaction.class, false);
	}

	public ApplePayTransaction getById(String transactionId) {
		return db.getById(ApplePayTransaction.class, transactionId);
	}

	public ApplePayTransaction create(String transactionId, String orderId) {
		ApplePayTransaction applePayTransaction = new ApplePayTransaction();
		applePayTransaction.setTransactionId(transactionId);
		applePayTransaction.setOrderId(orderId);
		db.save(applePayTransaction);
		return applePayTransaction;
	}

}
