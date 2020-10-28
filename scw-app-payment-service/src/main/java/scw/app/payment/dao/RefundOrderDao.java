package scw.app.payment.dao;

import java.util.List;

import scw.app.payment.model.RefundRequest;
import scw.app.payment.pojo.RefundOrder;

public interface RefundOrderDao {
	RefundOrder getById(String refundOrderId);

	RefundOrder create(RefundRequest request);

	List<RefundOrder> getRefundOrderList(String orderId);
}
