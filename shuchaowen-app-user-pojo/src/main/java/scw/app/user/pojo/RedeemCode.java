package scw.app.user.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.Counter;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 兑换码
 * 
 * @author shuchaowen
 *
 */
@Table
public class RedeemCode implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private int type;
	@PrimaryKey
	private String code;
	@Counter
	private int usableCount;// 最大可使用的次数 -1是不限制次数
	private long usableBeginTime;// 0表示不限制时间
	private long usableEndTime;// 0表示不限制时间

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

	public int getUsableCount() {
		return usableCount;
	}

	public void setUsableCount(int usableCount) {
		this.usableCount = usableCount;
	}

	public long getUsableBeginTime() {
		return usableBeginTime;
	}

	public void setUsableBeginTime(long usableBeginTime) {
		this.usableBeginTime = usableBeginTime;
	}

	public long getUsableEndTime() {
		return usableEndTime;
	}

	public void setUsableEndTime(long usableEndTime) {
		this.usableEndTime = usableEndTime;
	}
}