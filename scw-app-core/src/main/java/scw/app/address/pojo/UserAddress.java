package scw.app.address.pojo;

import scw.app.address.model.UserAddressModel;
import scw.mapper.MapperUtils;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.AutoIncrement;
import scw.orm.sql.annotation.Index;
import scw.orm.sql.annotation.Table;

@Table
public class UserAddress extends UserAddressModel {
	private static final long serialVersionUID = 1L;
	@AutoIncrement
	@PrimaryKey
	private long id;
	@Index
	private long uid;
	private long createTime;
	private long lastUpdateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
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
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(UserAddress.class).getValueMap(this).toString();
	}
}
