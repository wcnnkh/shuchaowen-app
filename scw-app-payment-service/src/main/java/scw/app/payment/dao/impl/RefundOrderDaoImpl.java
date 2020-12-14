package scw.app.payment.dao.impl;

import java.util.List;

import scw.app.payment.dao.RefundOrderDao;
import scw.app.payment.model.RefundRequest;
import scw.app.payment.pojo.RefundOrder;
import scw.beans.annotation.Service;
import scw.db.DB;
import scw.mapper.Copy;
import scw.sql.SimpleSql;

@Service
public class RefundOrderDaoImpl implements RefundOrderDao {
	private DB db;

	public RefundOrderDaoImpl(DB db) {
		this.db = db;
		db.createTable(RefundOrder.class, false);
	}

	public RefundOrder getById(String refundOrderId) {
		return db.getById(RefundOrder.class, refundOrderId);
	}

	public RefundOrder create(RefundRequest request) {
		RefundOrder refundOrder = new RefundOrder();
		Copy.copy(refundOrder, request);
		db.save(refundOrder);
		return refundOrder;
	}

	public List<RefundOrder> getRefundOrderList(String orderId) {
		return db.select(RefundOrder.class, new SimpleSql("select * from refund_order where orderId=?", orderId));
	}

}
