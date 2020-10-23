package scw.app.payment.dao;

import java.util.List;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.pojo.Order;
import scw.util.Pagination;

public interface OrderDao {
	Order getById(String orderId);

	Order create(PaymentRequest request);

	boolean updateStatus(String orderId, PaymentStatus status);

	boolean updateApplePayProductId(String orderId, List<String> productIds);

	Pagination<Order> search(String query, int page, int limit, PaymentStatus paymentStatus,
			LogisticsStatus logisticsStatus);
}
