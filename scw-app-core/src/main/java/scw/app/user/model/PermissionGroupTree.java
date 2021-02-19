package scw.app.user.model;

import java.io.Serializable;
import java.util.List;

import scw.app.user.pojo.PermissionGroup;
import scw.mapper.MapperUtils;

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
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(PermissionGroupTree.class).getValueMap(this).toString();
	}
}
