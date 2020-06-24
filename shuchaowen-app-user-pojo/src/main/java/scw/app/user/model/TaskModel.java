package scw.app.user.model;

import java.io.Serializable;

import scw.sql.orm.annotation.Index;

public class TaskModel implements Serializable{
	private static final long serialVersionUID = 1L;
	@Index
	private long parentId;//上一个任务的id
	private String name;
	private String describe;
	private int condition;// 完成条件
	private long cts;
	private boolean hidden;
	private long beginTime;
	private long endTime;
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public long getCts() {
		return cts;
	}
	public void setCts(long cts) {
		this.cts = cts;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
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
}
