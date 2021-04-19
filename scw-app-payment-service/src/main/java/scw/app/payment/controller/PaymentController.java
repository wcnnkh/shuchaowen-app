package scw.app.payment.controller;

import scw.app.payment.PaymentConfig;
import scw.app.payment.PaymentStatusDispatcher;
import scw.app.payment.PaymentStatusEvent;
import scw.app.payment.service.impl.PaymentServiceImpl;
import scw.beans.annotation.Autowired;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;

@Controller(value=PaymentConfig.CONTROLLER_PREFIX, methods=HttpMethod.POST)
public class PaymentController {
	@Autowired
	private PaymentServiceImpl paymentServiceImpl;
	@Autowired
	private PaymentStatusDispatcher paymentStatusDispatcher;
	
	@Controller(value="/{paymentMethod}/{paymentStatus}")
	public void callback(String paymentMethod, String paymentStatus, HttpChannel httpChannel){
		PaymentStatusEvent event = paymentServiceImpl.callback(paymentMethod, paymentStatus, httpChannel);
		paymentStatusDispatcher.publishEvent(event);
	}
}
