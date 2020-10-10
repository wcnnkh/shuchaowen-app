package scw.app.payment.service;

import scw.app.payment.model.RefundRequest;
import scw.app.payment.pojo.RefundOrder;
import scw.result.DataResult;

public interface RefundOrderService {
	RefundOrder getById(String id);

	DataResult<RefundOrder> create(RefundRequest request);
}
