package scw.app.payment.service;

import scw.app.payment.PaymentStatus;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.pojo.Order;
import scw.result.DataResult;
import scw.result.Result;

public interface OrderService {
	Order getById(String orderId);
	
	DataResult<Order> create(PaymentRequest request);
	
	Result updateStatus(String orderId, PaymentStatus status);
}
