package scw.app.cms.pojo;

import scw.app.cms.model.BasicChannel;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class Channel extends BasicChannel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private int id;
	private long createTime;
	private long lastUpdateTime;
	private long createUid;
	private long lastUpdateUid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public long getCreateUid() {
		return createUid;
	}

	public void setCreateUid(long createUid) {
		this.createUid = createUid;
	}

	public long getLastUpdateUid() {
		return lastUpdateUid;
	}

	public void setLastUpdateUid(long lastUpdateUid) {
		this.lastUpdateUid = lastUpdateUid;
	}
}
