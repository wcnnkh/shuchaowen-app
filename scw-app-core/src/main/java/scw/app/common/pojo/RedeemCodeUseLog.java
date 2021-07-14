package scw.app.common.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.orm.annotation.PrimaryKey;
import scw.orm.generator.annotation.CreateTime;
import scw.orm.generator.annotation.Generator;
import scw.orm.sql.annotation.Index;
import scw.orm.sql.annotation.Table;

@Table
public class RedeemCodeUseLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@Generator
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
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(RedeemCodeUseLog.class).getValueMap(this).toString();
	}
}
