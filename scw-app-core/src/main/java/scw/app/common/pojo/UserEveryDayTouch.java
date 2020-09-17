package scw.app.common.pojo;

import java.io.Serializable;

import scw.core.utils.XTime;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class UserEveryDayTouch implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private int type;
	private int count;
	private long lastTouchTime;

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

	public long getLastTouchTime() {
		return lastTouchTime;
	}

	public void setLastTouchTime(long lastTouchTime) {
		this.lastTouchTime = lastTouchTime;
	}

	public boolean isCanTouch(int maxCount) {
		if(XTime.isToday(lastTouchTime)){
			return count >= maxCount;
		}
		return true;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
