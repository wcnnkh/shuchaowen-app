package scw.app.common.service.impl;

import java.io.Serializable;

import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.cache.annotation.TemporaryCacheEnable;

@TemporaryCacheEnable(value=false)
@Table
public class RedeemCodeMaxIdTable implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private int type;
	private int maxId;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMaxId() {
		return maxId;
	}
	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}
}
