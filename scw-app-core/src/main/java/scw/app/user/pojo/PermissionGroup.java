package scw.app.user.pojo;

import scw.app.user.model.PermissionGroupModel;
import scw.mapper.MapperUtils;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.AutoIncrement;
import scw.orm.sql.annotation.Table;

@Table
public class PermissionGroup extends PermissionGroupModel {
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

	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(PermissionGroup.class).getValueMap(this).toString();
	}
}
