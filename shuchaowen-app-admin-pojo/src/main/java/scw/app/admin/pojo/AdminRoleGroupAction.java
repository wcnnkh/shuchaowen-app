package scw.app.admin.pojo;

import java.io.Serializable;

import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.Table;

@Table
public class AdminRoleGroupAction implements Serializable {
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
}
