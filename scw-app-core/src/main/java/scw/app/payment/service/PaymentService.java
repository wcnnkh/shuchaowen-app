package scw.app.payment.service;

import scw.app.payment.enums.PaymentMethod;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.model.RefundRequest;
import scw.app.payment.pojo.RefundOrder;
import scw.result.DataResult;
import scw.result.Result;

public interface PaymentService {
	/**
	 * 发起支付
	 * 
	 * @param request
	 * @return
	 */
	DataResult<Object> payment(PaymentRequest request);

	/**
	 * 重新支付
	 * 
	 * @param orderId
	 * @param paymentMethod
	 * @return
	 */
	DataResult<Object> repayments(String orderId, PaymentMethod paymentMethod);

	/**
	 * 取消支付
	 * 
	 * @param orderId
	 * @return
	 */
	Result cancel(String orderId);

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
