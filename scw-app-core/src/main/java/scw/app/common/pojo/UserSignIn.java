package scw.app.common.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 用户签到
 * 
 * @author shuchaowen
 *
 */
@Table
public class UserSignIn implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private int type;
	private int count;// 连续签到次数
	private long lastSignInTime;// 最后一次连续签到时间

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getLastSignInTime() {
		return lastSignInTime;
	}

	public void setLastSignInTime(long lastSignInTime) {
		this.lastSignInTime = lastSignInTime;
	}
}
