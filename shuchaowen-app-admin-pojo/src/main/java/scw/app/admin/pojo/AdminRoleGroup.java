package scw.app.admin.pojo;

import scw.app.admin.model.AdminRoleGroupModel;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.AutoIncrement;
import scw.orm.sql.annotation.Table;

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
