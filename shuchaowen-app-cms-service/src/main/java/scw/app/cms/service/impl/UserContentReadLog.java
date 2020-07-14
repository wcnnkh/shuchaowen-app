package scw.app.cms.service.impl;

import java.io.Serializable;

import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class UserContentReadLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private long contentId;
	private long readTime;
	private long lastReadTime;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getContentId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}

	public long getReadTime() {
		return readTime;
	}

	public void setReadTime(long readTime) {
		this.readTime = readTime;
	}

	public long getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(long lastReadTime) {
		this.lastReadTime = lastReadTime;
	}
}
