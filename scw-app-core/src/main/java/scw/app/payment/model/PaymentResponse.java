package scw.app.payment.model;

import java.io.Serializable;

import scw.app.payment.pojo.Order;

public class PaymentResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Order order;
	private Object credential;// 凭据

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Object getCredential() {
		return credential;
	}

	public void setCredential(Object credential) {
		this.credential = credential;
	}
}
