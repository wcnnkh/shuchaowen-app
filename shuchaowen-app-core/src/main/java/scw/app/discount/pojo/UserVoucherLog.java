package scw.app.discount.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class UserVoucherLog implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private String id;
	@Index
	private int group;
	@Index
	private long uid;
	@Index
	private long voucherId;
	private int count;
	private long cts;
	private boolean cancel;
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
	public long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(long voucherId) {
		this.voucherId = voucherId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getCts() {
		return cts;
	}
	public void setCts(long cts) {
		this.cts = cts;
	}
	public boolean isCancel() {
		return cancel;
	}
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
}
