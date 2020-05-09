package scw.app.admin.pojo;

import scw.app.admin.model.AdminRoleGroupModel;
import scw.sql.orm.annotation.AutoIncrement;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class AdminRoleGroup extends AdminRoleGroupModel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@AutoIncrement
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
