package scw.app.payment;

import scw.net.message.InputMessage;

public interface PaymentSupport {
	boolean isSupport(String channel);

	/**
	 * 发起一个支付请求
	 * 
	 * @param paymentRequest
	 * @return
	 * @throws PaymentException
	 */
	PaymentVoucher request(PaymentRequest paymentRequest)
			throws PaymentException;

	/**
	 * 支付回调
	 * 
	 * @param inputMessage
	 * @return
	 * @throws PaymentException
	 */
	PaymentResponse paymentCall(InputMessage inputMessage)
			throws PaymentException;
}
