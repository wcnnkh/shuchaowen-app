package scw.app.cms.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.AutoIncrement;
import scw.sql.orm.annotation.PrimaryKey;

public class Content implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@AutoIncrement
	private long id;
	private String name;
	private String describe;
	private long readContent;// 阅读数量
	private int fabulousCount;// 赞
	private int stepCount;// 踩
	private int weight;// 权重
	private long releaseTime;// 发布时间
	private long offShelfTime;// 下架时间
	private long lastUpdateTime;//最后一次更新时间
	private long createTime;//创建时间
	private long deleteTime;// 删除时间
	private boolean delete;//是否已经删除

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getReadContent() {
		return readContent;
	}

	public void setReadContent(long readContent) {
		this.readContent = readContent;
	}

	public int getFabulousCount() {
		return fabulousCount;
	}

	public void setFabulousCount(int fabulousCount) {
		this.fabulousCount = fabulousCount;
	}

	public int getStepCount() {
		return stepCount;
	}

	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public long getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

	public long getOffShelfTime() {
		return offShelfTime;
	}

	public void setOffShelfTime(long offShelfTime) {
		this.offShelfTime = offShelfTime;
	}

	public long getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(long deleteTime) {
		this.deleteTime = deleteTime;
	}
}
