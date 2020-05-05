package scw.app.payment;

import java.io.Serializable;
import java.util.Currency;

/**
 * 支付请求
 * 
 * @author shuchaowen
 *
 */
public class PaymentRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderNo;
	private String channel;
	private long amount;
	private String clientIp;
	private Currency currency;
	private String subject;
	private String body;
	private long timeExpire;
	private String description;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(long timeExpire) {
		this.timeExpire = timeExpire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
