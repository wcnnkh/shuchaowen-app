package scw.app.payment.service;

import scw.app.payment.dto.PaymentResponse;
import scw.app.payment.dto.RefundRequest;
import scw.app.payment.dto.UnifiedPaymentRequest;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.lang.NotSupportedException;

/**
 * 支付服务
 * 
 * @author shuchaowen
 *
 */
public interface PaymentService {
	/**
	 * 发起支付
	 * 
	 * @param request
	 * @return
	 */
	DataResult<PaymentResponse> payment(UnifiedPaymentRequest request) throws NotSupportedException;

	/**
	 * 退款
	 * 
	 * @param request
	 * @return
	 */
	Result refund(RefundRequest request) throws NotSupportedException;
}
