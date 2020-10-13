package scw.app.payment.service;

import scw.app.payment.model.PaymentRequest;
import scw.app.payment.model.PaymentResponse;
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
	DataResult<PaymentResponse> payment(PaymentRequest request);
	
	/**
	 * 关闭交易
	 * @param orderId
	 * @return
	 */
	Result close(String orderId);

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
