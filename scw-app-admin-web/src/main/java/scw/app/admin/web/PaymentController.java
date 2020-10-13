package scw.app.admin.web;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.pojo.Order;
import scw.app.payment.service.OrderService;
import scw.app.user.security.LoginRequired;
import scw.beans.annotation.Autowired;
import scw.core.parameter.annotation.DefaultValue;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.annotation.Controller;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;
import scw.util.Pagination;

@LoginRequired
@Controller(value = "/admin/payment")
@ActionAuthority(value = "支付管理", menu = true)
public class PaymentController {
	@Autowired
	private PageFactory pageFactory;
	private OrderService orderService;
	
	public PaymentController(OrderService orderService){
		this.orderService = orderService;
	}

	@ActionAuthority(value = "订单列表", menu = true)
	@Controller(value = "list")
	public Page list(String query, @DefaultValue("1") int page, @DefaultValue("10") int limit,
			PaymentStatus paymentStatus, LogisticsStatus logisticsStatus) {
		Page view = pageFactory.getPage("/admin/ftl/payment/order_list.ftl");
		Pagination<Order> pagination = orderService.search(query, page, limit, paymentStatus, logisticsStatus);
		view.put("list", pagination.getData());
		view.put("totalCount", pagination.getTotalCount());
		view.put("maxPage", pagination.getMaxPage());
		view.put("page", page);
		view.put("paymentStatus", paymentStatus);
		view.put("logisticsStatus", logisticsStatus);
		view.put("query", query);
		appendConfig(view);
		return view;
	}

	private void appendConfig(Page page) {
		page.put("paymentStatusConfigs", PaymentStatus.values());
		page.put("logisticsStatusConfigs", LogisticsStatus.values());
	}
}
