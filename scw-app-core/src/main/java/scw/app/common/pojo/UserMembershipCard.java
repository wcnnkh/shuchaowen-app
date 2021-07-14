package scw.app.common.pojo;

import java.io.Serializable;

import scw.lang.Description;
import scw.orm.annotation.PrimaryKey;
import scw.orm.generator.annotation.CreateTime;
import scw.orm.generator.annotation.Generator;
import scw.orm.sql.annotation.Index;
import scw.orm.sql.annotation.Table;

@Table(comment = "用户会员卡")
public class UserMembershipCard implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@Generator
	@Description("会员卡id")
	private String id;
	@Index
	private long uid;
	@Description("会员卡类型")
	private int type;
	@CreateTime
	@Description("创建时间")
	private long cts;
	@Description("生效时间")
	private long beginTime;
	@Description("失效时间")
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
