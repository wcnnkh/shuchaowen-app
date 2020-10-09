package scw.app.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import scw.app.payment.AlipayConfig;
import scw.app.payment.controller.NotifyUrlControllerConfig;
import scw.app.payment.enums.PaymentMethod;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.BasePaymentInfo;
import scw.app.payment.model.PaymentRequest;
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
import scw.tencent.wx.WeiXinPay;
import scw.tencent.wx.WeiXinPayResponse;

public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired(required = false)
	private WeiXinPay weiXinPay;
	@Autowired(required = false)
	private AlipayConfig alipayConfig;
	private NotifyUrlControllerConfig notifyUrlControllerConfig;
	@Autowired
	private RefundOrderService refundOrderService;

	public PaymentServiceImpl(NotifyUrlControllerConfig notifyUrlControllerConfig) {
		this.notifyUrlControllerConfig = notifyUrlControllerConfig;
	}

	public DataResult<Object> payment(PaymentRequest request) {
		DataResult<Order> orderResult = orderService.create(request);
		if (orderResult.isError()) {
			return orderResult.dataResult();
		}

		Object result = payment(orderResult.getData().getId(), request.getPayChannel(), orderResult.getData());
		return resultFactory.success(result);
	}

	public Object payment(String orderId, int paymentChannel, BasePaymentInfo paymentRequest) {
		if (paymentRequest.getPrice() == 0) {
			// 不要钱的
			return null;
		} else {// 要钱的
			if (paymentChannel == PaymentMethod.ALI_APP.getChannel()) {
				AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com",
						alipayConfig.getAppId(), alipayConfig.getPrivateKey(), alipayConfig.getType(),
						alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSignType());
				AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
				AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
				model.setBody(paymentRequest.getName());
				model.setSubject(paymentRequest.getDetail());
				model.setOutTradeNo(orderId);
				model.setTimeoutExpress("30m");
				model.setTotalAmount(((float) paymentRequest.getPrice() / 100) + "");
				model.setProductCode("QUICK_MSECURITY_PAY");
				request.setBizModel(model);
				request.setNotifyUrl(notifyUrlControllerConfig.getAliPaySuccessNotifyUrl());
				try {
					AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
					return response.getBody();
				} catch (AlipayApiException e) {
					throw new RuntimeException(e);
				}
			} else if (paymentChannel == PaymentMethod.WX_APP.getChannel()) {
				return weiXinPay.getSimpleUnifiedorder("APP", paymentRequest.getName(), orderId,
						paymentRequest.getPrice(), paymentRequest.getIp(),
						notifyUrlControllerConfig.getWeiXinPaySuccessNotifyUrl());
			} else if (paymentChannel == PaymentMethod.WX_WEB.getChannel()) {
				return weiXinPay.getDefaultUnifiedorder("JSAPI", paymentRequest.getName(), orderId, "CNY",
						paymentRequest.getPrice(), paymentRequest.getIp(), null, null, null,
						paymentRequest.getWxOpenid(), notifyUrlControllerConfig.getWeiXinPaySuccessNotifyUrl());
			}
			throw new NotSupportedException("不支持的支付方式");
		}
	}

	public DataResult<Object> repayments(String orderId, PaymentMethod paymentMethod) {
		return null;
	}

	public Result cancel(String orderId) {
		// TODO Auto-generated method stub
		return null;
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
			refundRequest.setNotify_url(notifyUrlControllerConfig.getWeiXinRefundNotifyUrl());
			refundRequest.setOut_trade_no(order.getId());
			refundRequest.setOut_refund_no(refundOrder.getId());
			refundRequest.setRefund_desc(refundOrder.getRefundDesc());
			refundRequest.setRefund_fee(refundOrder.getPrice());
			refundRequest.setTotal_fee(order.getPrice());
			WeiXinPayResponse response = weiXinPay.refund(refundRequest);
			if (response.isSuccess()) {
				return resultFactory.error("申请退款异常(" + response.getReturnMsg() + ")");
			}
		} else if (order.getPayChannel() == PaymentMethod.ALI_APP.getChannel()) {
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com", alipayConfig.getAppId(),
					alipayConfig.getPrivateKey(), alipayConfig.getType(), alipayConfig.getCharset(),
					alipayConfig.getPublicKey(), alipayConfig.getSignType());
			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(order.getId());
			model.setRefundAmount(((float) refundOrder.getPrice() / 100) + "");
			model.setRefundReason(refundOrder.getRefundDesc());
			model.setOutRequestNo(refundOrder.getId());
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			request.setBizModel(model);
			request.setNotifyUrl(notifyUrlControllerConfig.getAliPaySuccessNotifyUrl());
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
