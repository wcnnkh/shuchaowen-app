package scw.app.user.model;

import java.io.Serializable;
import java.util.List;

import scw.app.user.pojo.PermissionGroup;

public class PermissionGroupTree implements Serializable {
	private static final long serialVersionUID = 1L;
	private final PermissionGroup permissionGroup;
	private final List<PermissionGroupTree> subList;

	public PermissionGroupTree(PermissionGroup permissionGroup,
			List<PermissionGroupTree> subList) {
		this.permissionGroup = permissionGroup;
		this.subList = subList;
	}

	public PermissionGroup getAdminRoleGroup() {
		return permissionGroup;
	}

	public List<PermissionGroupTree> getSubList() {
		return subList;
	}
}
