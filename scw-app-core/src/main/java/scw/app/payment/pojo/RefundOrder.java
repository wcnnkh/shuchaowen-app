package scw.app.payment.pojo;

import java.io.Serializable;

import scw.core.utils.StringUtils;
import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

@Table
public class RefundOrder implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	private String id;
	@Index
	private String orderId;
	private int price;
	private String refundDesc;
	@CreateTime
	private long cts;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getPriceDescribe(){
		return StringUtils.formatNothingToYuan(price);
	}
	
	public String getRefundDesc() {
		return refundDesc;
	}
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
}
