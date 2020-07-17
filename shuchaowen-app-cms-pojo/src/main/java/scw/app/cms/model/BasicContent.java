package scw.app.cms.model;

import java.io.Serializable;

import scw.sql.orm.annotation.Index;

public class BasicContent implements Serializable {
	private static final long serialVersionUID = 1L;
	@Index
	private int channelId;
	private String name;
	private String describe;
	private long readContent;// 阅读数量
	private int fabulousCount;// 赞
	private int stepCount;// 踩
	private int collectionCount;// 收藏
	private int weight;// 权重
	private long releaseTime;// 发布时间
	private long offShelfTime;// 下架时间
	private boolean delete;// 是否已经删除

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
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

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
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

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public int getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(int collectionCount) {
		this.collectionCount = collectionCount;
	}
}
