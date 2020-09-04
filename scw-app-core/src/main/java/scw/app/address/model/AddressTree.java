package scw.app.address.model;

import java.util.List;

public class AddressTree extends AddressModel {
	private static final long serialVersionUID = 1L;
	private int id;
	private List<AddressTree> subList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<AddressTree> getSubList() {
		return subList;
	}

	public void setSubList(List<AddressTree> subList) {
		this.subList = subList;
	}
}
