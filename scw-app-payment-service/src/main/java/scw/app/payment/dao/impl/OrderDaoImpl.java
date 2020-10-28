package scw.app.payment.dao.impl;

import java.util.List;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.dao.OrderDao;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.pojo.Order;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Copy;
import scw.sql.WhereSql;
import scw.util.Pagination;

@Configuration(order = Integer.MIN_VALUE)
public class OrderDaoImpl implements OrderDao {
	private DB db;

	public OrderDaoImpl(DB db) {
		this.db = db;
		db.createTable(Order.class, false);
	}

	public Order getById(String orderId) {
		return db.getById(Order.class, orderId);
	}

	public Order create(PaymentRequest request) {
		Order order = new Order();
		Copy.copy(order, request);
		order.setStatus(PaymentStatus.CREATED.getStatus());
		db.save(order);
		return order;
	}

	public boolean updateStatus(String orderId, PaymentStatus status) {
		Order order = getById(orderId);
		if (order == null) {
			return false;
		}

		PaymentStatus paymentStatus = PaymentStatus.forStatus(order.getStatus());
		if (paymentStatus == null) {
			return false;
		}

		if (!paymentStatus.isSwitchTo(status)) {
			return false;
		}

		order.setStatus(status.getStatus());
		return db.update(order);
	}

	public boolean updateApplePayProductId(String orderId, List<String> productIds) {
		Order order = getById(orderId);
		if (order == null) {
			return false;
		}

		order.setAppleProductIds(productIds);
		db.update(order);
		return true;
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
