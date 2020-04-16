package scw.app.admin.pojo;

import scw.app.admin.model.AdminRoleModel;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.AutoIncrement;
import scw.orm.sql.annotation.Table;

@Table
public class AdminRole extends AdminRoleModel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@AutoIncrement
	private int id;
	private long lastUpdatePasswordTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLastUpdatePasswordTime() {
		return lastUpdatePasswordTime;
	}

	public void setLastUpdatePasswordTime(long lastUpdatePasswordTime) {
		this.lastUpdatePasswordTime = lastUpdatePasswordTime;
	}
}
