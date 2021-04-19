package scw.app.payment;

import scw.beans.annotation.ConfigurationProperties;
import scw.beans.annotation.Value;

@ConfigurationProperties(prefix="payment")
public class PaymentConfig {
	public static final String CONTROLLER_PREFIX = "${payment.controller.prefix:/payment}";
	
	@Value(value=CONTROLLER_PREFIX, listener = false)
	private String controler;
	
	private String callbackHost;
	
	public String getPaymentCallbackUrl(String paymentMethod, String paymentStatus){
		return getCallbackHost() + "/" + controler + "/" + paymentMethod + "/" + paymentStatus;
	}

	public String getCallbackHost() {
		return callbackHost;
	}

	public void setCallbackHost(String callbackHost) {
		this.callbackHost = callbackHost;
	}
}
