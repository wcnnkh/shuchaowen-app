package scw.app.cms.service.impl;

import java.io.Serializable;

import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 此表可以和UserStepLog表合并
 * 
 * @author shuchaowen
 *
 */
@Table
public class UserContentFabulousLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@PrimaryKey
	private long contentId;
	private long cts;

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

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}
}
