package scw.app.payment;

import java.util.Collections;
import java.util.Map;

import scw.event.BasicEvent;
import scw.lang.Nullable;

public class PaymentStatusEvent extends BasicEvent{
	private static final long serialVersionUID = 1L;
	/**
	 * 支付状态
	 */
	private final String paymentStatus;
	
	/**
	 * 商户的订单id
	 */
	private final String merchantOrderId;
	/**
	 * 平台订单号(如：微信、支付宝)
	 */
	@Nullable
	private final String platformOrderId;
	
	
	/**
	 * 支付方式
	 */
	private final String paymentMethod;
	
	/**
	 * 支付金额
	 */
	private final int price;
	
	/**
	 * 扩展数据
	 */
	private final Map<String, String> extendedData;
	
	public PaymentStatusEvent(long createTime, String paymentStatus, String merchantOrderId, @Nullable String platformOrderId, String paymentMethod, int price, Map<String, String> extendedData){
		super(createTime);
		this.paymentStatus = paymentStatus;
		this.merchantOrderId = merchantOrderId;
		this.platformOrderId = platformOrderId;
		this.paymentMethod = paymentMethod;
		this.price = price;
		this.extendedData = extendedData;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public String getPlatformOrderId() {
		return platformOrderId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public int getPrice() {
		return price;
	}

	public Map<String, String> getExtendedData() {
		if(extendedData == null){
			return Collections.emptyMap();
		}
		
		return extendedData;
	}
	
}
