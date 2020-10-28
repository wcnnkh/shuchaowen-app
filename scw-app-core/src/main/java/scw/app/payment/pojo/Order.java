package scw.app.payment.pojo;

import java.util.Collections;
import java.util.List;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.OrderModel;
import scw.core.utils.XTime;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Column;
import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

@Table
public class Order extends OrderModel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	private String id;
	@Index
	private int status;// 订单状态
	@CreateTime
	private long cts;
	// 物流状态
	@Index
	private int logisticsStatus;
	@Column(type = "text")
	private List<String> appleProductIds;// apple pay支付的productIds

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public PaymentStatus getPaymentStatus() {
		return PaymentStatus.forStatus(status);
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}

	public int getLogisticsStatus() {
		return logisticsStatus;
	}

	public LogisticsStatus toLogisticsStatus() {
		return LogisticsStatus.forStatus(logisticsStatus);
	}

	public void setLogisticsStatus(int logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	public List<String> getAppleProductIds() {
		if(appleProductIds == null){
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableList(appleProductIds);
	}

	public void setAppleProductIds(List<String> appleProductIds) {
		this.appleProductIds = appleProductIds;
	}

	public String getCtsDescribe() {
		return XTime.format(cts, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
