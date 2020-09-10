package scw.app.address.model;

import java.util.List;

import scw.app.address.pojo.Address;
import scw.app.address.pojo.UserAddress;
import scw.mapper.MapperUtils;

public class UserAddressInfo extends UserAddress {
	private static final long serialVersionUID = 1L;
	private List<Address> addresses;

	public String getAddress() {
		StringBuilder sb = new StringBuilder();
		for (Address address : addresses) {
			sb.append(address.getName());
		}
		return sb.toString();
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
