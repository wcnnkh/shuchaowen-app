package scw.app.common.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.Column;
import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

@Table(comment = "用户会员卡")
public class UserMembershipCard implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	@Column(comment = "会员卡id")
	private String id;
	@Index
	private long uid;
	@Column(comment = "会员卡类型")
	private int type;
	@CreateTime
	@Column(comment = "创建时间")
	private long cts;
	@Column(comment = "生效时间")
	private long beginTime;
	@Column(comment = "失效时间")
	private long endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isAvailable() {
		long t = System.currentTimeMillis();
		return t >= beginTime && t < endTime;
	}
}
