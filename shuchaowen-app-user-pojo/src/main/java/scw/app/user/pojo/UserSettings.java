package scw.app.user.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.Column;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class UserSettings implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@Column(type="text")
	private String settings;
	private long lastUpdateTime;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getSettings() {
		return settings;
	}
	public void setSettings(String settings) {
		this.settings = settings;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
