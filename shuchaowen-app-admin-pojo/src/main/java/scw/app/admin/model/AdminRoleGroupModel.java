package scw.app.admin.model;

import java.io.Serializable;

public class AdminRoleGroupModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int parentId;
	private String name;
	private boolean disable;

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}
}
