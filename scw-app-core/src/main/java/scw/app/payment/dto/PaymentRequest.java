package scw.app.payment.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import scw.core.utils.StringUtils;

public class PaymentRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 支付方式
	 */
	private String paymentMethod;
	
	/**
	 * 商户订单号
	 */
	private String orderId;
	
	/**
	 * 订单主体(名称/描述)
	 */
	private String subject;
	
	/**
	 * 请求处理的金额(支付就是支付金额，退款就是退款金额)
	 */
	private int price;
	
	/**
	 * 扩展数据
	 */
	private Map<String, String> extendedData;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Map<String, String> getExtendedData() {
		if(extendedData == null){
			return Collections.emptyMap();
		}
		return extendedData;
	}

	public void setExtendedData(Map<String, String> extendedData) {
		this.extendedData = extendedData;
	}
	
	public String getPriceDescribe(){
		return StringUtils.formatNothingToYuan(price);
	}
}
