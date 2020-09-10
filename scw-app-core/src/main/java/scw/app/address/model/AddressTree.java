package scw.app.address.model;

import java.util.List;

import scw.app.address.pojo.Address;
import scw.mapper.MapperUtils;

public class AddressTree extends Address {
	private static final long serialVersionUID = 1L;
	private List<AddressTree> subList;

	public List<AddressTree> getSubList() {
		return subList;
	}

	public void setSubList(List<AddressTree> subList) {
		this.subList = subList;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
