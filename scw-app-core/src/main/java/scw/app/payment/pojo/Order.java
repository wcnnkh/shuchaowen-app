package scw.app.payment.pojo;

import scw.app.logistics.enums.LogisticsStatus;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.model.BasePaymentRequest;
import scw.core.utils.XTime;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

@Table
public class Order extends BasePaymentRequest {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	private String id;
	@Index
	private int status;// 订单状态
	private String pointLogId;// 积分使用日志id
	private String voucherLogId;// 代金券使用日志id
	@CreateTime
	private long cts;
	// 物流状态
	@Index
	private int logisticsStatus;

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

	public String getPointLogId() {
		return pointLogId;
	}

	public void setPointLogId(String pointLogId) {
		this.pointLogId = pointLogId;
	}

	public String getVoucherLogId() {
		return voucherLogId;
	}

	public void setVoucherLogId(String voucherLogId) {
		this.voucherLogId = voucherLogId;
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

	public String getCtsDescribe() {
		return XTime.format(cts, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
