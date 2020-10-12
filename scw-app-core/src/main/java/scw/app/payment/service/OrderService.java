package scw.app.payment.service;

import scw.app.address.model.UserAddressModel;
import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.PaymentRequest;
import scw.app.payment.pojo.Order;
import scw.result.DataResult;
import scw.result.Result;
import scw.util.Pagination;

public interface OrderService {
	/**
	 * 获取订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	Order getById(String orderId);

	/**
	 * 创建订单
	 * 
	 * @param request
	 * @return
	 */
	DataResult<Order> create(PaymentRequest request);

	/**
	 * 修改订单状态
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 */
	Result updateStatus(String orderId, PaymentStatus status);

	/**
	 * 修改收货信息
	 * 
	 * @param orderId
	 * @param addressModel
	 * @return
	 */
	Result updateAddress(String orderId, UserAddressModel addressModel);

	Pagination<Order> search(String query, int page, int limit, PaymentStatus paymentStatus,
			LogisticsStatus logisticsStatus);
}
