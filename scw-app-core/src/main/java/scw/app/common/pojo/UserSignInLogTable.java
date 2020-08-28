package scw.app.common.pojo;

import java.io.Serializable;

import scw.app.common.model.UserSignInLog;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.util.CalendarUtils;

/**
 * 用户签到日志
 * 
 * @author shuchaowen
 *
 */
@Table
public class UserSignInLogTable implements Serializable, UserSignInLog {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private int type;
	@PrimaryKey
	private long signInTime;
	private long createTime;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(long signInTime) {
		this.signInTime = signInTime;
	}
	
	public boolean isSupplementary(){
		return CalendarUtils.getDayBeginCalendar(signInTime).getTimeInMillis() < CalendarUtils.getDayBeginCalendar(createTime).getTimeInMillis();
	}
}
