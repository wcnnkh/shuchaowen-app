package scw.app.payment.service;

import java.util.List;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.event.PaymentEvent;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.model.PaymentResponse;
import scw.app.payment.model.RefundRequest;
import scw.app.payment.pojo.Order;
import scw.app.payment.pojo.RefundOrder;
import scw.result.DataResult;
import scw.result.Result;
import scw.util.Pagination;

public interface PaymentService {
	Order getOrder(String orderId);

	Pagination<Order> search(String query, int page, int limit, PaymentStatus paymentStatus,
			LogisticsStatus logisticsStatus);

	Result publish(PaymentEvent paymentEvent);

	/**
	 * 发起支付
	 * 
	 * @param request
	 * @return
	 */
	DataResult<PaymentResponse> payment(PaymentRequest request);

	/**
	 * 关闭交易
	 * 
	 * @param orderId
	 * @return
	 */
	Result close(String orderId);

	RefundOrder getRefundOrder(String refundOrderId);

	List<RefundOrder> getRefundOrderList(String orderId);

	/**
	 * 退款
	 * 
	 * @param orderId
	 * @return
	 */
	DataResult<RefundOrder> refund(RefundRequest request);

	/**
	 * 重新退款
	 * 
	 * @param refundId
	 * @return
	 */
	Result refundAgain(String refundId);
}
