package scw.app.payment.service.impl;

import java.util.List;

import scw.app.payment.PaymentServiceAdapter;
import scw.app.payment.PaymentStatusEvent;
import scw.app.payment.dto.PaymentResponse;
import scw.app.payment.dto.RefundRequest;
import scw.app.payment.dto.UnifiedPaymentRequest;
import scw.app.payment.service.PaymentService;
import scw.beans.BeanFactory;
import scw.beans.annotation.Service;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.lang.NotSupportedException;
import scw.mvc.HttpChannel;

@Service
public class PaymentServiceImpl implements PaymentService, PaymentServiceAdapter {
	private List<PaymentServiceAdapter> adapters;
	
	public PaymentServiceImpl(BeanFactory beanFactory) {
		this.adapters = beanFactory.getServiceLoader(PaymentServiceAdapter.class).toList();
	}

	@Override
	public DataResult<PaymentResponse> payment(UnifiedPaymentRequest request) {
		for(PaymentServiceAdapter adapter : adapters){
			if(adapter.isAccept(request.getPaymentMethod())){
				return adapter.payment(request);
			}
		}
		throw new NotSupportedException("不支持的支付方式:" + request.getPaymentMethod());
	}

	@Override
	public Result refund(RefundRequest request) {
		for(PaymentServiceAdapter adapter : adapters){
			if(adapter.isAccept(request.getPaymentMethod())){
				return adapter.refund(request);
			}
		}
		throw new NotSupportedException("不支持的支付方式:" + request.getPaymentMethod());
	}

	@Override
	public boolean isAccept(String paymentMethod) {
		for(PaymentServiceAdapter adapter : adapters){
			if(adapter.isAccept(paymentMethod)){
				return true;
			}
		}
		return false;
	}

	@Override
	public PaymentStatusEvent callback(String paymentMethod,
			String paymentStatus, HttpChannel httpChannel) {
		for(PaymentServiceAdapter adapter : adapters){
			if(adapter.isAccept(paymentMethod)){
				return adapter.callback(paymentMethod, paymentStatus, httpChannel);
			}
		}
		throw new NotSupportedException("不支持的支付方式:" + paymentMethod);
	}
}
