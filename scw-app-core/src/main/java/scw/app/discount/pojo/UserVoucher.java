package scw.app.discount.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Counter;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 用户抵用券
 * @author shuchaowen
 *
 */
@Table
public class UserVoucher implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private long voucherId;
	@Counter
	private int count;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(long voucherId) {
		this.voucherId = voucherId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
