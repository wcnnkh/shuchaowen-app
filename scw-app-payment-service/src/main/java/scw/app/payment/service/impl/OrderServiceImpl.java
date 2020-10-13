package scw.app.payment.service.impl;

import scw.app.address.model.UserAddressModel;
import scw.app.discount.service.UserAccumulatedPointsService;
import scw.app.discount.service.UserVoucherService;
import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.event.PaymentEvent;
import scw.app.payment.event.PaymentEventDispatcher;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.pojo.Order;
import scw.app.payment.service.OrderService;
import scw.app.util.BaseServiceConfiguration;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.lang.Nullable;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.sql.WhereSql;
import scw.util.Pagination;

@Configuration(order = Integer.MIN_VALUE)
public class OrderServiceImpl extends BaseServiceConfiguration implements OrderService {
	private UserAccumulatedPointsService userAccumulatedPointsService;
	private UserVoucherService userVoucherService;
	private PaymentEventDispatcher paymentEventDispatcher;

	public OrderServiceImpl(DB db, ResultFactory resultFactory,
			@Nullable UserAccumulatedPointsService userAccumulatedPointsService,
			@Nullable UserVoucherService userVoucherService, PaymentEventDispatcher paymentEventDispatcher) {
		super(db, resultFactory);
		this.userAccumulatedPointsService = userAccumulatedPointsService;
		this.userVoucherService = userVoucherService;
		this.paymentEventDispatcher = paymentEventDispatcher;
		db.createTable(Order.class, false);
	}

	public Order getById(String orderId) {
		return db.getById(Order.class, orderId);
	}

	public DataResult<Order> create(PaymentRequest request) {
		if (request.getAccumulatedPoints() > 0 && userAccumulatedPointsService == null) {
			return resultFactory.error("不支持积分服务");
		}

		if (request.getVoucterId() > 0 && userVoucherService == null) {
			return resultFactory.error("不支持代金券服务");
		}

		Order order = new Order();
		Copy.copy(order, request);
		if (request.getAccumulatedPoints() > 0) {
			String logId = userAccumulatedPointsService.change(request.getUid(), -request.getAccumulatedPoints(), "消费");
			if (logId == null) {
				return resultFactory.error("积分不足");
			}

			order.setPointLogId(logId);
		}

		if (request.getVoucterId() > 0) {
			String logId = userVoucherService.change(request.getUid(), request.getVoucterId(), -1, "消费");
			if (logId == null) {
				return resultFactory.error("代金券不足");
			}

			order.setVoucherLogId(logId);
		}

		if (order.getPrice() == 0) {
			// 免费商品
			order.setStatus(PaymentStatus.SUCCESS.getStatus());
		}
		db.save(order);
		return resultFactory.success(order);
	}

	public Result updateStatus(String orderId, PaymentStatus status) {
		Order order = getById(orderId);
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		PaymentStatus paymentStatus = PaymentStatus.forStatus(order.getStatus());
		if (paymentStatus == null) {
			return resultFactory.error("不存在的状态");
		}

		if (!paymentStatus.isSwitchTo(status)) {
			return resultFactory.error("订单状态错误(" + status + ")");
		}

		order.setStatus(status.getStatus());
		db.update(order);
		paymentEventDispatcher.publishEvent(new PaymentEvent(orderId, status));
		return resultFactory.success();
	}

	public Result updateAddress(String orderId, UserAddressModel addressModel) {
		Order order = getById(orderId);
		if (order == null) {
			return resultFactory.error("订单不存在");
		}

		Copy.copy(order, addressModel);
		db.update(order);
		return resultFactory.success();
	}

	public Pagination<Order> search(String query, int page, int limit, PaymentStatus paymentStatus,
			LogisticsStatus logisticsStatus) {
		WhereSql sql = new WhereSql();
		if (paymentStatus != null) {
			sql.and("status=?", paymentStatus.getStatus());
		}

		if (logisticsStatus != null) {
			sql.and("logisticsStatus=?", logisticsStatus.getStatus());
		}

		if (StringUtils.isNotEmpty(query)) {
			String like = "%" + query + "%";
			sql.or("id like ?", like);
			sql.or("name like ?", like);
		}
		return db.select(Order.class, page, limit, sql.assembleSql("select * from `order`", "order by cts desc"));
	}

}
