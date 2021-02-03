package scw.app.payment.service.impl;

import java.util.ArrayList;
import java.util.List;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.PaymentConfig;
import scw.app.payment.dao.ApplePayTransactionDao;
import scw.app.payment.dao.OrderDao;
import scw.app.payment.dao.RefundOrderDao;
import scw.app.payment.enums.PaymentMethod;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.event.PaymentEvent;
import scw.app.payment.event.PaymentEventDispatcher;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.model.PaymentResponse;
import scw.app.payment.pojo.Order;
import scw.app.payment.pojo.RefundOrder;
import scw.app.payment.service.PaymentService;
import scw.apple.pay.ApplePay;
import scw.apple.pay.InApp;
import scw.apple.pay.VerifyReceiptRequest;
import scw.apple.pay.VerifyReceiptResponse;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Service;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.tencent.wx.pay.RefundRequest;
import scw.tencent.wx.pay.Unifiedorder;
import scw.tencent.wx.pay.UnifiedorderRequest;
import scw.tencent.wx.pay.WeiXinPay;
import scw.tencent.wx.pay.WeiXinPayResponse;
import scw.util.Pagination;

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

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private ResultFactory resultFactory;
	private PaymentConfig paymentConfig;
	@Autowired
	private ApplePayTransactionDao applePayTransactionDao;
	private PaymentEventDispatcher paymentEventDispatcher;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private RefundOrderDao refundOrderDao;

	public PaymentServiceImpl(PaymentConfig paymentConfig, PaymentEventDispatcher paymentEventDispatche) {
		this.paymentConfig = paymentConfig;
		this.paymentEventDispatcher = paymentEventDispatche;
	}

	public Order getOrder(String orderId) {
		return orderDao.getById(orderId);
	}

	public RefundOrder getRefundOrder(String refundOrderId) {
		return refundOrderDao.getById(refundOrderId);
	}

	public DataResult<?> payment(PaymentRequest paymentRequest, Order order) {
		if (order.getPrice() == 0) {
			// 不要钱的
			Result result = publish(new PaymentEvent(order.getId(), PaymentStatus.SUCCESS));
			if (result.isError()) {
				return result.dataResult();
			}

			return resultFactory.success();
		}

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
				return resultFactory.success(response.getBody());
			} catch (AlipayApiException e) {
				throw new RuntimeException(e);
			}
		} else if (order.getPaymentMethod() == PaymentMethod.WX_APP) {
			WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);
			Unifiedorder unifiedorder = weiXinPay.payment(new UnifiedorderRequest(order.getName(), order.getId(),
					order.getPrice(), order.getIp(), paymentConfig.getWeiXinPaySuccessNotifyUrl(), "APP"));
			return resultFactory.success(unifiedorder);
		} else if (order.getPaymentMethod() == PaymentMethod.WX_WEB) {
			WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);

			UnifiedorderRequest unifiedorderRequest = new UnifiedorderRequest(order.getName(), order.getId(),
					order.getPrice(), order.getIp(), paymentConfig.getWeiXinPaySuccessNotifyUrl(), "JSAPI");
			unifiedorderRequest.setOpenid(order.getWxOpenid());
			Unifiedorder unifiedorder = weiXinPay.payment(unifiedorderRequest);
			return resultFactory.success(unifiedorder);
		} else if (order.getPaymentMethod() == PaymentMethod.APPLE) {
			ApplePay applePay = paymentConfig.getApplePay(order);
			VerifyReceiptResponse response = applePay
					.verifyReceipt(new VerifyReceiptRequest(paymentRequest.getApplePayReceiptData()));
			if (response.isError()) {
				return resultFactory.error("apple pay error(" + response.getStatus() + ")");
			}

			List<String> productIds = new ArrayList<String>();
			for (InApp app : response.getReceipt().getInApps()) {
				if (applePayTransactionDao.getById(app.getTransactionId()) != null) {
					return resultFactory.error("此凭据已核销");
				}

				applePayTransactionDao.create(app.getTransactionId(), order.getId());
				productIds.add(app.getProductId());
			}

			orderDao.updateApplePayProductId(order.getId(), productIds);
			Result result = publish(new PaymentEvent(order.getId(), PaymentStatus.SUCCESS));
			if (result.isError()) {
				return result.dataResult();
			}

			return resultFactory.success();
		} else {
			return resultFactory.error("不支持的支付方式(" + order.getPayChannel());
		}
	}

	public DataResult<PaymentResponse> payment(PaymentRequest paymentRequest) {
		Order order = orderDao.create(paymentRequest);
		DataResult<?> credentialResult = payment(paymentRequest, order);
		if (credentialResult.isError()) {
			return credentialResult.dataResult();
		}

		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setCredential(credentialResult.getData());
		paymentResponse.setOrder(orderDao.getById(order.getId()));
		return resultFactory.success(paymentResponse);
	}

	public Result publish(PaymentEvent paymentEvent) {
		boolean success = orderDao.updateStatus(paymentEvent.getOrderId(), paymentEvent.getStatus());
		if (!success) {
			return resultFactory.error("订单状态错误(" + paymentEvent.getStatus() + ")");
		}

		paymentEventDispatcher.publishEvent(paymentEvent);
		return resultFactory.success();
	}

	public Result close(String orderId) {
		Order order = orderDao.getById(orderId);
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		Result result = publish(new PaymentEvent(order.getId(), PaymentStatus.CANCEL));
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
		Order order = orderDao.getById(request.getOrderId());
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		if (order.getPaymentStatus().isSwitchTo(PaymentStatus.REFUND)) {
			return resultFactory.error("订单状态错误");
		}

		if (request.getPrice() == 0) {
			return resultFactory.error("退款金额不能为0");
		}

		if (request.getPrice() > order.getPrice()) {
			return resultFactory.error("退款金额不能大于实际支付金额");
		}

		RefundOrder refundOrder = refundOrderDao.create(request);
		return refund(order, refundOrder);
	}

	public DataResult<RefundOrder> refund(Order order, RefundOrder refundOrder) {
		PaymentEvent refundEvent = new PaymentEvent(order.getId(), PaymentStatus.REFUND);
		refundEvent.setRefundOrderId(refundOrder.getId());
		if (refundOrder.getPrice() == 0) {
			Result result = publish(refundEvent);
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
			Result result = publish(refundEvent);
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
		RefundOrder refundOrder = refundOrderDao.getById(refundId);
		if (refundOrder == null) {
			return resultFactory.error("退款订单不存在");
		}

		Order order = orderDao.getById(refundOrder.getOrderId());
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		return refund(order, refundOrder);
	}

	public Pagination<Order> search(String query, int page, int limit, PaymentStatus paymentStatus,
			LogisticsStatus logisticsStatus) {
		return orderDao.search(query, page, limit, paymentStatus, logisticsStatus);
	}

	public List<RefundOrder> getRefundOrderList(String orderId) {
		return refundOrderDao.getRefundOrderList(orderId);
	}
}
