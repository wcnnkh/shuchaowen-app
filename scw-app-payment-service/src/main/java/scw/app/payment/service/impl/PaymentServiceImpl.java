package scw.app.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import scw.app.payment.PaymentConfig;
import scw.app.payment.enums.PaymentMethod;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.model.PaymentResponse;
import scw.app.payment.pojo.Order;
import scw.app.payment.pojo.RefundOrder;
import scw.app.payment.service.OrderService;
import scw.app.payment.service.PaymentService;
import scw.app.payment.service.RefundOrderService;
import scw.beans.annotation.Autowired;
import scw.lang.NotSupportedException;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.tencent.wx.RefundRequest;
import scw.tencent.wx.UnifiedorderRequest;
import scw.tencent.wx.WeiXinPay;
import scw.tencent.wx.WeiXinPayResponse;

public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ResultFactory resultFactory;
	private PaymentConfig paymentConfig;
	@Autowired
	private RefundOrderService refundOrderService;

	public PaymentServiceImpl(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
	}

	public DataResult<PaymentResponse> payment(PaymentRequest request) {
		DataResult<Order> orderResult = orderService.create(request);
		if (orderResult.isError()) {
			return orderResult.dataResult();
		}

		PaymentResponse response = new PaymentResponse();
		response.setOrder(orderResult.getData());
		Object result = payment(orderResult.getData());
		response.setCredential(result);
		return resultFactory.success(response);
	}

	public Object payment(Order order) {
		if (order.getPrice() == 0) {
			// 不要钱的
			return null;
		} else {// 要钱的
			if (order.getPaymentMethod() == PaymentMethod.ALI_APP) {
				AlipayClient alipayClient = paymentConfig.getAlipayClient(order);
				AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
				AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
				model.setSubject(order.getName());
				model.setOutTradeNo(order.getId());
				model.setTimeoutExpress("30m");
				model.setTotalAmount(order.getPriceDescribe());
				model.setProductCode("QUICK_MSECURITY_PAY");
				request.setBizModel(model);
				request.setNotifyUrl(paymentConfig.getAliPaySuccessNotifyUrl());
				try {
					AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
					return response.getBody();
				} catch (AlipayApiException e) {
					throw new RuntimeException(e);
				}
			} else if (order.getPaymentMethod() == PaymentMethod.WX_APP) {
				WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);
				return weiXinPay.getUnifiedorder(new UnifiedorderRequest(order.getName(), order.getId(),
						order.getPrice(), order.getIp(), paymentConfig.getWeiXinPaySuccessNotifyUrl(), "APP"));
			} else if (order.getPaymentMethod() == PaymentMethod.WX_WEB) {
				WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);

				UnifiedorderRequest unifiedorderRequest = new UnifiedorderRequest(order.getName(), order.getId(),
						order.getPrice(), order.getIp(), paymentConfig.getWeiXinPaySuccessNotifyUrl(), "JSAPI");
				unifiedorderRequest.setOpenid(order.getWxOpenid());
				return weiXinPay.getUnifiedorder(unifiedorderRequest);
			}
			throw new NotSupportedException("不支持的支付方式");
		}
	}

	public Result close(String orderId) {
		Order order = orderService.getById(orderId);
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		Result result = orderService.updateStatus(orderId, PaymentStatus.CANCEL);
		if (result.isError()) {
			return result.dataResult();
		}

		return closeorder(order);
	}

	public Result closeorder(Order order) {
		if (order.getPayChannel() == PaymentMethod.WX_APP.getChannel()
				|| order.getPayChannel() == PaymentMethod.WX_WEB.getChannel()) {
			WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);
			WeiXinPayResponse weiXinPayResponse = weiXinPay.closeorder(order.getId());
			if (!weiXinPayResponse.isSuccess()) {
				return resultFactory.error(weiXinPayResponse.getReturnMsg());
			}

			return resultFactory.success();
		} else if (order.getPayChannel() == PaymentMethod.ALI_APP.getChannel()) {
			AlipayClient alipayClient = paymentConfig.getAlipayClient(order);

			AlipayTradeCloseModel model = new AlipayTradeCloseModel();
			model.setOutTradeNo(order.getId());
			AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
			request.setBizModel(model);
			try {
				AlipayTradeCloseResponse response = alipayClient.sdkExecute(request);
				if (!response.isSuccess()) {
					return resultFactory.error(response.getMsg());
				}
			} catch (AlipayApiException e) {
				throw new RuntimeException(e);
			}
		}
		return resultFactory.success();
	}

	public DataResult<RefundOrder> refund(scw.app.payment.model.RefundRequest request) {
		Order order = orderService.getById(request.getOrderId());
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		DataResult<RefundOrder> refundResult = refundOrderService.create(request);
		if (refundResult.isError()) {
			return refundResult;
		}

		return refund(order, refundResult.getData());
	}

	public DataResult<RefundOrder> refund(Order order, RefundOrder refundOrder) {
		if (refundOrder.getPrice() == 0) {
			Result result = orderService.updateStatus(refundOrder.getOrderId(), PaymentStatus.REFUND);
			if (result.isError()) {
				return result.dataResult();
			}

			return result.dataResult(refundOrder);
		}

		if (order.getPayChannel() == PaymentMethod.WX_APP.getChannel()
				|| order.getPayChannel() == PaymentMethod.WX_WEB.getChannel()) {
			RefundRequest refundRequest = new RefundRequest();
			refundRequest.setNotify_url(paymentConfig.getWeiXinRefundNotifyUrl());
			refundRequest.setOut_trade_no(order.getId());
			refundRequest.setOut_refund_no(refundOrder.getId());
			refundRequest.setRefund_desc(refundOrder.getRefundDesc());
			refundRequest.setRefund_fee(refundOrder.getPrice());
			refundRequest.setTotal_fee(order.getPrice());

			WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);
			WeiXinPayResponse response = weiXinPay.refund(refundRequest);
			if (response.isSuccess()) {
				return resultFactory.error("申请退款异常(" + response.getReturnMsg() + ")");
			}
		} else if (order.getPayChannel() == PaymentMethod.ALI_APP.getChannel()) {
			AlipayClient alipayClient = paymentConfig.getAlipayClient(order);
			Result result = orderService.updateStatus(refundOrder.getOrderId(), PaymentStatus.REFUND);
			if (result.isError()) {
				return result.dataResult();
			}

			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(order.getId());
			model.setRefundAmount(refundOrder.getPriceDescribe());
			model.setRefundReason(refundOrder.getRefundDesc());
			model.setOutRequestNo(refundOrder.getId());
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			request.setBizModel(model);
			request.setNotifyUrl(paymentConfig.getAliPaySuccessNotifyUrl());
			try {
				AlipayTradeRefundResponse response = alipayClient.sdkExecute(request);
				if (!response.isSuccess()) {
					return resultFactory.error("退款失败");
				}
			} catch (AlipayApiException e) {
				throw new RuntimeException(e);
			}
		} else {
			return resultFactory.error("暂不支持退款");
		}
		return resultFactory.success(refundOrder);
	}

	public Result refundAgain(String refundId) {
		RefundOrder refundOrder = refundOrderService.getById(refundId);
		if (refundOrder == null) {
			return resultFactory.error("退款订单不存在");
		}

		Order order = orderService.getById(refundOrder.getOrderId());
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		return refund(order, refundOrder);
	}

}
