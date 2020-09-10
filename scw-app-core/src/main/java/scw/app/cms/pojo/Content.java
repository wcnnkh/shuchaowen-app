package scw.app.cms.pojo;

import scw.app.cms.model.BasicContent;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.AutoIncrement;
import scw.sql.orm.annotation.PrimaryKey;

public class Content extends BasicContent {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@AutoIncrement
	private long id;
	private long lastUpdateTime;// 最后一次更新时间
	private long createTime;// 创建时间
	private long deleteTime;// 删除时间
	private long createUid;
	private long lastUpdateUid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(long deleteTime) {
		this.deleteTime = deleteTime;
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
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
