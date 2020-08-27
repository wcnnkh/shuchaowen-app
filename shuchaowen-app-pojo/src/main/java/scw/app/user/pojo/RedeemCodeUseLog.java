package scw.app.user.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

@Table
public class RedeemCodeUseLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	private String id;
	@Index
	private long uid;
	@Index
	private int redeemCodeType;
	@Index
	private String redeemCode;
	@CreateTime
	private long cts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getRedeemCodeType() {
		return redeemCodeType;
	}

	public void setRedeemCodeType(int redeemCodeType) {
		this.redeemCodeType = redeemCodeType;
	}

	public String getRedeemCode() {
		return redeemCode;
	}

	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}
}
