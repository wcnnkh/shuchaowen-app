package scw.app.payment.pojo;

import scw.app.payment.model.RefundRequest;
import scw.core.utils.XTime;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

@Table
public class RefundOrder extends RefundRequest {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	private String id;
	@CreateTime
	private long cts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}

	public String getCtsDescribe() {
		return XTime.format(cts, "yyyy-MM-dd HH:mm:ss");
	}
}
