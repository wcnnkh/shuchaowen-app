package scw.app.payment.dao.impl;

import scw.app.payment.dao.ApplePayOrderInfoDao;
import scw.app.payment.pojo.ApplePayOrderInfo;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;

@Configuration(order = Integer.MIN_VALUE)
public class ApplePayOrderInfoDaoImpl implements ApplePayOrderInfoDao {
	private DB db;

	public ApplePayOrderInfoDaoImpl(DB db) {
		this.db = db;
		db.createTable(ApplePayOrderInfo.class, false);
	}

	public ApplePayOrderInfo getById(String transactionId) {
		return db.getById(ApplePayOrderInfo.class, transactionId);
	}

	public ApplePayOrderInfo create(String transactionId, String orderId) {
		ApplePayOrderInfo applePayOrderInfo = new ApplePayOrderInfo();
		applePayOrderInfo.setTransactionId(transactionId);
		applePayOrderInfo.setOrderId(orderId);
		db.save(applePayOrderInfo);
		return applePayOrderInfo;
	}

}
