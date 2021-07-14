package scw.app.common.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.Table;

/**
 * 用户使用兑换码日志
 * 
 * @author shuchaowen
 *
 */
@Table
public class UserRedeemCodeLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private int type;
	@PrimaryKey
	private String code;
	private long cts;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(UserRedeemCodeLog.class).getValueMap(this).toString();
	}
}
