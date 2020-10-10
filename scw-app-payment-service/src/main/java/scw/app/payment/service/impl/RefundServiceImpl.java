package scw.app.payment.service.impl;

import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.RefundRequest;
import scw.app.payment.pojo.Order;
import scw.app.payment.pojo.RefundOrder;
import scw.app.payment.service.OrderService;
import scw.app.payment.service.RefundOrderService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.DataResult;
import scw.result.ResultFactory;

@Configuration(order = Integer.MIN_VALUE)
public class RefundServiceImpl extends BaseServiceConfiguration implements RefundOrderService {
	@Autowired
	private OrderService orderService;

	public RefundServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(RefundOrder.class, false);
	}

	public DataResult<RefundOrder> create(RefundRequest request) {
		Order order = orderService.getById(request.getOrderId());
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		if (order.getPaymentStatus().isSwitchTo(PaymentStatus.REFUND)) {
			return resultFactory.error("订单状态错误");
		}

		if (request.getPrice() != null && request.getPrice() > order.getPrice()) {
			return resultFactory.error("退款金额不能大于实际支付金额");
		}

		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setOrderId(request.getOrderId());
		refundOrder.setRefundDesc(request.getRefundDesc());
		refundOrder.setPrice(request.getPrice() == null ? order.getPrice() : request.getPrice());
		db.save(refundOrder);
		return resultFactory.success();
	}

	public RefundOrder getById(String id) {
		return db.getById(RefundOrder.class, id);
	}
}
