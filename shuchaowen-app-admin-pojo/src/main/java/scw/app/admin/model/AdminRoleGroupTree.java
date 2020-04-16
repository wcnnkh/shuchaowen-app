package scw.app.admin.model;

import java.io.Serializable;
import java.util.List;

import scw.app.admin.pojo.AdminRoleGroup;

public class AdminRoleGroupTree implements Serializable {
	private static final long serialVersionUID = 1L;
	private final AdminRoleGroup adminRoleGroup;
	private final List<AdminRoleGroupTree> subList;

	public AdminRoleGroupTree(AdminRoleGroup adminRoleGroup,
			List<AdminRoleGroupTree> subList) {
		this.adminRoleGroup = adminRoleGroup;
		this.subList = subList;
	}

	public AdminRoleGroup getAdminRoleGroup() {
		return adminRoleGroup;
	}

	public List<AdminRoleGroupTree> getSubList() {
		return subList;
	}
}
