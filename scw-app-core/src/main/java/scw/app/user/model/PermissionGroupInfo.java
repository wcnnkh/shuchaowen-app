package scw.app.user.model;

import java.util.Collection;

import scw.mapper.MapperUtils;

public class PermissionGroupInfo extends PermissionGroupModel{
	private static final long serialVersionUID = 1L;
	private int id;
	private Collection<String> authorityIds;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Collection<String> getAuthorityIds() {
		return authorityIds;
	}
	public void setAuthorityIds(Collection<String> authorityIds) {
		this.authorityIds = authorityIds;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(PermissionGroupInfo.class).getValueMap(this).toString();
	}
}
