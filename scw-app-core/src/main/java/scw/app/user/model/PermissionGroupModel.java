package scw.app.user.model;

import java.io.Serializable;

import scw.mapper.MapperUtils;

public class PermissionGroupModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int parentId;
	private String name;
	private boolean disable;//是否禁用

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
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(PermissionGroupModel.class).getValueMap(this).toString();
	}
}
