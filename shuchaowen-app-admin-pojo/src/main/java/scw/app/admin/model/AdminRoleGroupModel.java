package scw.app.admin.model;

import java.io.Serializable;

public class AdminRoleGroupModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int parentId;
	private String name;

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
}
