package scw.app.user.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.Table;

@Table
public class PermissionGroupAction implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private int groupId;
	@PrimaryKey
	private String actionId;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(PermissionGroupAction.class).getValueMap(this).toString();
	}
}
